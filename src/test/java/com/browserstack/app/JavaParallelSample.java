package com.browserstack.app;

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
class DeviceOne implements Runnable {
	public void run() {
		Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
		capsHashtable.put("deviceName", "iPhone 12 Pro");
		capsHashtable.put("realMobile", "true");
		JavaParallelSample deviceOne = new JavaParallelSample();
		deviceOne.executeTest(capsHashtable, "Thread 1");
	}
}
class DeviceTwo implements Runnable {
	public void run() {
		Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
		capsHashtable.put("deviceName", "Samsung Galaxy S20");
		capsHashtable.put("realMobile", "true");
		JavaParallelSample deviceTwo = new JavaParallelSample();
		deviceTwo.executeTest(capsHashtable, "Thread 2");
	}
}
class DeviceThree implements Runnable {
	public void run() {
		Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
		capsHashtable.put("os", "OS X");
		JavaParallelSample deviceThree = new JavaParallelSample();
		deviceThree.executeTest(capsHashtable, "Thread 3");
	}
}
class DeviceFour implements Runnable {
	public void run() {
		Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
		capsHashtable.put("os", "OS X");
		JavaParallelSample deviceFour = new JavaParallelSample();
		deviceFour.executeTest(capsHashtable, "Thread 4");
  	}
}
class DeviceFive implements Runnable {
	public void run() {
		Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
		capsHashtable.put("os", "Windows");
		JavaParallelSample deviceFive = new JavaParallelSample();
		deviceFive.executeTest(capsHashtable, "thread 5");
  	}
}
public class JavaParallelSample {
	public static final String USERNAME = "rutvikchandla_2MEern";
	public static final String AUTOMATE_KEY = "AXHzyg34Qr81Nep231pu";
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
	public void executeTest(Hashtable<String, String> capsHashtable, String sessionName) {
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

