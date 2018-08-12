package com.github.abhishek8908.integrationtest;

import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.File;

import com.github.abhishek8908.util.DriverUtil;

public class DriverDownloadIntegrationTest {

	private static String osName = null;
	private static String tmpDir = (getOSName().equals("windows")) ? "c:\\temp"
			: "/tmp";

	// TODO: inter-test dependency
	@Test(enabled = false)
	public void downloadChomeDriverTest()
			throws IOException, ConfigurationException {
		System.setProperty("ver", "2.39");
		System.setProperty("os", "win32");
		System.setProperty("ext", "zip");
		DriverUtil.download("chromedriver", tmpDir, "2.39");
		assertTrue(
				(new File(tmpDir + File.separator + "chromedriver-2.39-win32.exe"))
						.exists());
	}

	@Test(enabled = false)
	public void downloadEdgeeDriverTest()
			throws IOException, ConfigurationException {
		System.setProperty("ver", "c");
		System.setProperty("os", "win32");
		System.setProperty("ext", "zip");
		DriverUtil.download("edgedriver", tmpDir, "2.39");
		assertTrue((new File(
				tmpDir + File.separator + "MicrosoftWebDriver-2.39-win32.exe"))
						.exists());
	}

	@Test(enabled = false)
	public void downloadGeckoDriverTest()
			throws IOException, ConfigurationException {
		System.setProperty("ver", "0.21.0");
		System.setProperty("os", "linux64");
		System.setProperty("ext", "tar.gz");
		DriverUtil.download("geckodriver", tmpDir, "0.21.0");
		assertTrue(
				(new File(tmpDir + File.separator + "geckodriver-0.21.0-linux64"))
						.exists());
	}

	public static String getOSName() {
		if (osName == null) {
			osName = System.getProperty("os.name").toLowerCase();
			if (osName.startsWith("windows")) {
				osName = "windows";
			}
		}
		return osName;
	}
}