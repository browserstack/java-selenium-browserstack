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
  browserstackOptions.put("sessionName", "BStack-[Java] Sample Test"); // test name
  browserstackOptions.put("buildName", "BStack Build Number 1"); // CI/CD job or build name
  capabilities.setCapability("bstack:options", browserstackOptions);
```

## Steps to run local test (using Java SDK)
1. Add `browserstack.sdk.version` property in `<properties>` tag.
    ```
   <properties>
        <browserstack.sdk.version>1.0.6</browserstack.sdk.version>
   </properties>
    ```

2. Add `browserstack-java-sdk` in your `pom.xml` as below.
    ```
    <dependency>
        <groupId>com.browserstack</groupId>
        <artifactId>browserstack-java-sdk</artifactId>
        <version>${browserstack.sdk.version}</version>
        <scope>compile</scope>
    </dependency>
   ```

3. Modify `exec-maven-plugin` in the profile by adding `browserstack-java-sdk` as an `<argument>`.
    ```
   <configuration>
        <classpathScope>test</classpathScope>
        <executable>java</executable>
        <arguments>
        <argument>-classpath</argument>
        <classpath/>
        <argument>-javaagent:${settings.localRepository}/com/browserstack/browserstack-java-sdk/${browserstack.sdk.version}/browserstack-java-sdk-${browserstack.sdk.version}.jar</argument>
        <argument>com.browserstack.app.JavaLocalSample</argument>
        </arguments>
    </configuration>
   ```

### Arguments

For the full list of arguments that can be passed in `browserStackLocalOptions`, refer [BrowserStack Local modifiers](https://www.browserstack.com/docs/local-testing/binary-params). For examples, refer below -

#### Verbose Logging
To enable verbose logging -
```java
browserStackLocalOptions:
  - v: true
```

#### Force Start
To kill other running Browserstack Local instances -
```java
browserStackLocalOptions:
  - force: true
```

#### Only Automate
To disable local testing for Live and Screenshots, and enable only Automate -
```java
browserStackLocalOptions:
  - onlyAutomate: true
```

#### Force Local
To route all traffic via local(your) machine -
```java
browserStackLocalOptions:
  - forcelocal: true
```

#### Proxy
To use a proxy for local testing -

* proxyHost: Hostname/IP of proxy, remaining proxy options are ignored if this option is absent
* proxyPort: Port for the proxy, defaults to 3128 when -proxyHost is used
* proxyUser: Username for connecting to proxy (Basic Auth Only)
* proxyPass: Password for USERNAME, will be ignored if USERNAME is empty or not specified

```java
browserStackLocalOptions:
  - proxyHost: 127.0.0.1
  - proxyPort: 8000
  - proxyUser: user
  - proxyPass: password
```

#### Local Identifier
If doing simultaneous multiple local testing connections, set this uniquely for different processes -
```java
browserStackLocalOptions:
  - localIdentifier: randomstring
```

## Build and run test using maven.

### Install Dependencies using maven.
```
mvn install
```

### Run tests using maven.

a. To run single test session.
```
mvn exec:exec -P sample-test 
```

b. To run parallel test session.
```
mvn exec:exec -P sample-parallel-test 
```

c. To run local test session.
```
mvn exec:exec -P sample-local-test 
```
