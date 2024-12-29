package com.shripathi.projects.website.elpais.pages.Dashboard;

import com.shripathi.constants.FrameworkConstants;
import com.shripathi.driver.DriverManager;
import com.shripathi.keywords.WebUI;
import com.shripathi.projects.website.elpais.pages.Opinion.OpinionPageElpais;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPageElpais {

    public DashboardPageElpais() {
    }
    public String pageUrl = "/";

    public By menuOpinion = By.xpath("//*[@id='csw']/div[1]/nav/div/a[2]");

    public OpinionPageElpais openOpinionPageElpais() {
        new WebDriverWait(DriverManager.getDriver(),Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='didomi-notice-agree-button']")));
        WebUI.clickElement(By.xpath("//*[@id='didomi-notice-agree-button']"),10);
        //*[@id="btn_open_hamburger"]
        WebUI.clickElement(menuOpinion);
        return new OpinionPageElpais();
    }
}
