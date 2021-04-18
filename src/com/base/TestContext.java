package com.base;


import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class TestContext extends DriverManager  {
    private DriverManager webDriverManager;
    private PageObjectManager pageObjectManager;
    private TestConfiguration testConfiguration;
    private ActionMethods actionMethods;
    private Logger logger;
    private WebDriver driver;

    public TestContext() throws Exception {
        driver=getDriver();
        pageObjectManager = new PageObjectManager(this);//
//        scenarioContext = new ScenarioContext();
        actionMethods=new ActionMethods(this);
        logger=Logger.getLogger(RunnerTest.class.getName());
    }

    public PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }

    public Logger getLogger() {
        return logger;
    }

    public ActionMethods getActionMethods() {
        return actionMethods;
    }
}