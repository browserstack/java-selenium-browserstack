package com.browserstack.app;

//Sample test in Java to run Automate session.
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.JavascriptExecutor;
import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.browserstack.local.Local;

public class JavaLocalSample {
  public static final String AUTOMATE_USERNAME = "BROWSERSTACK_USERNAME";
  public static final String AUTOMATE_ACCESS_KEY = "BROWSERSTACK_ACCESS_KEY";
  public static final String URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
  public static void main(String[] args) throws Exception {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("os", "OS X");
    caps.setCapability("os_version", "Sierra");
    caps.setCapability("browser", "Safari");
    caps.setCapability("browser_version", "10.0");
    caps.setCapability("browserstack.local", "true");
    caps.setCapability("name", "BStack-[Java] Sample Local Test"); // test name
    caps.setCapability("build", "BStack Build Number 1"); // CI/CD job or build name
    
    //Creates an instance of Local
    Local bsLocal = new Local();

    // You can also set an environment variable - "BROWSERSTACK_ACCESS_KEY".
    HashMap<String, String> bsLocalArgs = new HashMap<String, String>();
    bsLocalArgs.put("key", AUTOMATE_ACCESS_KEY);

    // Starts the Local instance with the required arguments
    bsLocal.start(bsLocalArgs);

    // Check if BrowserStack local instance is running
    System.out.println(bsLocal.isRunning());

    final WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
    try {
      driver.get("http://bs-local.com:45691/check");
      final WebDriverWait wait = new WebDriverWait(driver, 10);
      // getting name of the product
      String bodyText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body"))).getText();
      
      if (bodyText.equals("Up and running")) {
        markTestStatus("passed", "Local Test is successful and up and running", driver);
        }
      } catch (Exception e) {
          markTestStatus("failed", "Could'nt connect the local", driver);
      }
      //Stop the Local instance
      bsLocal.stop();
      driver.quit();
  }
  // This method accepts the status, reason and WebDriver instance and marks the test on BrowserStack
  public static void markTestStatus(String status, String reason, WebDriver driver) {
    final JavascriptExecutor jse = (JavascriptExecutor) driver;
    jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""+ status + "\", \"reason\": \"" + reason + "\"}}");
  }
}
