package com.browserstack.app;
import java.util.*;  
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Hashtable;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class ParallelTest implements Runnable {
  public static final String USERNAME = "BROWSERSTACK_USERNAME";
	public static final String AUTOMATE_KEY = "BROWSERSTACK_ACCESS_KEY";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
  Hashtable<String, String> capsHashtable;
  String sessionName;

  ParallelTest(Hashtable<String, String> cap, String sessionString){
    capsHashtable = cap;
    sessionName = sessionString;
  }

  public void run() {
    DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("bstack:options", capsHashtable);
		caps.setCapability("sessionName", sessionName); // test name
		caps.setCapability("buildName", "BStack-[Java] Sample buildName"); // CI/CD job or build name
		WebDriver driver;
		try {
			driver = new RemoteWebDriver(new URL(URL), caps);
			final JavascriptExecutor jse = (JavascriptExecutor) driver;
			try {
				// Searching for 'BrowserStack' on google.com
				driver.get("https://bstackdemo.com/");
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
		cap1.put("deviceName", "iPhone 12 Pro");
		cap1.put("realMobile", "true");
    caps.add(cap1);

    //device 2
    Hashtable<String, String> cap2 = new Hashtable<String, String>();
		cap2.put("deviceName", "Samsung Galaxy S20");
		cap2.put("realMobile", "true");
    caps.add(cap2);

    //device 3
    Hashtable<String, String> cap3 = new Hashtable<String, String>();
		cap3.put("os", "OS X");
    caps.add(cap3);

    //device 4
    Hashtable<String, String> cap4 = new Hashtable<String, String>();
		cap4.put("os", "windows");
    caps.add(cap4);

    for (Hashtable<String, String> cap : caps) {
      Thread thread = new Thread(new ParallelTest(cap, "session name"));
      thread.start();
    }
	}
}
