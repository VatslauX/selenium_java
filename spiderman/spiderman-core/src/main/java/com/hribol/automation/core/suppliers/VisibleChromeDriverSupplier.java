package com.hribol.automation.core.suppliers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by hvrigazov on 22.04.17.
 */
public class VisibleChromeDriverSupplier implements VisibleWebDriverSupplier {

    @Override
    public WebDriver get(DesiredCapabilities desiredCapabilities) {
        return new ChromeDriver(desiredCapabilities);
    }
}
