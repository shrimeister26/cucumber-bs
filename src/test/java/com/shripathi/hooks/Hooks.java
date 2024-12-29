package com.shripathi.hooks;

import com.shripathi.constants.FrameworkConstants;
import com.shripathi.driver.DriverManager;
import com.shripathi.driver.ScenarioManager;
import com.shripathi.helpers.CaptureHelpers;
import com.shripathi.helpers.FileHelpers;
import com.shripathi.helpers.PropertiesHelpers;
import com.shripathi.helpers.SystemHelpers;
import com.shripathi.keywords.WebUI;
import com.shripathi.report.AllureManager;
import com.shripathi.mail.EmailManager;
import com.shripathi.utils.LogUtils;
import com.shripathi.utils.ReportUtils;
import com.shripathi.utils.ZipUtils;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static com.shripathi.constants.FrameworkConstants.*;
import static com.shripathi.keywords.WebUI.sleep;

public class Hooks {

    TestContext testContext;

    public Hooks(TestContext context) {
        testContext = context;
    }

    @BeforeAll
    public static void before_all() {
        LogUtils.info("================ BEFORE ALL ================");
        PropertiesHelpers.loadAllFiles();
        AllureManager.setAllureEnvironmentInformation();

        try {
            if (DELETE_TEMP_FOLDER.equals(YES)) {
                FileUtils.deleteDirectory(new File("target/allure-results"));
                LogUtils.info("Deleted directory target/allure-results");
                FileUtils.deleteDirectory(new File("ExportData"));
                LogUtils.info("Deleted directory ExportData");
            }
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void after_all() {
        LogUtils.info("================ AFTER ALL ================");
        ZipUtils.zipReportFolder();
        ReportUtils.openReports(SystemHelpers.getCurrentDir() + PropertiesHelpers.getValue("extent.reporter.spark.out"));
        //FileHelpers.copyFile("src/test/resources/config/allure/environment.xml", "target/allure-results/environment.xml");
        FileHelpers.copyFile("src/test/resources/config/allure/categories.json", "target/allure-results/categories.json");
        FileHelpers.copyFile("src/test/resources/config/allure/executor.json", "target/allure-results/executor.json");

        ZipUtils.zipFolder(SystemHelpers.getCurrentDir() + "target" + File.separator + "allure-results", "allure-results");

        sleep(2);

        EmailManager.sendEmail(count_totalTCs
                , count_passedTCs
                , count_failedTCs
                , count_skippedTCs);

        LogUtils.info("count_totalTCs: " + count_totalTCs);
        LogUtils.info("count_passedTCs: " + count_passedTCs);
        LogUtils.info("count_failedTCs: " + count_failedTCs);
        LogUtils.info("count_skippedTCs: " + count_skippedTCs);
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        LogUtils.info("Running Scenario Name: " + scenario.getName());
        count_totalTCs = count_totalTCs + 1;

        String browserName = (System.getProperty("browser") != null && !System.getProperty("browser").isEmpty()) ? System.getProperty("browser")
                : FrameworkConstants.BROWSER;
        Allure.step("\uD83E\uDD16 Run test on " + browserName.toUpperCase() + " browser.");

        ScenarioManager.setScenario(scenario);

        if (VIDEO_RECORD.toLowerCase().trim().equals(YES)) {
            CaptureHelpers.startRecord(scenario.getName());
        }
    }

    @After
    public void afterScenario(Scenario scenario) {

        if (Status.PASSED.equals(scenario.getStatus())) {
            count_passedTCs = count_passedTCs + 1;
        }
        if (Status.FAILED.equals(scenario.getStatus())) {
            count_failedTCs = count_failedTCs + 1;
        }
        if (Status.SKIPPED.equals(scenario.getStatus())) {
            count_skippedTCs = count_skippedTCs + 1;
        }

        if (VIDEO_RECORD.toLowerCase().trim().equals(YES)) {
            sleep(1);
            CaptureHelpers.stopRecord();
        }

        //Quit driver in thread local
        DriverManager.quit();
        WebUI.stopSoftAssertAll();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.getStatus().equals(Status.PASSED) && SCREENSHOT_PASSED_STEPS.equals(YES)) {
            WebUI.waitForPageLoaded();
            CaptureHelpers.takeScreenshotScenario(scenario,"Screenshot passed step");
        }
        if (scenario.getStatus().equals(Status.FAILED) && SCREENSHOT_FAILED_STEPS.equals(YES)) {
            WebUI.waitForPageLoaded();
            CaptureHelpers.takeScreenshotScenario(scenario,"Screenshot failed step");
        }
        if (SCREENSHOT_ALL_STEPS.equals(YES)) {
            WebUI.waitForPageLoaded();
            CaptureHelpers.takeScreenshotScenario(scenario,"Screenshot step");
        }
    }

}
