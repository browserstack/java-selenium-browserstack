# Java-selenium-browserstack
---

## Prerequisite
Make sure `maven` is installed in your system. See if it is properly installed.

```
mvn --version
```

## Steps to run local test (using Java SDK)

1. Clone and navigate to the repo.
   ```
   git clone -b sdk https://github.com/browserstack/java-selenium-browserstack.git
   cd java-selenium-browserstack
   ```

2. Set your [BrowserStack Username and Access Key](https://www.browserstack.com/accounts/settings) in [browserstack.yml](browserstack.yml)

3. Add `browserstack-java-sdk` in your `pom.xml` as below.
    ```
    <dependency>
        <groupId>com.browserstack</groupId>
        <artifactId>browserstack-java-sdk</artifactId>
        <version>LATEST</version>
        <scope>compile</scope>
    </dependency>
   ```

## Build and run test using maven.

### Install Dependencies using maven.
```
mvn install
```

### Run tests using maven.

a. To run single test session.
```
mvn exec:exec -Dexec.executable="java" -Dexec.classpathScope=test -Dexec.args="-cp %classpath -javaagent:/path/to/browserstack-java-sdk-jar com.browserstack.app.JavaSample"
```

b. To run local test session.
```
mvn exec:exec -Dexec.executable="java" -Dexec.classpathScope=test -Dexec.args="-cp %classpath -javaagent:/path/to/browserstack-java-sdk-jar com.browserstack.app.JavaLocalSample"
```

### Arguments

For the full list of arguments that can be passed in `browserStackLocalOptions`, refer [BrowserStack Local modifiers](https://www.browserstack.com/docs/local-testing/binary-params). For examples, refer below -

#### Verbose Logging
To enable verbose logging -
```java
browserStackLocalOptions:
  v: true
  logFile: /browserstack/logs.txt
```

#### Force Start
To kill other running Browserstack Local instances -
```java
browserStackLocalOptions:
  force: true
```

#### Only Automate
To disable local testing for Live and Screenshots, and enable only Automate -
```java
browserStackLocalOptions:
  onlyAutomate: true
```

#### Force Local
To route all traffic via local(your) machine -
```java
browserStackLocalOptions:
  forcelocal: true
```

#### Local Identifier
If doing simultaneous multiple local testing connections, set this uniquely for different processes -
```java
browserStackLocalOptions:
  localIdentifier: randomstring
```
