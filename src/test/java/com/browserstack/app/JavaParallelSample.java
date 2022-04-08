package com.browserstack.app;

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
class DeviceOne implements Runnable {
	public void run() {
		Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
		capsHashtable.put("device", "iPhone 12 Pro");
		capsHashtable.put("real_mobile", "true");
		capsHashtable.put("build", "BStack-[Java] Sample Build");
		capsHashtable.put("name", "Thread 1");
		JavaParallelSample deviceOne = new JavaParallelSample();
		deviceOne.executeTest(capsHashtable);
	}
}
class DeviceTwo implements Runnable {
	public void run() {
		Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
		capsHashtable.put("device", "Samsung Galaxy S20");
		capsHashtable.put("real_mobile", "true");
		capsHashtable.put("build", "BStack-[Java] Sample Build");
		capsHashtable.put("name", "Thread 2");
		JavaParallelSample deviceTwo = new JavaParallelSample();
		deviceTwo.executeTest(capsHashtable);
	}
}
class DeviceThree implements Runnable {
	public void run() {
		Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
		capsHashtable.put("browser", "safari");
		capsHashtable.put("browser_version", "14");
		capsHashtable.put("os", "OS X");
		capsHashtable.put("os_version", "Big Sur");
		capsHashtable.put("build", "BStack-[Java] Sample Build");
		capsHashtable.put("name", "Thread 3");
		JavaParallelSample deviceThree = new JavaParallelSample();
		deviceThree.executeTest(capsHashtable);
	}
}
class DeviceFour implements Runnable {
	public void run() {
		Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
		capsHashtable.put("browser", "Chrome");
		capsHashtable.put("browser_version", "latest");
		capsHashtable.put("os", "OS X");
		capsHashtable.put("os_version", "Monterey");
		capsHashtable.put("build", "BStack-[Java] Sample Build");
		capsHashtable.put("name", "Thread 4");
		JavaParallelSample deviceFour = new JavaParallelSample();
		deviceFour.executeTest(capsHashtable);
  	}
}
class DeviceFive implements Runnable {
	public void run() {
		Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
		capsHashtable.put("browser", "Chrome");
		capsHashtable.put("browser_version", "latest");
		capsHashtable.put("os", "Windows");
		capsHashtable.put("os_version", "10");
		capsHashtable.put("build", "BStack-[Java] Sample Build");
		capsHashtable.put("name", "Thread 5");
		JavaParallelSample deviceFive = new JavaParallelSample();
		deviceFive.executeTest(capsHashtable);
  	}
}
public class JavaParallelSample {
	public static final String USERNAME = "";
	public static final String AUTOMATE_KEY = "";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
	public static void main(String[] args) throws Exception {
		Thread threadOne = new Thread(new DeviceOne());
		threadOne.start();
		Thread threadTwo = new Thread(new DeviceTwo());
		threadTwo.start();
		Thread threadThree = new Thread(new DeviceThree());
		threadThree.start();
		Thread threadFour = new Thread(new DeviceFour());
		threadFour.start();
		Thread threadFive = new Thread(new DeviceFive());
		threadFive.start();
		}
	public void executeTest(Hashtable<String, String> capsHashtable) {
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

