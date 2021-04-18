package com.stepdefination;

import com.base.DriverManager;
import com.base.TestConfiguration;
import com.base.TestContext;
import cucumber.api.Scenario;
import cucumber.api.java.After;

public class Hooks {
    private DriverManager driverManager;
    private TestConfiguration testConfiguration;

    public Hooks(TestContext testContext) {
        driverManager=(DriverManager)testContext;
        testConfiguration=(TestConfiguration)testContext;
    }

    @After
    public void tearDown(Scenario scenario) throws Exception{
        try {
            driverManager.getScreenshot(scenario,testConfiguration.getUdid());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            driverManager.quitDriver();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(System.getProperty("os.name").contains("Windows")){
            Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
        }
    }
}
