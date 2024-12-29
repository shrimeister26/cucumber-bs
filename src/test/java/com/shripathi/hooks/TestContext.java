package com.shripathi.hooks;

import com.shripathi.driver.DriverManager;
import com.shripathi.driver.TargetFactory;
import com.shripathi.projects.website.elpais.pages.Dashboard.DashboardPageElpais;
import com.shripathi.projects.website.elpais.pages.Opinion.OpinionPageElpais;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;

import java.util.concurrent.TimeUnit;

public class TestContext {

    private WebDriver driver;

    public TestContext() {
        driver = ThreadGuard.protect(new TargetFactory().createInstance());
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        DriverManager.setDriver(driver);
    }

    private DashboardPageElpais dashboardPageElpais;
    private OpinionPageElpais opinionPageElpais;

    public DashboardPageElpais getDashboardPageElpais() {
        if (dashboardPageElpais == null) {
            dashboardPageElpais = new DashboardPageElpais();
        }
        return dashboardPageElpais;
    }

    public OpinionPageElpais getOpinionPageElpais() {
        if (opinionPageElpais == null) {
            opinionPageElpais = new OpinionPageElpais();
        }
        return opinionPageElpais;
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

}
