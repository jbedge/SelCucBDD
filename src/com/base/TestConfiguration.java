package com.base;

import cucumber.api.Scenario;
import org.apache.log4j.Logger;
import org.testng.Reporter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class TestConfiguration {
    private String configName = "resources/config/config.yaml";
    Logger log = Logger.getLogger(TestConfiguration.class.getName());
    private Map config = null;
    private String browser;
    private String env;
    private String appiumPort;
    private String version;
    private String udid;
    private String deviceName;
    private String app = null;
    private Boolean sp;
    private Scenario scenario;
    private int systemPort;
    private int chromeDriverPort;
    public static final int WAIT_1_SECOND = 1000;
    public static final int WAIT_2_SECOND = 2000;
    public static final int WAIT_HALF_SECOND = 500;
    public static final int WAIT_3_SECOND = 3000;
    public static final int WAIT_5_SECOND = 5000;
    public static final int WAIT_10_SECOND = 10000;

    public TestConfiguration() {
        setBrowser(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser"));
        setEnv(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env"));
        setAppiumPort(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("port"));
        setUdid(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("udid"));
        setVersion(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("version"));
        setDeviceName(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("deviceName"));
        setSP(true);
        setSystemPort(Integer.parseInt(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("systemPort")));
        setChromeDriverPort(Integer.parseInt(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("chromedriverPort")));
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowser() {
        return browser;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getAppiumPort() {
        return appiumPort;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAppiumPort(String appiumPort) {
        this.appiumPort = appiumPort;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Map getConfig(String appUrl, String environment) throws Exception {
        Map config = null;
        log.info("Inside getConfig");
            InputStream input = new FileInputStream(configName);
            log.info("file loaded");
            Yaml yaml = new Yaml();
            Map<String, Object> data1 = (Map<String, Object>) yaml.load(input);
            System.out.println(data1);
            config = (Map) (data1).get("Origin");
            input.close();
            return config;
    }

    public void setAppUrl(String application, TestConfiguration configuration) throws Exception {

        if(config==null) {
            config = getConfig(application.toLowerCase(), env.toLowerCase());
            app = application.toLowerCase();
        }
    }

    public String getApplicationURl()
    {
        return config.get("url").toString();
    }

    public  String getAdminUrl() {
        return config.get("adminurl").toString();
    }

    public String getInstoreUrl()
    {
        return config.get("instoreurl").toString();
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public Boolean getSP() {
        return sp;
    }

    public void setSP(Boolean sp) {
        this.sp = sp;
    }

    public int getSystemPort() {
        return systemPort;
    }

    public void setSystemPort(int systemPort) {
        this.systemPort = systemPort;
    }

    public int getChromeDriverPort() {
        return chromeDriverPort;
    }

    public void setChromeDriverPort(int chromeDriverPort) {
        this.chromeDriverPort = chromeDriverPort;
    }

    public String getcurrentURL(){
        return config.get("url").toString();
    }

//    public void setAppUrl(String application, TestConfiguration configuration) throws Exception {
//
//        if(config==null) {
//            System.out.println("------?"+application.toLowerCase()+env.toLowerCase());
//            config = getConfig(application.toLowerCase(), env.toLowerCase());
//            app = application.toLowerCase();
//            DatabaseSqlServer databaseSqlServer = new DatabaseSqlServer(getjCareDBURL());
//            boolean isXiChanged = false;
//            try {
//                System.out.println(configuration.getEnv());
//                DatabaseQueriesJcare dbq = new DatabaseQueriesJcare(configuration);
//                databaseSqlServer.executeSelectQuery(dbq.GET_XI_VALUES_OF_SKUS);
//                isXiChanged = true;
//            } catch (Exception e){
//                if(e.getMessage().equalsIgnoreCase("No data found in the database")) {
//                    log.error("No xi values changed");
//                } else {
//                    throw e;
//                }
//            }
//            if(isXiChanged) {
//                CachedRowSet result = databaseSqlServer.getResultSet();
//                StringBuffer SKU = new StringBuffer();
//                while(result.next()){
//                    SKU.append(",").append("'").append(result.getString("SKU")).append("'");
//                }
//                String SKU1 = String.valueOf(SKU).replaceFirst(",","");
//                DatabaseQueriesJcare dbq = new DatabaseQueriesJcare(configuration);
//                databaseSqlServer.executeSelectQuery(dbq.getModelNameOfSku.replace("REPLACE_SKU_HERE",SKU1));
//                CachedRowSet result1 = databaseSqlServer.getResultSet();
//                while (result1.next()){
//                    String modelName = result1.getString("model");
//                    databaseSqlServer.executeUpdateQuery(dbq.updateXiValueOfModel.replace("REPLACE_MODEL_HERE",modelName));
//                }
//            }
//            boolean isSkuAddedInProtectionList = false;
//            try {
//                DatabaseQueriesJcare dbq = new DatabaseQueriesJcare(configuration);
//                databaseSqlServer.executeSelectQuery(dbq.GET_ITEMID_ADDED_IN_PROTECTION_SKU_LIST);
//                isSkuAddedInProtectionList=true;
//            } catch (Exception e){
//                if(e.getMessage().equalsIgnoreCase("No data found in the database")) {
//                    log.error("No SKU added into Protection list");
//                } else {
//                    throw e;
//                }
//            }
//            if(isSkuAddedInProtectionList) {
//                CachedRowSet result = databaseSqlServer.getResultSet();
//                StringBuffer SKU = new StringBuffer();
//                while(result.next()){
//                    SKU.append(",").append("'").append(result.getString("ItemId")).append("'");
//                }
//                String SKU1 = String.valueOf(SKU).replaceFirst(",","");
//                DatabaseQueriesJcare dbq = new DatabaseQueriesJcare(configuration);
//                databaseSqlServer.executeUpdateQuery(dbq.deleteItemIdFromProtectionSkuList.replace("REPLACE_SKU_HERE",SKU1));
//            }
//
//            boolean isSkuAddedInHighEndList = false;
//            try {
//                DatabaseQueriesJcare dbq = new DatabaseQueriesJcare(configuration);
//                databaseSqlServer.executeSelectQuery(dbq.GET_ITEMID_ADDED_IN_HIGHEND_SKU_LIST);
//                isSkuAddedInHighEndList=true;
//            } catch (Exception e){
//                if(e.getMessage().equalsIgnoreCase("No data found in the database")) {
//                    log.error("No SKU added in high end list");
//                } else {
//                    throw e;
//                }
//            }
//            if(isSkuAddedInHighEndList) {
//                CachedRowSet result = databaseSqlServer.getResultSet();
//                StringBuffer SKU = new StringBuffer();
//                while(result.next()){
//                    SKU.append(",").append("'").append(result.getString("ItemId")).append("'");
//                }
//                String SKU1 = String.valueOf(SKU).replaceFirst(",","");
//                DatabaseQueriesJcare dbq = new DatabaseQueriesJcare(configuration);
//                databaseSqlServer.executeUpdateQuery(dbq.deleteItemIdFromHighEndSkuList.replace("REPLACE_SKU_HERE",SKU1));
//            }
            //           CommonObjectsSteps commonObjectsSteps = new CommonObjectsSteps();
//            String[] HoldNames = {"SpecificAreaCodes","SpecificStores","HighEndModel","AntiLineContractLength","TooYoung"};
//            for(String holdName:HoldNames) {
//                commonObjectsSteps.iMakeHoldInRulesTable(holdName,"false");
//            }
//        }
//    }
}
