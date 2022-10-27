package com.browserstack.app;

import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


class ParallelTest implements Runnable {
    public static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME") != null ? System.getenv("BROWSERSTACK_USERNAME") : "BROWSERSTACK_USERNAME";
    public static final String AUTOMATE_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY") != null ? System.getenv("BROWSERSTACK_ACCESS_KEY") : "BROWSERSTACK_ACCESS_KEY";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
    Hashtable<String, String> capsHashtable;

    ParallelTest(Hashtable<String, String> cap) {
        capsHashtable = cap;
    }

    @Override
    public void run() {
        String key;
        DesiredCapabilities caps = new DesiredCapabilities();
        // Iterate over the hashtable and set the capabilities
        Set<String> keys = capsHashtable.keySet();
        Iterator<String> keysIterator = keys.iterator();
        while (keysIterator.hasNext()) {
            key = keysIterator.next();
            caps.setCapability(key, capsHashtable.get(key));
        }
        WebDriver driver;
        try {
            driver = new RemoteWebDriver(new URL(URL), caps);
            final JavascriptExecutor jse = (JavascriptExecutor) driver;
            try {
                // Searching for 'BrowserStack' on google.com
                driver.get("https://bstackdemo.com/");
                WebDriverWait wait = new WebDriverWait(driver, 10);
                wait.until(ExpectedConditions.titleIs("StackDemo"));
                // Getting name of the product
                String product_name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'1\']/p"))).getText();
                //checking whether the Add to Cart button is clickable
                WebElement cart_btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\'1\']/div[4]")));
                // clicking the 'Add to cart' button
                cart_btn.click();
                // checking if the Cart pane is visible
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("float-cart__content")));
                // getting the product's name added in the cart
                final String product_in_cart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'__next\']/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]"))).getText();
                // checking if the product added to cart is available in the cart
                if (product_name.equals(product_in_cart)) {
                    jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Product has been added to the cart!\"}}");
                }
            } catch (Exception e) {
                jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Some elements failed to load..\"}}");
            }
            driver.quit();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

public class JavaParallelSample {

    public static void main(String[] args) throws Exception {
        List<Hashtable<String, String>> caps = new ArrayList<Hashtable<String, String>>();

        //device 1
        Hashtable<String, String> cap1 = new Hashtable<String, String>();
        cap1.put("device", "iPhone 12 Pro");
        cap1.put("real_mobile", "true");
        cap1.put("build", "browserstack-build-1");
        cap1.put("name", "BStack parallel java-selenium");
        cap1.put("browserstack.source", "java-selenium:sample-selenium-3:v1.0");
        caps.add(cap1);

        //device 2
        Hashtable<String, String> cap2 = new Hashtable<String, String>();
        cap2.put("device", "Samsung Galaxy S20");
        cap2.put("real_mobile", "true");
        cap2.put("build", "browserstack-build-1");
        cap2.put("name", "BStack parallel java-selenium");
        cap2.put("browserstack.source", "java-selenium:sample-selenium-3:v1.0");
        caps.add(cap2);

        //device 3
        Hashtable<String, String> cap3 = new Hashtable<String, String>();
        cap3.put("browser", "safari");
        cap3.put("browser_version", "14");
        cap3.put("os", "OS X");
        cap3.put("os_version", "Big Sur");
        cap3.put("build", "browserstack-build-1");
        cap3.put("name", "BStack parallel java-selenium");
        cap3.put("browserstack.source", "java-selenium:sample-selenium-3:v1.0");
        caps.add(cap3);

        //device 4
        Hashtable<String, String> cap4 = new Hashtable<String, String>();
        cap4.put("browser", "Chrome");
        cap4.put("browser_version", "latest");
        cap4.put("os", "OS X");
        cap4.put("os_version", "Monterey");
        cap4.put("build", "browserstack-build-1");
        cap4.put("name", "BStack parallel java-selenium");
        cap4.put("browserstack.source", "java-selenium:sample-selenium-3:v1.0");
        caps.add(cap4);

        for (Hashtable<String, String> cap : caps) {
            Thread thread = new Thread(new ParallelTest(cap));
            thread.start();
        }
    }
}

