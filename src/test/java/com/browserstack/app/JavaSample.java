package com.browserstack.app;

//Sample test in Java to run Automate session.

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.JavascriptExecutor;

import java.net.URL;
import java.util.HashMap;
import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JavaSample {
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
        browserstackOptions.put("local", "false");
        browserstackOptions.put("seleniumVersion", "4.0.0");
        browserstackOptions.put("sessionName", "BStack-[Java] Sample Test"); // test name
        browserstackOptions.put("buildName", "BStack Local Build Number 1"); // CI/CD job or build name
        capabilities.setCapability("bstack:options", browserstackOptions);
        final WebDriver driver = new RemoteWebDriver(new URL(URL), capabilities);
        try {
            driver.get("https://bstackdemo.com/");
            final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.titleIs("StackDemo"));
            // getting name of the product
            String product_name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'1\']/p"))).getText();
            // waiting for the Add to cart button to be clickable
            WebElement cart_btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\'1\']/div[4]")));
            // clicking the 'Add to cart' button
            cart_btn.click();
            // checking if the Cart pane is visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("float-cart__content")));
            // getting the product's name added in the cart
            final String product_in_cart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'__next\']/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]"))).getText();
            // verifying whether the product added to cart is available in the cart
            if (product_name.equals(product_in_cart)) {
                markTestStatus("passed", "Product has been successfully added to the cart!", driver);
            }
        } catch (Exception e) {
            markTestStatus("failed", "Some elements failed to load", driver);
        }
        driver.quit();
    }

    // This method accepts the status, reason and WebDriver instance and marks the test on BrowserStack
    public static void markTestStatus(String status, String reason, WebDriver driver) {
        final JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"" + status + "\", \"reason\": \"" + reason + "\"}}");
    }
}
