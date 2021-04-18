package com.base;

import com.pages.LoginPage;
import org.openqa.selenium.WebDriver;

public class PageObjectManager {

    public WebDriver driver;
    private TestConfiguration testConfiguration;
    private TestContext testContext;

    public PageObjectManager(TestContext testContext){
        this.testContext=testContext;
    }

    private LoginPage loginPage;

    public LoginPage getLoginPage(){
        return (loginPage==null)?loginPage=new LoginPage(testContext):loginPage;
    }
}
