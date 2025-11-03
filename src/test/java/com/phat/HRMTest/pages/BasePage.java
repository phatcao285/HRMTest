package com.phat.HRMTest.pages;

import com.phat.keywords.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class BasePage {
    private WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    // sidebar scrollable
    @FindAll({
            @FindBy(css = "div.navbar-content.ps--active-y"),
            @FindBy(css = ".pc-sidebar .navbar-content"),
            @FindBy(css = "nav.pc-sidebar .navbar-content")
    })
    private WebElement sidebarContainer;

    // Projects
    @FindBy(xpath = "//li/a[contains(@href,'projects')]")
    private WebElement menuProject;

    // Client(s) — bao cả href & label
    @FindAll({
            @FindBy(xpath = "//li/a[contains(@href,'/client')]"),
            @FindBy(xpath = "//li/a[.//span[normalize-space()='Client' or normalize-space()='Clients']]")
    })
    private WebElement menuClient;

    // Tasks
    @FindBy(xpath = "//li/a[contains(@href,'tasks')][.//span[normalize-space()='Tasks']]")
    private WebElement menuTask;

    @FindBy(xpath = "//div[normalize-space()='Logout']/a")
    private WebElement buttonLogout;

    @FindBy(xpath = "//li/a[contains(@href,'desk')]")
    private WebElement menuHome;

    // -------- Actions ----------
    public void clickMenuProject() {
        WebUI.waitForElementVisible(menuProject);
        WebUI.scrollInContainerToElement(sidebarContainer, menuProject);
        WebUI.clickElement(menuProject);
    }

    // BasePage.java
    public void clickMenuClient() {
        // Chờ có element + cuộn ĐÚNG sidebar như trước
        WebUI.waitForElementPresent(menuClient);
        WebUI.scrollInContainerToElement(sidebarContainer, menuClient);
        WebUI.waitForElementClickable(menuClient);
        WebUI.clickElement(menuClient);

        // ✅ Đợi điều hướng thật sự sang trang client
        WebUI.waitForUrlContains("/clients-list");
    }



    public void clickMenuTask() {
        WebUI.waitForElementVisible(menuTask);
        WebUI.scrollInContainerToElement(sidebarContainer, menuTask);
        WebUI.clickElement(menuTask);
    }

    public void clickLogout() {
        WebUI.waitForElementVisible(buttonLogout);
        WebUI.clickElement(buttonLogout);
    }

    public void verifyUserNavigateToHome() {
        WebUI.verifyDisplay(menuHome, WebUI.isElementDisplayed(menuHome), "Menu Home not display");
        String actualUrl = WebUI.getCurrentURL();
        WebUI.verifyEqual(actualUrl, "https://hrm.anhtester.com/erp/desk", "User not navigate to Home");
    }
}

