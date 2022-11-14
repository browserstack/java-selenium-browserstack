package com.browserstack;

import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.browserstack.local.Local;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

public class BrowserStackTestNGTest {
    public WebDriver driver;
    private static Local l;
    private static JSONObject config;
    private static Map<String, Object> commonCapabilities;
    private static String username;
    private static String accessKey;

    @BeforeSuite(alwaysRun=true)
    @Parameters(value = { "config" })
    public void beforeSuite(String config_file) throws Exception {
        JSONParser parser = new JSONParser();
        config = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/" + config_file));
        commonCapabilities = (Map<String, Object>) config.get("capabilities");
        HashMap bstackOptionsMap = (HashMap) commonCapabilities.get("bstack:options");


        username = System.getenv("BROWSERSTACK_USERNAME");
        if (username == null) {
            username = (String) config.get("user");
        }

        accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (accessKey == null) {
            accessKey = (String) config.get("key");
        }
		try {
			if ((bstackOptionsMap.get("local") != null &&
					bstackOptionsMap.get("local").toString().equalsIgnoreCase("true") && (l == null))) {
				l = new Local();
				Map<String, String> options = new HashMap<String, String>();
				options.put("key", accessKey);
				l.start(options);
			}
		} catch (Exception e) {
			System.out.println("Error while start local - " + e);
		}
    }

    @BeforeMethod(alwaysRun = true)
    @org.testng.annotations.Parameters(value = { "config", "environment" })
    @SuppressWarnings("unchecked")
    public void setUp(String config_file, String environment) throws Exception {
        JSONParser parser = new JSONParser();
        config = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/" + config_file));
        JSONObject envs = (JSONObject) config.get("environments");

        MutableCapabilities capabilities = new MutableCapabilities();

        Map<String, Object> envCapabilities = (Map<String, Object>) envs.get(environment);
        Iterator it = envCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            capabilities.setCapability(pair.getKey().toString(), pair.getValue());
        }

        commonCapabilities = (Map<String, Object>) config.get("capabilities");
        it = commonCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (capabilities.getCapability(pair.getKey().toString()) == null) {
                capabilities.setCapability(pair.getKey().toString(), pair.getValue());
            } else if (pair.getKey().toString().equalsIgnoreCase("bstack:options")) {
                HashMap bstackOptionsMap = (HashMap) pair.getValue();
               bstackOptionsMap.putAll((HashMap) capabilities.getCapability("bstack:options"));
               capabilities.setCapability(pair.getKey().toString(), bstackOptionsMap);
            }
        }

        if (capabilities.getCapability("bstack:options") != null) {
            HashMap bstackOptionsMap = (HashMap) capabilities.getCapability("bstack:options");
            if ((bstackOptionsMap.get("local") != null &&
                    bstackOptionsMap.get("local").toString().equalsIgnoreCase("true") && (l == null || !l.isRunning()))) {
                l = new Local();
                Map<String, String> options = new HashMap<String, String>();
                options.put("key", accessKey);
                l.start(options);
            }
            bstackOptionsMap.put("source", "java-selenium:sample-selenium-4:v1.0");
        }

        driver = new RemoteWebDriver(
                new URL("http://" + username + ":" + accessKey + "@" + config.get("server") + "/wd/hub"), capabilities);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.quit();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() throws Exception {
        if (l != null) l.stop();
    }
}
