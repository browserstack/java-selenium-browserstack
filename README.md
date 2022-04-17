# Java-selenium-browserstack
---

## Prerequisite
Make sure `maven` is installed in your system. See if it is properly installed.

```
mvn --version
```

## Steps to run test

In every test file (JavaSample, JavaLocalSample, JavaParallelSample) make sure you set your credentials.
```java
  public static final String AUTOMATE_USERNAME = "BROWSERSTACK_USERNAME";
  public static final String AUTOMATE_ACCESS_KEY = "BROWSERSTACK_ACCESS_KEY";
```

1. Clone and navigate to the repo.

```
  git clone https://github.com/browserstack/java-selenium-browserstack.git
  cd java-selenium-browserstack
```

2. Change capabilities of test.

```java
  HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
  browserstackOptions.put("os", "OS X");
  browserstackOptions.put("osVersion", "Sierra");
  browserstackOptions.put("local", "false");
  browserstackOptions.put("seleniumVersion", "4.0.0");
  capabilities.setCapability("bstack:options", browserstackOptions);
  capabilities.setCapability("sessionName", "BStack-[Java] Sample Test"); // test name
  capabilities.setCapability("buildName", "BStack Build Number 1"); // CI/CD job or build name
```

## Build and run test using maven.

### Install Dependencies using maven.
```
mvn install
```

### Run tests using maven.

a. To run single test session.
```
  mvn -Dexec.mainClass="com.browserstack.app.JavaSample" -Dexec.classpathScope=test test-compile exec:java
```

b. To run parallel test session.
```
  mvn -Dexec.mainClass="com.browserstack.app.JavaParallelSample" -Dexec.classpathScope=test test-compile exec:java
```

c. To run local test session.
```
  mvn -Dexec.mainClass="com.browserstack.app.JavaLocalSample" -Dexec.classpathScope=test test-compile exec:java
```
