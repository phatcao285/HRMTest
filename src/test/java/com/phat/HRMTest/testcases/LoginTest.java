package com.phat.HRMTest.testcases;

import com.phat.HRMTest.pages.BasePage;
import com.phat.HRMTest.pages.LoginPage;
import com.phat.common.BaseTest;
import com.phat.dataprovider.DataProviderFactory;
import com.phat.driver.DriverManager;
import io.qameta.allure.*;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    @Owner("Thanh Phat")
    @Epic("Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test login success with valid registered account")
    @Test(dataProvider = "data_LoginSuccess_registered_account", dataProviderClass = DataProviderFactory.class)
    public void testLoginSuccess(String username, String password) {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        BasePage basePage   = new BasePage(DriverManager.getDriver());
        loginPage.loginHRM(username, password);
        loginPage.verifyShowAlertLoginSuccess();
        basePage.verifyUserNavigateToHome();
    }

    @Owner("Thanh Phat")
    @Epic("Login")
    @Severity(SeverityLevel.MINOR)
    @Description("Test login fail with invalid username")
    @Test(dataProvider = "data_LoginFail_without_username", dataProviderClass = DataProviderFactory.class)
    public void testLoginFailureWithInvalidUsername(String username, String password) {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.loginHRM(username, password);
        loginPage.verifyShowAlertErrorInputUsernameRequired();
    }

    @Owner("Thanh Phat")
    @Epic("Login")
    @Severity(SeverityLevel.MINOR)
    @Description("Test login fail with invalid password")
    @Test(dataProvider = "data_LoginFail_without_password", dataProviderClass = DataProviderFactory.class)
    public void testLoginFailureWithInvalidPassword(String username, String password) {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.loginHRM(username, password);
        loginPage.verifyShowAlertErrorInputPasswordRequired();
    }
}
