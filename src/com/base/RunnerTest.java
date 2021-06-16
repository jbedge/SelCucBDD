package com.base;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//@CucumberOptions(
//        features = "resources/features"
//)
    public class RunnerTest extends AbstractTestNGCucumberTests {
        private String dateString;
        Logger log = Logger.getLogger(RunnerTest.class.getName());
        private String executeTags;



    private String plugin;
    private String glue;
    private String monochrome;
    private String strict;
    private String dryRun;

//        private String glue = "com.stepdefination,com.pages";
        private String testName;

        @Test
        public void startExecution(ITestContext tContext) throws Exception {
            testName = tContext.getCurrentXmlTest().getName();
            executeTags = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("tags");
            plugin= Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("plugin");
            glue= Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("glue");
            monochrome= Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("monochrome");
            strict= Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("strict");
            dryRun= Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("dryRun");
            defaultRun(testName);
        }




        public File[] finder(String dirName) {
            File dir = new File(dirName);
            return dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".json");
                }
            });
        }

        private void defaultRun(String testName) throws Exception {
            String workingLoc = null;
            if (testName.equalsIgnoreCase("Test1")) {
                workingLoc = System.getProperty("user.dir") + "/target";
            } else if (testName.equalsIgnoreCase("Test2")) {
                workingLoc = System.getProperty("user.dir") + "/target2";
            }
            File workingDir = new File(workingLoc);
            File[] allDir = workingDir.listFiles();
            for (File dir : allDir) {
                if (dir.getName().startsWith("rerun") && dir.getName().endsWith(".txt")) {
                    FileDeleteStrategy.FORCE.delete(dir);
                }

                if (dir.getName().startsWith("cucumber") && dir.getName().endsWith(".json")) {
                    FileDeleteStrategy.FORCE.delete(dir);
                }
            }
            List<String> arguments = new ArrayList<String>();
            arguments.add("resources/features");
            String[] tags = {executeTags};
            for (String tag : tags) {
                arguments.add("--tags");
                arguments.add(tag);
            }
            arguments.add("--plugin");
            if (testName.equalsIgnoreCase("test1")) {
                arguments.add("json:target/cucumberDefault.json");
            } else {
                arguments.add("json:target2/cucumberDefault.json");
            }
            arguments.add("--plugin");
            if (testName.equalsIgnoreCase("test1")) {
                arguments.add("rerun:target/rerun.txt");
            } else {
                arguments.add("rerun:target2/rerun.txt");
            }

            String[] gluepackages = glue.split(",");
            for (String packages : gluepackages) {
                if (!packages.contains("none")) {
                    arguments.add("--glue");
                    arguments.add(packages);
                }
            }

            arguments.add(dryRun);
            arguments.add(monochrome);
            arguments.add(strict);
            System.out.println("Cucumber options : "+arguments);
            final String[] argv = arguments.toArray(new String[0]);
            try {
                ExecuteTests(argv);
                GenerateAllRunReports("DefaultRun", testName);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public byte ExecuteTests(final String[] argv) throws InterruptedException, IOException {
            RuntimeOptions runtimeOptions = new RuntimeOptions(new ArrayList(Arrays.asList(argv)));//from this call it recognizes cucumber class
            MultiLoader resourceLoader = new MultiLoader(this.getClass().getClassLoader());
            ResourceLoaderClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, this.getClass().getClassLoader());
            Runtime runtime = new Runtime(resourceLoader, classFinder, this.getClass().getClassLoader(), runtimeOptions);
            runtime.run();
            System.out.println(runtime.exitStatus());
            return runtime.exitStatus();
        }



        public void ExecuteReRerun(String refactoredFeatureTextFile, String targetJson, String targetRerun, String testName) throws IOException {
            List<String> arguments = new ArrayList<String>();
            arguments.add(refactoredFeatureTextFile);
            arguments.add("--plugin");
            if (testName.equalsIgnoreCase("test1")) {
                arguments.add("pretty:target/cucumber-pretty.txt");
            } else if (testName.equalsIgnoreCase("Test2")) {
                arguments.add("pretty:target2/cucumber-pretty.txt");
            }
            arguments.add("--plugin");
            arguments.add("json:" + targetJson);
            arguments.add("--plugin");
            arguments.add("rerun:" + targetRerun);
            String[] gluepackages = "com.stepDefinition".split(",");
            for (String packages : gluepackages) {
                if (!packages.contains("none")) {
                    arguments.add("--glue");
                    arguments.add(packages);
                }
            }
            final String[] argv = arguments.toArray(new String[0]);
            executetests(argv);
        }

        public void refactorReruntxt(String sourceFile, String destinationFile) {
            File file = null;
            try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    try {
                        String[] failedFeatures = sCurrentLine.split("\\s+");
                        List<String> refactorFailedFeatured = new ArrayList<String>();
                        for (String s : failedFeatures) {
                            if (s.contains("resources")) {
                                refactorFailedFeatured.add("" + s);
                            } else {
                                refactorFailedFeatured.add("resources/features/" + s);
                                System.out.println(refactorFailedFeatured);
                            }
                        }
                        StringBuilder sb = new StringBuilder();
                        for (String s : refactorFailedFeatured) {
                            sb.append(s).append(" ");
                        }
                        file = new File(destinationFile);

                        // if file doesnt exists, then create it
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(sb.toString());
                        bw.close();
                        System.out.println("rerun.txt refactored successfully");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void GenerateAllRunReports(String runName, String testName) {
            try {
                File[] jsons = null;
                if (testName.equalsIgnoreCase("Test1")) {
                    jsons = finder("target/");
                } else if (testName.equalsIgnoreCase("Test2")) {
                    jsons = finder("target2/");
                }

                if (runName.equalsIgnoreCase("DefaultRun")) {
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_H_m_s_a");
                    dateString = simpleDateFormat.format(date);
                    List<File> defaultRunJSONs = new ArrayList<File>();
                    for (File f : jsons) {
                        if (f.getName().contains("cucumberDefault") && f.getName().endsWith(".json")) {
                            defaultRunJSONs.add(f);
                        }
                    }
                    if (!defaultRunJSONs.isEmpty()) {
                        generateRunWiseReport(defaultRunJSONs, "Default_Run", testName);
                    }
                } else if (runName.equalsIgnoreCase("FirstRun")) {
                    List<File> firstRunJSONs = new ArrayList<File>();
                    for (File f : jsons) {
                        if (f.getName().contains("cucumber1") && f.getName().endsWith(".json")) {
                            firstRunJSONs.add(f);
                        }
                    }
                    if (firstRunJSONs.size() != 0) {
                        generateRunWiseReport(firstRunJSONs, "First_Re-Run", testName);
                    }
                } else if (runName.equalsIgnoreCase("SecondRun")) {
                    List<File> secondRunJSONs = new ArrayList<File>();
                    for (File f : jsons) {
                        if (f.getName().contains("cucumber2") && f.getName().endsWith(".json")) {
                            secondRunJSONs.add(f);
                        }
                    }
                    if (secondRunJSONs.size() != 0) {
                        generateRunWiseReport(secondRunJSONs, "Second_Re-Run", testName);
                    }
                } else if (runName.equalsIgnoreCase("ThirdRun")) {
                    List<File> thirdRunJSONs = new ArrayList<File>();
                    for (File f : jsons) {
                        if (f.getName().contains("cucumber3") && f.getName().endsWith(".json")) {
                            thirdRunJSONs.add(f);
                        }
                    }
                    if (thirdRunJSONs.size() != 0) {
                        generateRunWiseReport(thirdRunJSONs, "Third_Re-Run", testName);
                    }
                } else if (runName.equalsIgnoreCase("FourthRun")) {
                    List<File> thirdRunJSONs = new ArrayList<File>();
                    for (File f : jsons) {
                        if (f.getName().contains("cucumber4") && f.getName().endsWith(".json")) {
                            thirdRunJSONs.add(f);
                        }
                    }
                    if (thirdRunJSONs.size() != 0) {
                        generateRunWiseReport(thirdRunJSONs, "Fourth_Re-Run", testName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void generateRunWiseReport(List<File> jsons, String run, String testName) {
            String projectDir = System.getProperty("user.dir");
            String reportsDir = null;
            if (projectDir != null) {
                String[] ss = projectDir.split("\\\\");
                if (ss.length != 0) {
                    String[] stimestamp = ss[ss.length - 1].split("_");
                    reportsDir = stimestamp[stimestamp.length - 1];
                }
            }
            try {
                //Adding tag name to the Reports folder name in case there is a single tag.
                String tags = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("tags");
                String folderName = ".Results/Reports_";
                if (tags.split(",").length == 1) {
                    folderName = ".Results/Reports_" + tags.replace("@", "") + "_";
                }
                //=======================================================================
                File rd = new File(folderName + dateString + "/Result_" + reportsDir);
                List<String> jsonReports = new ArrayList<String>();
                for (File json : jsons) {
                    jsonReports.add(json.getAbsolutePath());
                }
                Configuration configuration = new Configuration(rd, Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("applicationName"));
                configuration.addClassifications("Browser", Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser"));
                configuration.addClassifications("Version", Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("version"));
                configuration.addClassifications("Device Name",Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("deviceName") );
                configuration.addClassifications("Environment", Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("env"));
                //List<String> jsonReports, File reportDirectory, String pluginUrlPath, String buildNumber, String buildProject, boolean skippedFails, boolean undefinedFails, boolean flashCharts, boolean runWithJenkins, boolean artifactsEnabled, String artifactConfig, boolean highCharts
                ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
                reportBuilder.generateReports();
                System.out.println(run + " consolidated reports are generated under directory " + reportsDir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public byte executetests(final String[] argv) throws IOException {
            RuntimeOptions runtimeOptions = new RuntimeOptions(new ArrayList(Arrays.asList(argv)));
            MultiLoader resourceLoader = new MultiLoader(RunnerTest.class.getClassLoader());
            ResourceLoaderClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, RunnerTest.class.getClassLoader());
            Runtime runtime = new Runtime(resourceLoader, classFinder, RunnerTest.class.getClassLoader(), runtimeOptions);
            runtime.run();
            System.out.println(runtime.exitStatus());
            return runtime.exitStatus();
        }


        public void firstRun(String testName) throws Exception {
            if (testName.equalsIgnoreCase("Test1")) {
                refactorReruntxt("target/rerun.txt", "target/rerun1.txt");
            } else if (testName.equalsIgnoreCase("Test2")) {
                refactorReruntxt("target2/rerun.txt", "target2/rerun1.txt");
            }
            if (testName.equalsIgnoreCase("Test1")) {
                if (new File("target/rerun1.txt").exists() && new BufferedReader(new FileReader("target/rerun1.txt")).readLine() != null) {
                    log.info("First run execution started....");
                    // ExecuteReRerun first arg: refactored input file ; second arg:- output json file path for getting result
                    if (testName.equalsIgnoreCase("Test1")) {
                        ExecuteReRerun("@target/rerun1.txt", "target/cucumber1.json", "target/rerun2.txt", testName);
                    }
                    // generateReport("target/cucumber/", "target/cucumber1.json", "first-run-feature-overview.html");
                    GenerateAllRunReports("FirstRun", testName);
                    log.info("First run execution completed and report is created....");
                }
            } else if (testName.equalsIgnoreCase("Test2")) {
                if (new File("target2/rerun1.txt").exists() && new BufferedReader(new FileReader("target2/rerun1.txt")).readLine() != null) {
                    log.info("First run execution started....");
                    // ExecuteReRerun first arg: refactored input file ; second arg:- output json file path for getting result
                    if (testName.equalsIgnoreCase("Test2")) {
                        ExecuteReRerun("@target2/rerun1.txt", "target2/cucumber1.json", "target2/rerun2.txt", testName);
                    }
                    // generateReport("target/cucumber/", "target/cucumber1.json", "first-run-feature-overview.html");
                    GenerateAllRunReports("FirstRun", testName);
                    log.info("First run execution completed and report is created....");
                }
            }

        }

        public void secondRun(String testName) throws Exception {
            if (testName.equalsIgnoreCase("Test1")) {
                if (new File("target/rerun2.txt").exists() && new BufferedReader(new FileReader("target/rerun2.txt")).readLine() != null) {
                    // ExecuteReRerun first arg: refactored input file ; second arg:- output json file path for getting result
                    log.info("Second run execution started....");
                    ExecuteReRerun("@target/rerun2.txt", "target/cucumber2.json", "target/rerun3.txt", testName);
                    // generateReport("target/cucumber/", "target/cucumber2.json", "second-run-feature-overview.html");
                    GenerateAllRunReports("SecondRun", testName);
                    log.info("Second run execution completed and report is created....");
                }
            } else if (testName.equalsIgnoreCase("Test2")) {
                if (new File("target2/rerun2.txt").exists() && new BufferedReader(new FileReader("target2/rerun2.txt")).readLine() != null) {
                    // ExecuteReRerun first arg: refactored input file ; second arg:- output json file path for getting result
                    log.info("Second run execution started....");
                    ExecuteReRerun("@target2/rerun2.txt", "target2/cucumber2.json", "target2/rerun3.txt", testName);
                    // generateReport("target/cucumber/", "target/cucumber2.json", "second-run-feature-overview.html");
                    GenerateAllRunReports("SecondRun", testName);
                    log.info("Second run execution completed and report is created....");
                }
            }
        }

    public void retryCount(int retryCount) throws Exception {
        System.setProperty("retryCount", "0");
        int count = Integer.valueOf(System.getProperty("retryCount"));
        switch (retryCount) {
            case 1:
                firstRun(testName);
                break;
            case 2:
                firstRun(testName);
                secondRun(testName);
                break;
            default:
                firstRun(testName);
                break;
        }
    }
    }
