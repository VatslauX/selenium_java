package com.mycompany.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import org.apache.commons.codec.binary.Base64;

public class BaseTest {

	public WebDriver driver;
	public WebDriverWait wait;
	public Actions actions;
	public Alert alert;
	public JavascriptExecutor js;
	public TakesScreenshot screenshot;

	private static ArrayList<String> chromeExtensions = new ArrayList<>();
	static {
		chromeExtensions.add("chropath"); // without .crx extension
	}

	private static String extensionDir = "C:\\Users\\Serguei\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Extensions";

	private static final String browser = getPropertyEnv("webdriver.driver",
			"chrome"); // "firefox";
	private static final Map<String, String> browserDrivers = new HashMap<>();
	static {
		browserDrivers.put("chrome", "chromedriver.exe");
		browserDrivers.put("firefox", "geckodriver.exe");
	}

	private static String osName;

	public int scriptTimeout = 5;
	public int flexibleWait = 120;
	public int implicitWait = 1;
	public long pollingInterval = 500;

	public String baseURL = "about:blank";

	// WARNING: do not use @Before... or @AfterSuite otherwise the descendant test
	// class may fail
	@AfterSuite
	public void afterSuite() throws Exception {
	}

	// WARMING: do not define or the descendant test class will fail
	@BeforeSuite
	public void beforeSuite() {
	}

	@SuppressWarnings("deprecation")
	@BeforeClass
	public void beforeClass() throws IOException {

		getOsName();
		if (browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					(new File("c:/java/selenium/chromedriver.exe")).getAbsolutePath());
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			ChromeOptions chromeOptions = new ChromeOptions();

			HashMap<String, Object> chromePrefs = new HashMap<>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			String downloadFilepath = System.getProperty("user.dir")
					+ System.getProperty("file.separator") + "target"
					+ System.getProperty("file.separator");
			chromePrefs.put("download.default_directory", downloadFilepath);
			chromePrefs.put("enableNetwork", "true");
			chromeOptions.setExperimentalOption("prefs", chromePrefs);
			chromeOptions.addArguments("allow-running-insecure-content");
			chromeOptions.addArguments("allow-insecure-localhost");
			chromeOptions.addArguments("enable-local-file-accesses");
			chromeOptions.addArguments("disable-notifications");
			// chromeOptions.addArguments("start-maximized");
			chromeOptions.addArguments("browser.download.folderList=2");
			chromeOptions.addArguments(
					"--browser.helperApps.neverAsk.saveToDisk=image/jpg,text/csv,text/xml,application/xml,application/vnd.ms-excel,application/x-excel,application/x-msexcel,application/excel,application/pdf");
			chromeOptions.addArguments("browser.download.dir=" + downloadFilepath);
			// chromeOptions.addArguments("user-data-dir=/path/to/your/custom/profile");
			capabilities
					.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
			capabilities.setCapability(chromeOptions.CAPABILITY, chromeOptions);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

			// https://stackoverflow.com/questions/35858679/adding-extension-to-selenium2webdriver-chrome-driver
			// https://productforums.google.com/forum/#!topic/chrome/g02KlhK12fU

			ArrayList<String> chromeExtensionsBase64Encoded = new ArrayList<>();
			for (String extensionName : chromeExtensions) {
				String extensionLocation = extensionDir + "\\" + extensionName + ".crx";
				System.err.println("About to load extension " + extensionLocation);
				File extensionFile = new File(extensionLocation);

				if (extensionFile.exists() && !extensionFile.isDirectory()) {
					// origin:
					// http://www.oodlestechnologies.com/blogs/Encode-%26-Decode-Image-Using-Base64-encoding-and-Decoding
					// http://www.java2s.com/Code/Java/File-Input-Output/Base64encodedecodedatausingtheBase64encodingscheme.htm
					try {
						FileInputStream extensionFileInputStream = new FileInputStream(
								extensionFile);
						byte extensionData[] = new byte[(int) extensionFile.length()];
						extensionFileInputStream.read(extensionData);

						byte[] base64EncodedByteArray = Base64.encodeBase64(extensionData);

						extensionFileInputStream.close();
						chromeExtensionsBase64Encoded
								.add(new String(base64EncodedByteArray));
						System.out.println(String.format(
								"Chrome Extension successfully encoded and added: %s...",
								new String(base64EncodedByteArray).substring(0, 64)));
					} catch (FileNotFoundException e) {
						System.out
								.println("Chrome Extension Not Found on that Location" + e);
					} catch (IOException ex) {
						System.out.println("Problem in Reading The Chrome Extension" + ex);
					}
				}
				chromeOptions.addEncodedExtensions(chromeExtensionsBase64Encoded);
			}
			driver = new ChromeDriver(capabilities);
		} else if (browser.equals("firefox")) {

			System.setProperty("webdriver.gecko.driver",
					osName.toLowerCase().startsWith("windows")
							? new File("c:/java/selenium/geckodriver.exe").getAbsolutePath()
							: "/tmp/geckodriver");
			System
					.setProperty("webdriver.firefox.bin",
							osName.toLowerCase().startsWith("windows") ? new File(
									"c:/Program Files (x86)/Mozilla Firefox/firefox.exe")
											.getAbsolutePath()
									: "/usr/bin/firefox");
			// https://github.com/SeleniumHQ/selenium/wiki/DesiredCapabilities
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			// use legacy FirefoxDriver
			capabilities.setCapability("marionette", false);
			// http://www.programcreek.com/java-api-examples/index.php?api=org.openqa.selenium.firefox.FirefoxProfile
			capabilities.setCapability("locationContextEnabled", false);
			capabilities.setCapability("acceptSslCerts", true);
			capabilities.setCapability("elementScrollBehavior", 1);
			FirefoxProfile profile = new FirefoxProfile();
			profile.setAcceptUntrustedCertificates(true);
			profile.setAssumeUntrustedCertificateIssuer(true);
			profile.setEnableNativeEvents(false);

			System.out.println(System.getProperty("user.dir"));
			capabilities.setCapability(FirefoxDriver.PROFILE, profile);
			try {
				driver = new FirefoxDriver(capabilities);
			} catch (WebDriverException e) {
				e.printStackTrace();
				throw new RuntimeException("Cannot initialize Firefox driver");
			}
		}
		actions = new Actions(driver);

		driver.manage().timeouts().setScriptTimeout(scriptTimeout,
				TimeUnit.SECONDS);
		// Declare a wait time
		wait = new WebDriverWait(driver, flexibleWait);
		wait.pollingEvery(pollingInterval, TimeUnit.MILLISECONDS);
		screenshot = ((TakesScreenshot) driver);
		js = ((JavascriptExecutor) driver);
		// driver.manage().window().maximize();

		// Go to URL
		driver.get(baseURL);
	}

	@AfterClass
	public void afterClass() throws Exception {
		driver.get("about:blank");
		if (driver != null) {
			driver.quit();
		}
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		String methodName = method.getName();
		System.out.println("Test Name: " + methodName + "\n");
	}

	@AfterMethod
	public void afterMethod() {
		// driver.get("about:blank");
	}

	@AfterTest(alwaysRun = true)
	public void afterTest() {
		killProcess(browserDrivers.get(browser));
	}

	public void highlight(WebElement element) {
		highlight(element, 100);
	}

	public void highlight(WebElement element, long highlight_interval) {
		if (wait == null) {
			wait = new WebDriverWait(driver, flexibleWait);
		}
		wait.pollingEvery(pollingInterval, TimeUnit.MILLISECONDS);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			if (driver instanceof JavascriptExecutor) {
				((JavascriptExecutor) driver).executeScript(
						"arguments[0].style.border='3px solid yellow'", element);
			}
			Thread.sleep(highlight_interval);
			if (driver instanceof JavascriptExecutor) {
				((JavascriptExecutor) driver)
						.executeScript("arguments[0].style.border=''", element);
			}
		} catch (InterruptedException e) {
			// System.err.println("Ignored: " + e.toString());
		}
	}

	public Object executeScript(String script, Object... arguments) {
		if (driver instanceof JavascriptExecutor) {
			JavascriptExecutor javascriptExecutor = JavascriptExecutor.class
					.cast(driver);
			return javascriptExecutor.executeScript(script, arguments);
		} else {
			throw new RuntimeException("Script execution failed.");
		}
	}

	public void sleep(Integer seconds) {
		long secondsLong = (long) seconds;
		try {
			Thread.sleep(secondsLong);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Utilities
	public static String getOsName() {
		if (osName == null) {
			osName = System.getProperty("os.name");
		}
		return osName;
	}

	// origin:
	// https://github.com/TsvetomirSlavov/wdci/blob/master/code/src/main/java/com/seleniumsimplified/webdriver/manager/EnvironmentPropertyReader.java
	public static String getPropertyEnv(String name, String defaultValue) {
		String value = System.getProperty(name);
		if (value == null) {
			value = System.getenv(name);
			if (value == null) {
				value = defaultValue;
			}
		}
		return value;
	}

	// https://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
	public static void killProcess(String processName) {

		String command = String.format((osName.toLowerCase().startsWith("windows"))
				? "taskkill.exe /F /IM %s" : "killall %s", processName.trim());

		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(command);
			// process.redirectErrorStream( true);

			BufferedReader stdoutBufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			BufferedReader stderrBufferedReader = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));
			String line = null;

			StringBuffer processOutput = new StringBuffer();
			while ((line = stdoutBufferedReader.readLine()) != null) {
				processOutput.append(line);
			}
			StringBuffer processError = new StringBuffer();
			while ((line = stderrBufferedReader.readLine()) != null) {
				processError.append(line);
			}
			int exitCode = process.waitFor();
			// ignore exit code 128: the process "<browser driver>" not found.
			if (exitCode != 0 && (exitCode ^ 128) != 0) {
				System.out.println("Process exit code: " + exitCode);
				if (processOutput.length() > 0) {
					System.out.println("<OUTPUT>" + processOutput + "</OUTPUT>");
				}
				if (processError.length() > 0) {
					// e.g.
					// The process "chromedriver.exe"
					// with PID 5540 could not be terminated.
					// Reason: Access is denied.
					System.out.println("<ERROR>" + processError + "</ERROR>");
				}
			}
		} catch (Exception e) {
			System.err.println("Exception (ignored): " + e.getMessage());
		}
	}
}