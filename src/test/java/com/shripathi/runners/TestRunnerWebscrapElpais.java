package com.shripathi.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/WebscrapElpais.feature",
        glue = {
                "com.shripathi.projects.website.elpais.stepdefinitions",
                "com.shripathi.hooks"
        },
        plugin = {
                "com.shripathi.hooks.CucumberListener",
                "pretty",
                "html:target/cucumber-reports/TestRunnerWebscrapElpais.html",
                "json:target/cucumber-reports/TestRunnerWebscrapElpais.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        tags = "@regression"
)

public class TestRunnerWebscrapElpais extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
