package com.base;

import cucumber.api.Scenario;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class driverManager {
    public WebDriver driver;
    public Boolean serverStarted  = false;
    public String dummyText;
    private String LOCAL_DIRECTORY_SOURCE = System.getProperty("user.dir") + File.separator + "browserDrivers" + File.separator;
    TestConfiguration configuration;

    public driverManager(TestConfiguration testConfiguration) {
        configuration= testConfiguration;
    }


    public WebDriver createDriver() throws Exception {
        String browser = configuration.getBrowser();
        switch (browser.toLowerCase()) {
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "chrome":
//                System.setProperty("webdriver.chrome.driver", LOCAL_DIRECTORY_SOURCE + "chromedriver.exe");
                WebDriverManager.chromedriver().version("89").setup();
                ChromeOptions options1 = new ChromeOptions();
                options1.addArguments("--disable-extensions");
                options1.addArguments("disable-infobars");
                options1.addArguments("--start-maximized");

                Map<String, Object> prefs = new HashMap<String, Object>();
                Map<String, Object> langs = new HashMap<String, Object>();
//                langs.put("ja", "en");// Japanese to english
//                prefs.put("translate", "{'enabled' : true}");
//                prefs.put("translate_whitelists", langs);
//                options1.setExperimentalOption("prefs", prefs);
                options1.setExperimentalOption("debuggerAddress","localhost:9222");
//                chrome.exe -remote-debugging-port=9222 -user-data-dir=C:\ChromeData :command to run chrome in debugmode
                options1.addArguments();
                driver = new ChromeDriver(options1);
                driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
                driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);
                break;
            case "ie":
                System.setProperty("webdriver.ie.driver", LOCAL_DIRECTORY_SOURCE + "IEDriverServer.exe");
                DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
                ieCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
                ieCapabilities.setCapability("nativeEvents", false);
                try {
                    driver = new InternetExplorerDriver(ieCapabilities);
                } catch (Exception e) {
                    System.out.print("Error:" + e.getMessage());
                }
                break;
            case "ios":
                DesiredCapabilities capabilities = new DesiredCapabilities();
//                AppiumServer appiumServer = new AppiumServer();
//                if(!serverStarted) {
//                    appiumServer.startServer("127.0.0.1", Integer.parseInt(CapabilitiesReader.getConfigValue("port")));
//                    serverStarted =true;
//                }
                capabilities.setCapability("platformName", "");
                capabilities.setCapability("platformVersion", "version");
                capabilities.setCapability("browserName", dummyText.equalsIgnoreCase("iOS") ? "Safari" : "Chrome");
                capabilities.setCapability("deviceName", "deviceName");
                capabilities.setCapability("language", "jp");
                capabilities.setCapability("udid", "udid");
                capabilities.setCapability("AutomationName", Integer.parseInt("version") >= 10 ? "XCUITest" : "UIAutomation");
                if (Integer.parseInt("version") >= 10) {
                    capabilities.setCapability("xcodeOrgId", "5WLA5C7LL5");
                    capabilities.setCapability("xcodeSigningId", "iOS Development");
                    capabilities.setCapability("wdaLocalPort", "8723");
                }
                try {
                    URL serverUrl = new URL("http://" + "127.0.0.1" + ":" + "port" + "/wd/hub");
                    driver = new IOSDriver<IOSElement>(serverUrl, capabilities);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "android":
                if(!serverStarted){
                    // System.out.println("port value is :" +configuration.getAppiumPort());
//                    AppiumServer server = new AppiumServer();
//                    if(!server.checkIfServerIsRunnning(Integer.parseInt(configuration.getAppiumPort()))) {
//                        server.startServer("127.0.0.1", Integer.parseInt(configuration.getAppiumPort()));
//                    }
//                    server.checkIfServerIsRunnning(Integer.parseInt(configuration.getAppiumPort()));
                    serverStarted = true;
                }
                DesiredCapabilities acapabilities = new DesiredCapabilities();
                acapabilities.setCapability("deviceName", configuration.getDeviceName());

                acapabilities.setCapability("udid", configuration.getUdid());
                acapabilities.setCapability("systemPort",configuration.getSystemPort());
                acapabilities.setCapability("platformVersion", configuration.getVersion());
                acapabilities.setCapability("platformName", configuration.getBrowser());
                acapabilities.setCapability("chromedriverPort",configuration.getChromeDriverPort());
                acapabilities.setCapability("browserName", Integer.parseInt( configuration.getVersion()) >= 7 ? "Chrome" : "Browser");
                // acapabilities.setCapability(CapabilityType.BROWSER_NAME,"Browser");
                acapabilities.setCapability("automationName", Integer.parseInt( configuration.getVersion()) >= 7 ? "UIAutomator2" : "UIAutomator1");
                if (Integer.parseInt( configuration.getVersion()) >= 7) {
                    acapabilities.setCapability("chromedriverExecutableDir", LOCAL_DIRECTORY_SOURCE);
                    ChromeOptions options = new ChromeOptions();
                    options.setExperimentalOption("w3c", false);
                    //options.addExtensions(new File(LOCAL_DIRECTORY_SOURCE + "extension.crx"));
                    //  options.addArguments("--no-sandbox");
                    options.addArguments("--ignore-certificate-errors");
                    //  options.addArguments("--disable-features=VizDisplayCompositor");
                    acapabilities.setCapability(ChromeOptions.CAPABILITY, options);
                } else {
                    acapabilities.setCapability("chromedriverExecutableDir", LOCAL_DIRECTORY_SOURCE);
                    acapabilities.setCapability("autoGrantPermissions", true);
                    acapabilities.setCapability("acceptSslCerts", true);
                    acapabilities.setCapability("systemPort", "4731");
                    acapabilities.setCapability("unicodeKeyboard", true);
                    acapabilities.setCapability("resetKeyboard", true);
                    acapabilities.setCapability("nativeWebScreenshot", true);
                    acapabilities.setCapability("androidScreenshotPath", "target/screenshots");
//                    ChromeOptions options = new ChromeOptions();
//                    options.setExperimentalOption("androidPackage","com.android.browser");
//                    acapabilities.setCapability(ChromeOptions.CAPABILITY, options);
                }
                try {
                    URL serverUrl = new URL("http://" + "127.0.0.1" + ":" + configuration.getAppiumPort() + "/wd/hub");
                    driver = new AndroidDriver(serverUrl, acapabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
        }
//        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
//        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//        driver.manage().timeouts().setScriptTimeout(40, TimeUnit.SECONDS);
//        if(browser.equalsIgnoreCase("chrome")||browser.equalsIgnoreCase("ie") ||browser.equalsIgnoreCase("firefox"))
//            driver.manage().window().maximize();
        return driver;
    }

    public void quitDriver() {
        driver.quit();
    }

    public void getScreenshot(Scenario scenario, String udid) throws Exception {
        if (scenario.isFailed()) {
            System.out.println("getScreenshot");
            try {
                    byte[] exception = (byte[]) ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    scenario.embed(exception, "image/jpeg");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}