package com.pages;

import com.base.ActionMethods;
import com.base.DriverManager;
import com.base.TestConfiguration;
import com.base.TestContext;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;
    private DriverManager driverManager;
    private TestConfiguration testConfiguration;
    private Logger logger;
    private ActionMethods actionMethods;

    public LoginPage(TestContext testContext) {
        driverManager=(DriverManager)testContext;
        testConfiguration=(TestConfiguration)testContext;
        driver=driverManager.getDriver();
        logger=testContext.getLogger();
        actionMethods=testContext.getActionMethods();
    }

    private By inpUserName=By.xpath("//*[@id=\"email\"]");
    private By inpPassword=By.xpath("//*[@id=\"pass\"]");
    private By btnLogin=By.xpath("//button[.='Log In']");

    public void setUserName(String val) throws Exception{
        logger.info("started:setusername method started");
        actionMethods.waitForVisibilityOfElement(inpUserName);
        actionMethods.enterValue(inpUserName,val);
        logger.info("completed:setusername method started");
    }

    public void setPassword(String val) throws Exception{
        logger.info("started:setPassword method started");
        actionMethods.waitForVisibilityOfElement(inpPassword);
        actionMethods.enterValue(inpPassword,val);
        logger.info("completed:setPassword method started");
    }

    public void clickOnLoginButton() throws Exception{
        logger.info("started:clickLoginButton method started");
        actionMethods.waitForVisibilityOfElement(btnLogin);
        actionMethods.click(btnLogin);
        logger.info("completed:clickLoginButton method started");
    }

    public void getURL() throws Exception{
        logger.info("Started:getURL");
        driver.get(testConfiguration.getcurrentURL());
        logger.info("Completed:getURL");
    }

    public void setUrl() throws Exception{
        testConfiguration.setAppUrl("Origin",testConfiguration);
    }
    public void waitforPageToLoad() throws Exception{
        testConfiguration.setAppUrl("Origin",testConfiguration);
    }

}
