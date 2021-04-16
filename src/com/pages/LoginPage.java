package com.pages;

import com.base.ActionMethods;
import com.base.TestConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends ActionMethods {

    private WebDriver driver;
    private TestConfiguration testConfiguration;

    public LoginPage(WebDriver driver, TestConfiguration configuration) {
        super(driver, configuration);
        this.driver=driver;
        this.testConfiguration=configuration;
    }

    private By inpUserName=By.xpath("//*[@id=\"email\"]");
    private By inpPassword=By.xpath("//*[@id=\"pass\"]");
    private By btnLogin=By.xpath("//button[.='Log In1']");

    public void setUserName(String val) throws Exception{
        logger.info("started:setusername method started");
        waitForVisibilityOfElement(inpUserName);
        enterValue(inpUserName,val);
        logger.info("completed:setusername method started");
    }

    public void setPassword(String val) throws Exception{
        logger.info("started:setPassword method started");
        waitForVisibilityOfElement(inpPassword);
        enterValue(inpPassword,val);
        logger.info("completed:setPassword method started");
    }

    public void clickOnLoginButton() throws Exception{
        logger.info("started:clickLoginButton method started");
        waitForVisibilityOfElement(btnLogin);
        click(btnLogin);
        logger.info("completed:clickLoginButton method started");
    }

    public void getURL() throws Exception{
        logger.info("Started:getURL");
        testConfiguration.setAppUrl("Origin",testConfiguration);
        driver.get(testConfiguration.getcurrentURL());
        waitForPageLoad();
        logger.info("Completed:getURL");
    }
}
