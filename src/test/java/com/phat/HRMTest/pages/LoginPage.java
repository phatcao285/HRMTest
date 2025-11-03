package com.phat.HRMTest.pages;

import com.phat.helpers.PropertiesHelper;
import com.phat.keywords.WebUI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class LoginPage {

    private WebDriver driver;

    // ========== Constructor ==========
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        // Dùng AjaxElementLocatorFactory để lazy-load + timeout 10s
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    // ========== Elements ==========
    @FindBy(xpath = "//input[@id='iusername']")
    private WebElement inputUsername;

    @FindBy(xpath = "//input[@id='ipassword']")
    private WebElement inputPassword;

    @FindBy(xpath = "//button[normalize-space()='Login']")
    private WebElement buttonLogin;

    // ===== Alert messages =====
    @FindBy(xpath = "//div[@class='toast toast-error']")
    private WebElement alertErrorInputRequiredField;

    @FindBy(xpath = "//h2[@id='swal2-title']")
    private WebElement alertLoginSuccess;

    // ========== Actions ==========
    public void loginHRM(String username, String password) {
        WebUI.openURL(PropertiesHelper.getValue("URL"));

        WebUI.waitForElementVisible(inputUsername);
        WebUI.clearTextWithKey(inputUsername);
        WebUI.setText(inputUsername, username);

        WebUI.waitForElementVisible(inputPassword);
        WebUI.clearTextWithKey(inputPassword);
        WebUI.setText(inputPassword, password);

        WebUI.waitForElementClickable(buttonLogin);
        WebUI.clickElement(buttonLogin);
    }

    // ========== Verification ==========
    public void verifyShowAlertLoginSuccess() {
        WebUI.waitForElementVisible(alertLoginSuccess);
        String actualText = WebUI.getElementText(alertLoginSuccess).trim();
        WebUI.verifyEqual(actualText, "Logged In Successfully.",
                actualText + " not match with expected");
    }

    public void verifyShowAlertErrorInputUsernameRequired() {
        WebUI.waitForElementVisible(alertErrorInputRequiredField);
        String actualText = WebUI.getElementText(alertErrorInputRequiredField).trim();

        // Sửa lại text bị lỗi "tThe" -> "The"
        WebUI.verifyEqual(actualText, "The username field is required.",
                actualText + " not match with expected");
    }

    public void verifyShowAlertErrorInputPasswordRequired() {
        WebUI.waitForElementVisible(alertErrorInputRequiredField);
        String actualText = WebUI.getElementText(alertErrorInputRequiredField).trim();

        WebUI.verifyEqual(actualText, "The password field is required.",
                actualText + " not match with expected");
    }
}
