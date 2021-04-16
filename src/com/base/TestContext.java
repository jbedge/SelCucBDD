package com.base;


import org.apache.log4j.Logger;

public class TestContext {
    private driverManager webDriverManager;
    private PageObjectManager pageObjectManager;
    private TestConfiguration testConfiguration;
    public ScenarioContext scenarioContext;
    private Logger logger;

    public TestContext() throws Exception {
        testConfiguration = new TestConfiguration();
        webDriverManager = new driverManager(testConfiguration);
        pageObjectManager = new PageObjectManager(webDriverManager.createDriver(), testConfiguration);
        scenarioContext = new ScenarioContext();
        logger=Logger.getLogger(RunnerTest.class.getName());
    }

    public TestConfiguration  getTestConfiguration() {
        return testConfiguration;
    }
    public driverManager getWebDriverManager() {
        return webDriverManager;
    }
    public PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }
    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }
    public Logger getLogger() {
        return logger;
    }

}