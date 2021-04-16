package com.stepdefination;

import com.base.TestConfiguration;
import com.base.TestContext;
import com.pages.LoginPage;
import cucumber.api.java.en.Given;
import org.apache.log4j.PropertyConfigurator;

public class LoginPageSteps {

    private LoginPage loginPage;
    private TestContext testContext;
    private TestConfiguration testConfiguration;

    public LoginPageSteps(TestContext testContext){
        PropertyConfigurator.configure(System.getProperty("user.dir")+"\\resources\\log4j.properties");
        this.testContext=testContext;
        testConfiguration=testContext.getTestConfiguration();
        loginPage=testContext.getPageObjectManager().getLoginPage();
    }

    @Given("^I am on facebook page$")
    public void i_am_on_google_page() throws Throwable {
        System.out.println("I am on google page");
        System.out.println(loginPage);
        loginPage.getURL();
    }

    @Given("^I enter user name \"([^\"]*)\"$")
    public void i_search_something(String args) throws Throwable {
        loginPage.setUserName(args);
    }

    @Given("^I enter password \"([^\"]*)\"$")
    public void i_click_on_something(String arg) throws Throwable {
        loginPage.setPassword(arg);
    }

    @Given("^I click on login button$")
    public void i_verify_the_header() throws Throwable {
        loginPage.clickOnLoginButton();
    }
}