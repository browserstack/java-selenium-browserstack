package com.browserstack.suite;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.browserstack.BrowserStackTestNGTest;

public class SuiteTest04 extends BrowserStackTestNGTest {

  @Test
  public void test_04() throws Exception {
      // navigate to bstackdemo
      driver.get("https://www.bstackdemo.com");
      
      // Check the title
      Assert.assertTrue(driver.getTitle().matches("StackDemo"));
      
      // Save the text of the product for later verify
      String productOnScreenText = driver.findElement(By.xpath("//*[@id=\"1\"]/p")).getText();
      // Click on add to cart button
      driver.findElement(By.xpath("//*[@id=\"1\"]/div[4]")).click();
      
      // See if the cart is opened or not
      Assert.assertTrue(driver.findElement(By.cssSelector(".float\\-cart__content")).isDisplayed());
      
      // Check the product inside the cart is same as of the main page
      String productOnCartText = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]")).getText();
      Assert.assertEquals(productOnScreenText, productOnCartText);
  }
}
