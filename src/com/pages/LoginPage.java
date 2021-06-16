package com.pages;

import com.base.ActionMethods;
import com.base.DriverManager;
import com.base.TestConfiguration;
import com.base.TestContext;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

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

//
    By Business_name= By.xpath("//*[@id='main-content']//h2[@class='topcard__title']");
    By Job_State = By.xpath("//*[@id='main-content']//span[@class='topcard__flavor topcard__flavor--bullet']");
    By Job_City = By.xpath("//*[@id='main-content']//span[@class='topcard__flavor topcard__flavor--bullet']");
    By Job_Title = By.xpath("//*[@id='main-content']//h2[@class='topcard__title']");
    By no_of_Applicants = By.xpath("//figcaption[@class='num-applicants__caption']");
    By Posted_Date = By.xpath("//span[contains(@class,'posted-time-ago')]");

    By Seniority_Level =By.xpath("//h3[.='Seniority level']//following-sibling::span");
    By Job_Function = By.xpath("//h3[.='Job function']//following-sibling::span");
    By Employment_Type = By.xpath("//h3[.='Employment type']//following-sibling::span");
    By Industries = By.xpath("//h3[.='Industries']//following-sibling::span");
    String url = driver.getCurrentUrl();
    String  Date_Time_of_scraping = DateTime.now().toString();
// joblist=driver.find_elements_by_xpath("//span[@class='screen-reader-text']//parent::a")
    By joblist=By.cssSelector("a.result-card__full-card-link");




    public void setUserName(String val) throws Exception{
        logger.info("started:setusername method started");
        List<WebElement> a=driver.findElements(joblist);
        for (WebElement we:a){
            we.click();
            waitforPageToLoad();
        }
        actionMethods.getText(Business_name);
//        actionMethods.waitForVisibilityOfElement(inpUserName);
//        actionMethods.enterValue(inpUserName,val);
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


    public static <T> List<List<T>> zip(List<T>... lists) {
        List<List<T>> zipped = new ArrayList<List<T>>();
        for (List<T> list : lists) {
            for (int i = 0, listSize = list.size(); i < listSize; i++) {
                List<T> list2;
                if (i >= zipped.size())
                    zipped.add(list2 = new ArrayList<T>());
                else
                    list2 = zipped.get(i);
                list2.add(list.get(i));
            }
        }
        return zipped;
    }

}
