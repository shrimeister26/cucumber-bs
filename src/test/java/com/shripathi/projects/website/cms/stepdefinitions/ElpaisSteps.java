package com.shripathi.projects.website.cms.stepdefinitions;

import com.shripathi.hooks.TestContext;
import com.shripathi.keywords.WebUI;
import com.shripathi.projects.website.cms.pages.Dashboard.DashboardPageElpais;
import com.shripathi.projects.website.cms.pages.Opinion.OpinionPageElpais;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ElpaisSteps {

    DashboardPageElpais dashboardPageElpais;
    OpinionPageElpais opinionPageElpais;

    public ElpaisSteps(TestContext testContext) {

        dashboardPageElpais = testContext.getDashboardPageElpais();
        opinionPageElpais = testContext.getOpinionPageElpais();
    }

    @Given("User navigate to Elpais home page {string}")
    public void userNavigateToLoginPageForAdmin(String url) {
        WebUI.openWebsite(url);
    }

    @Then("user navigates to first five topics and translates title to english")
    public void userIsRedirectedToTheDashboardPage() {
        opinionPageElpais.openOpinionPageElpais();
    }

    @When("user launches elpais opinion page")
    public void launchElpais() {
        OpinionPageElpais opinionPageElpais = dashboardPageElpais.openOpinionPageElpais();
    }
}
