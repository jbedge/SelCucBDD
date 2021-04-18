package com.stepdefination;

import com.base.DriverManager;
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
        testConfiguration=(TestConfiguration)testContext;
        loginPage=testContext.getPageObjectManager().getLoginPage();
    }

    @Given("^I set facebook url$")
    public void i_am_on_facebook_url() throws Throwable {
        loginPage.setUrl();
    }

    @Given("^I navigate to facebook url$")
    public void i_navigate_to_facebook_url() throws Throwable {
        loginPage.getURL();
    }

    @Given("^I wait for page to load$")
    public void i_wait_for_page_to_load() throws Throwable {
        loginPage.waitforPageToLoad();
    }



    @Given("^I enter user name \"([^\"]*)\"$")
    public void i_enter_user_name(String args) throws Throwable {
        loginPage.setUserName(args);
    }

    @Given("^I enter password \"([^\"]*)\"$")
    public void i_enter_user_password(String arg) throws Throwable {
        loginPage.setPassword(arg);
    }

    @Given("^I click on login button$")
    public void i_click_on_login_button() throws Throwable {
        loginPage.clickOnLoginButton();
    }
}