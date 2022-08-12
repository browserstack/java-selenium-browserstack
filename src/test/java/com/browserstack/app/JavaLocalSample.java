package com.browserstack.app;

//Sample test in Java to run Automate session.
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.JavascriptExecutor;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JavaLocalSample {
    public static final String AUTOMATE_USERNAME = System.getenv("BROWSERSTACK_USERNAME") != null ? System.getenv("BROWSERSTACK_USERNAME") : "BROWSERSTACK_USERNAME";
    public static final String AUTOMATE_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY") != null ? System.getenv("BROWSERSTACK_ACCESS_KEY") : "BROWSERSTACK_ACCESS_KEY";
    public static final String URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    public static void main(String[] args) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "latest");
        HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
        browserstackOptions.put("os", "OS X");
        browserstackOptions.put("osVersion", "Sierra");
        browserstackOptions.put("seleniumVersion", "4.0.0");
        browserstackOptions.put("sessionName", "BStack-[Java] Sample Test"); // test name
        browserstackOptions.put("buildName", "BStack Local Build Number 1"); // CI/CD job or build name
        capabilities.setCapability("bstack:options", browserstackOptions);

        final WebDriver driver = new RemoteWebDriver(new URL(URL), capabilities);
        try {
            driver.get("http://bs-local.com:45691/check");
            final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // getting name of the product
            String bodyText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body"))).getText();

            if (bodyText.equals("Up and running")) {
                markTestStatus("passed", "Local Test is successful and up and running", driver);
            }
        } catch (Exception e) {
            markTestStatus("failed", "Couldn't connect the local", driver);
        }
        driver.quit();
    }

    // This method accepts the status, reason and WebDriver instance and marks the test on BrowserStack
    public static void markTestStatus(String status, String reason, WebDriver driver) {
        final JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"" + status + "\", \"reason\": \"" + reason + "\"}}");
    }
}
