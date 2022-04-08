## <b> java-browserstack </b>

A base language repo for working with selenium and browserstack.

---
### <b> Branch: </b> <br/>
Master -> Selenium 3 code snippets<br/>
Selenium-4 -> Selenium 4 code snippets

---
<b>Folder Structure:</b> <br />

```
src/
    - test/java/com/browserstack/app
        - JavaSample.java (For single test)
        - JavaLocal.java (For testing local website / bs-local setup)
        - JavaParallel.java (For testing multiple sessions)
```

## Prerequisite
IDE for java - preferably Eclipse. <br>
Go over browserstack java IDE installation steps. <br>
https://www.browserstack.com/docs/automate/selenium/getting-started/java#prerequisites

## Steps to run test session
1. Install dependecies
```
mvn compile 
```

2. Change configuration of capabilites and use your Browserstack credentials <br>
For running single test session, navigate to JavaSample.java

```java
  // Use your browserstack credentials here
  public static final String AUTOMATE_USERNAME = "";
  public static final String AUTOMATE_ACCESS_KEY = "";
  
  // Change capabilities if you wish
  caps.setCapability("browserName", "iPhone");
  caps.setCapability("device", "iPhone 11");
  caps.setCapability("realMobile", "true");
  caps.setCapability("local", "true");
  caps.setCapability("os_version", "14.0");
  caps.setCapability("name", "BStack-[Java] Sample Test"); // test name
  caps.setCapability("build", "BStack Build Number 1"); // CI/CD job or build name
```

3. Run test session
Use Eclipse's run to start the session<br>
![image](https://user-images.githubusercontent.com/97675949/158067972-61b24364-79b8-44a8-aab3-cb05e1039ecf.png)
