package com.anhtester.projects.website.crm.stepdefinitions;

import com.anhtester.common.CommonPageCRM;
import com.anhtester.keywords.WebUI;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;

import static com.anhtester.keywords.WebUI.getCurrentUrl;
import static com.anhtester.keywords.WebUI.verifyContains;

public class DashboardSteps extends CommonPageCRM {

    @Given("User logged in with email {string} and password {string}")
    public void userLoggedInWithEmailAndPassword(String email, String password) {
        WebUI.openWebsite("https://app.hrsale.com/");
        WebUI.setText(By.xpath("//input[@id='iusername']"), email);
        WebUI.setText(By.xpath("//input[@id='ipassword']"), password);
        WebUI.clickElement(By.xpath("//button[@type='submit']"));
    }

    @Given("User navigate to dashboard")
    public void userNavigateToDashboard() {
        verifyContains(getCurrentUrl(), getDashboardPage().pageUrl, "The url of sign in page not match.");

    }

    @When("User click {string}")
    public void userClick(String menu) {
        WebUI.waitForPageLoaded();
        WebUI.sleep(2);
        WebUI.clickElement(By.xpath("//span[contains(text(),'" + menu + "')]"));
    }

    @Then("The user redirect to this page {string}")
    public void the_user_redirect_to_this_page(String menu) {
        WebUI.waitForPageLoaded();
        WebUI.verifyElementTextContains(By.xpath("//div[@id='smartwizard-2']//ul//li[contains(@class, 'nav-item active')]/a"), menu);
    }

}
