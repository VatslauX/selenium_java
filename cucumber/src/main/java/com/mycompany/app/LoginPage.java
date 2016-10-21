package com.mycompany.app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	private static WebElement element = null;
	private static WebDriverWait wait;

	public static WebElement account_Btn(WebDriver driver) {
		element = driver.findElement(By.cssSelector("div#account"));
		return element;
	}

	public static WebElement login_Btn(WebDriver driver) {
		element = driver.findElement(By.id("login"));

		return element;
	}

	public static WebElement log_Input(WebDriver driver) {
		wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("log")));
		element = driver.findElement(By.id("log"));
		return element;
	}

	public static WebElement pwd_Input(WebDriver driver) {
		element = driver.findElement(By.id("pwd"));
		return element;
	}

	public static WebElement wrong_Response_Message(WebDriver driver) {
		wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("response")));
		element = driver.findElement(By.className("response"));
		return element;
	}
  
  public static boolean MessageShown(WebDriver driver, String message){
		wait = new WebDriverWait(driver, 30);
  		wait.until(ExpectedConditions.textToBePresentInElement(
				wrong_Response_Message(driver), message));    
        return true;
  }

	public static String getLoginErrorMsg(WebDriver driver) {
		System.out.println("----------------"
				+ wrong_Response_Message(driver).getText());
		return wrong_Response_Message(driver).getText();
	}

	public static WebElement account_Login_Remind_Msg(WebDriver driver) {
		element = driver.findElement(By.className("myaccount"));
		return element;
	}
}
