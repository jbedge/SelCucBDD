package com.asurion.automation;

import com.asurion.automation.util.database.DataBaseMySql;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TestLogsInsert {

    Logger logger = Logger.getLogger(TestLogsInsert.class.getName());

    private static TestLogsInsert instance;

//  while debug we can uncomment the below method and add classname in testNG.xml to run quickly
//  without waiting for execution to complete

    @Test
    public void temp() throws IOException {
        insertLogs();
    }
    DataBaseMySql dataBaseMySql;
    private TestLogsInsert() {
        createDBLogFile();
        dataBaseMySql = new DataBaseMySql("jdbc:mysql://uap-common-int-mysql57-cluster.cluster-cou5pzlrtiho.ap-northeast-1.rds.amazonaws.com:3306?useSSL=false&useUnicode=yes&characterEncoding=UTF-8;appuser;appuser_pwd#1;");
    }

//   Singleton disign pattern
//   Saves memory because object is not created at each request. Only single instance is reused again and again.
    synchronized public static TestLogsInsert getInstance() {
        if (instance == null) {
            instance = new TestLogsInsert();
        }
        return instance;
    }

    public void insertLogs() throws IOException {

        long startTime = System.currentTimeMillis();

        String testName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName();

        if (testName.equals("Test1")) {
            extractJSONAndInsertIntoDB("./target/cucumberDefault.json");
            String retryCount=Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("retryCount");
            int count = Integer.parseInt(retryCount);
            for (int i = 1; i <= count; i++) {
                extractJSONAndInsertIntoDB("./target/cucumber" + i + ".json");
            }

        } else if (testName.equals("Test2")) {

            extractJSONAndInsertIntoDB("./target2/cucumberDefault.json");
            String retryCount=Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("retryCount");
            int count = Integer.parseInt(retryCount);
            for (int i = 1; i <= count; i++) {
                extractJSONAndInsertIntoDB("./target2/cucumber" + i + ".json");
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        logger.info("Time taken to insert in DB is : " + TimeUnit.MILLISECONDS.toSeconds(totalTime) + " seconds");
        closeFile();
    }

    public  void extractJSONAndInsertIntoDB(String jsonName) throws IOException {
        String dbLogs = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("insertDBLogs");

        /*dbLogs==null  - This is when this parameter is not defiled in suite file at all
          dbLogs == True - Process the logs  |   dbLogs == False - return don't Process the logs
          Files.exists(Paths.get(jsonName)) -> Check if files exists else return.
                                            If tests are passed in default run 1 then there will be no ./target/cucumber1.json ..n file generated
        */

        if (dbLogs== null || !dbLogs.equalsIgnoreCase("true")) {
            return;
        } else if (!(Files.exists(Paths.get(jsonName)))){
            return;
        }
        File source = new File(jsonName);
        copyFile(source);


        List<String> scenarios = new ArrayList();
        List<String> scenarioSteps = new ArrayList();
        FileInputStream fis = null;

        try {

            String releaseVersion = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("releaseVersion");
            String executionGroup = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("executionGroup");
            String browser = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser");


            String environment ="";
            if (!System.getProperty("execution_Environment", "").isEmpty()) {
                environment = System.getProperty("execution_Environment");
            }
            else {
                environment=Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");
            }
            String applicationName="";
            if (!System.getProperty("applicationName", "").isEmpty()) {
                applicationName = System.getProperty("applicationName");
            }
            else {
                applicationName =Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("applicationName");
            }

//            String applicationName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("applicationName");
//            String environment = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env");

            String chromeDriverPort = "0";
            String systemPort = "0";
            String devicePort = "";
            String port = "0";
            String udid = "0";
            String version = "0";
            String clientCode = "0";

            clientCode = applicationName.equalsIgnoreCase("Acyan") ? "KDDI" : "DOCOMO";

            if (browser.equalsIgnoreCase("Android")) {
                chromeDriverPort = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("chromedriverPort");
                systemPort = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("systemPort");
                devicePort = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("deviceName");
                port = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("port");
                udid = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("udid");
                version = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("version");
            }

            String SCENARIO_STATUS;
            fis = new FileInputStream(jsonName);
            String jsonBody = "";
            jsonBody = IOUtils.toString(fis, StandardCharsets.UTF_8);
            JSONArray featureFiles = JsonPath.read(jsonBody, "$[*].name");

            String CreatedBy = System.getProperty("user.name");
            String Scenario_UUID;

            for (int i = 0; i < featureFiles.size(); i++) {

                String FEATURE_NAME = JsonPath.read(jsonBody, "$[" + i + "].name");
                JSONArray scenarioName = JsonPath.read(jsonBody, "$[" + i + "].elements[*].name");
                int scenarioCount = scenarioName.size();
                for (int j = 0; j < scenarioCount; j++) {
                    String Keyword = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].keyword");
                    //skip processing for Background steps
                    if (!Keyword.trim().equalsIgnoreCase("Background")) {
                        Scenario_UUID = UUID.randomUUID().toString().replace("-", "").toUpperCase();
                        String ScenarioName = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].name");

                        JSONArray SCENARIOs_STATUS = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].steps[?(@.result.status=='failed')]");
                        if (SCENARIOs_STATUS.size() > 0) {
                            SCENARIO_STATUS = "failed";
                        } else {
                            SCENARIO_STATUS = "passed";
                        }

                        String Tags = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].tags[0].name");

                        long beforeTime = ((Number) JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].before[0].result.duration")).longValue();
                        beforeTime = TimeUnit.NANOSECONDS.toSeconds(beforeTime);

                        long after = ((Number) JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].after[0].result.duration")).longValue();
                        after = TimeUnit.NANOSECONDS.toSeconds(after);

                        long execution_Time = TimeUnit.NANOSECONDS.toSeconds(after - beforeTime);


                        String scenariosData = "('" + Scenario_UUID + "',\n" + "'" + ScenarioName.replaceAll("'", "\\\\'") + "',\n" + "'" + FEATURE_NAME + "',\n" + "'" + Keyword + "',\n" + "'" + SCENARIO_STATUS + "',\n" + "'" + Tags + "',\n" +
                                "'" + execution_Time + "',\n" + "'0000-00-00 00:00:00',\n" + "'0000-00-00 00:00:00',\n" + "'"+this.getDateAndTime()+"',\n" + "'" + CreatedBy + "',\n" + "'" + releaseVersion + "',\n" + "'" + browser + "',\n" + "'" + applicationName + "',\n" + "'" + environment + "',\n" + "'" + chromeDriverPort + "',\n" + "'" + systemPort + "',\n" + "'" + devicePort + "',\n" +
                                "'" + port + "',\n" + "'" + udid + "',\n" + "'" + version + "',\n" + "'" + clientCode + "',\n" + "'" + executionGroup + "')\n" +
                                "\n";
                        scenarios.add(scenariosData);
                        JSONArray stepsName = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].steps[*].name");
                        JSONArray stepsKeyword = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].steps[*].keyword");
                        JSONArray stepsLocation = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].steps[*].match.location");
                        JSONArray stepsStatus = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].steps[*].result.status");
                        JSONArray errorMessages = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].steps[*].result.error_message");
                        JSONArray stepsOutPut = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].steps[*].embeddings[0].mime_type");
                        JSONArray stepsScreenShot = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].steps[*].embeddings[0].data");
                        JSONArray stepsArguments = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].steps[*].match.arguments[*].val");
                        JSONArray stepsDuration = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].steps[*].result.duration");
                        JSONArray stepsLine = JsonPath.read(jsonBody, "$[" + i + "].elements[" + j + "].steps[*].line");


                        int i1 = stepsName.size() - stepsDuration.size();
                        List<Integer> list = new ArrayList<>(Collections.nCopies(i1, 0));//initialize array with fixed size and values.
                        stepsDuration.addAll(list);//append the array
                        if (stepsOutPut.size() == 0) {
                            stepsOutPut.add("");
                        }
                        if (stepsScreenShot.size() == 0) {
                            stepsScreenShot.add("");
                        }
                        if (stepsArguments.size() == 0) {
                            stepsArguments.add("");
                        }

                        JSONArray stepsArguments1 = new JSONArray();
                        int h = 0;
                        int stepCount = stepsName.size();

                        for (int m = 0; m < stepCount; m++) {
                            if (stepsName.get(m).toString().contains("\"")) {
                                stepsArguments1.add(stepsArguments.get(h));
                                h++;
                            } else {
                                stepsArguments1.add("");
                            }
                        }

                        int steps = stepsName.size();
                        String stepsOutputs = "";
                        String stepsScreenshots = "NA";
                        String errorMessage = "";
                        for (int l = 0; l < steps; l++) {

                            if (stepsStatus.get(l).equals("failed")) {
                                stepsOutputs = (String) stepsOutPut.get(0);
//                                for time being the screenshot is commented due to SQL Packet exception, will take care in future.
//                                stepsScreenshots = (String) stepsScreenShot.get(0);
                                String errmsg=((String) errorMessages.get(0));
                                int size=errmsg.length();
                                errorMessage = errmsg.substring(0,size>1500?1500:size).replaceAll("'", "\\\\'");
                            }
                            String step= ((String) stepsName.get(l)).replaceAll("'", "\\\\'");
                            Thread.sleep(1);
                            String qr2 = "(N'" + Scenario_UUID + "',\n" +
                                    "'" + UUID.randomUUID().toString().replace("-", "").toUpperCase() + "',\n" +
                                    "N'" + step + "',\n" +
                                    "'" + stepsKeyword.get(l) + "',\n" +
                                    "'" + stepsLocation.get(l) + "',\n" +
                                    "'" + TimeUnit.NANOSECONDS.toSeconds(((Number) stepsDuration.get(l)).longValue()) + "',\n" +
                                    "'" + stepsStatus.get(l) + "',\n" +
                                    "'" + stepsArguments1.get(l) + "',\n" +
                                    "'" + stepsOutputs + "',\n" +
                                    "'" + stepsScreenshots + "',\n" +
                                    "'" + stepsLine.get(l) + "',\n" +
                                    "'"+this.getDateAndTime()+"',\n" +
                                    "'" + CreatedBy + "',\n" +
                                    "'" + errorMessage + "'\n" +
                                    ")";
                            scenarioSteps.add(qr2);
                        }
                    }
                }

            }

            String scenariosCol = "INSERT INTO `automation`.`scenario`\n" +
                    "(`SCENARIO_ID`,`SCENARIO_NAME`,`FEATURE_NAME`,`KEYWORD`,`SCENARIO_STATUS`,`TAGS`,`EXECUTION_TIME`,`EXECUTION_START_TIME`,`EXECUTION_END_TIME`,`CREATED_DATE`,`CREATED_BY`,\n" +
                    "`RELEASE_VERSION`,`BROWSER`,`APPLICATION_NAME`,`ENVIRONMENT`,`BROWSER_DRIVER_PORT`,`SYSTEM_PORT`,`DEVICE_NAME`,`PORT`,`UDID`,`VERSION`,`CLIENT_CODE`,`EXECUTION_GROUP`) VALUES";
            String scenariosData = scenarios.toString().replace("[", "").replace("]", "") + ";";
            logger.info("no of scenarios inserted are : "+scenarios.size());
            dataBaseMySql.executeUpdateQuery(scenariosCol + scenariosData);
            writeDBLogFile(scenariosCol + scenariosData);

            String stepsCol = "INSERT INTO `automation`.`step`(`SCENARIO_ID`,`STEP_ID`,`STEPS_NAME`,`STEPS_KEYWORD`,`STEPS_LOCATION`,`STEPS_DURATION`,`STEPS_STATUS`,\n" +
                    "`STEPS_ARGUMENTS`,`STEPS_OUTPUT`,`STEPS_SCREENSHOT`,`STEPS_LINE`,`CREATED_DATE`,`CREATED_BY`,`ERROR_MESSAGE`)\n" +
                    "VALUES";
            String stepsData = scenarioSteps.toString().replace("[", "").replace("]", "") + ";";
            logger.info("no of scenario Steps inserted are : "+scenarioSteps.size());
//            logger.info(stepsCol + stepsData);
            dataBaseMySql.executeUpdateQuery(stepsCol + stepsData);
            writeDBLogFile(stepsCol + stepsData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public String getDateAndTime(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("JST"));
        String dateTime =sdf.format(calendar.getTime());
        return dateTime;
    }

    public void writeDBLogFile(String queries) throws IOException {
        writer.println(String.format(queries));
    }
    PrintWriter writer;
    public void createDBLogFile() {
//        The Files.createDirectories() creates a new directory and parent directories that do not exist. This method does not throw an exception if the directory already exists.
        try {
            Files.createDirectories(Paths.get(RunnerTest.folderName));
            writer = new PrintWriter(RunnerTest.folderName+File.separator+"DBInsertQueries.txt", "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeFile(){
        writer.close();
    }

    public void copyFile(File source) throws IOException {
        Files.createDirectories(Paths.get(RunnerTest.folderName));

        File dest = new File(RunnerTest.folderName+File.separator+source.getName());
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

