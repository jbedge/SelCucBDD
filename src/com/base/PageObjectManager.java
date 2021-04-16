package com.base;

import com.pages.LoginPage;
import org.openqa.selenium.WebDriver;

public class PageObjectManager {

    public WebDriver driver;
    private TestConfiguration testConfiguration;

    public PageObjectManager(WebDriver driver,TestConfiguration configuration){
        this.driver=driver;
        this.testConfiguration=configuration;
    }

    private LoginPage loginPage;

    public LoginPage getLoginPage(){
        return (loginPage==null)?loginPage=new LoginPage(driver,testConfiguration):loginPage;
    }
}
