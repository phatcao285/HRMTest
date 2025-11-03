package com.phat.HRMTest.testcases;

import com.phat.HRMTest.pages.BasePage;
import com.phat.HRMTest.pages.ClientPage;
import com.phat.HRMTest.pages.LoginPage;
import com.phat.common.BaseTest;
import com.phat.dataprovider.DataProviderFactory;
import com.phat.helpers.PropertiesHelper;
import io.qameta.allure.*;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

public class ClientTest extends BaseTest {

    @Owner("Thanh Phat")
    @Epic("Client Management")
    @Feature("Add new client")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test add new client function")
    @Test(dataProvider = "data_AddNewClient", dataProviderClass = DataProviderFactory.class)
    public void testAddNewClient(String firstName, String lastName, String password, String contactNumber,
                                 String gender, String email, String username, String filepath) {
        LoginPage loginPage   = new LoginPage(driver);
        BasePage basePage     = new BasePage(driver);
        ClientPage clientPage = new ClientPage(driver);

        loginPage.loginHRM(PropertiesHelper.getValue("ADMIN_USERNAME"), PropertiesHelper.getValue("ADMIN_PASSWORD"));
        basePage.clickMenuClient();
        clientPage.addNewClient(firstName, lastName, password, contactNumber, gender, email, username, filepath);
        clientPage.verifyAddNewClientSuccess(username);
        clientPage.verifyDetailClientAfterAddNew(firstName, lastName, contactNumber, gender, email, username);
    }

    @Owner("Thanh Phat")
    @Epic("Client Management")
    @Feature("Edit client")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test edit client function")
    @Test(dataProvider = "data_editClient", dataProviderClass = DataProviderFactory.class)
    public void testEditClient(String firstName, String lastName, String password, String contactNumber,
                               String gender, String email, String username, String filepath, String country) {
        LoginPage loginPage   = new LoginPage(driver);
        BasePage basePage     = new BasePage(driver);
        ClientPage clientPage = new ClientPage(driver);

        loginPage.loginHRM(PropertiesHelper.getValue("ADMIN_USERNAME"), PropertiesHelper.getValue("ADMIN_PASSWORD"));
        basePage.clickMenuClient();
        clientPage.addNewClient(firstName, lastName, password, contactNumber, gender, email, username, filepath);
        clientPage.editClient(username, country);
        clientPage.verifyDetailClientAfterUpdate(country);

    }

    @Owner("Thanh Phat")
    @Epic("Client Management")
    @Feature("Delete client")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test delete client function")
    @Test(dataProvider = "data_DeleteClient", dataProviderClass = DataProviderFactory.class)
    public void testDeleteClient(String firstName, String lastName, String password, String contactNumber,
                                 String gender, String email, String username, String filepath) {
        LoginPage loginPage   = new LoginPage(driver);
        BasePage basePage     = new BasePage(driver);
        ClientPage clientPage = new ClientPage(driver);

        loginPage.loginHRM(PropertiesHelper.getValue("ADMIN_USERNAME"), PropertiesHelper.getValue("ADMIN_PASSWORD"));
        basePage.clickMenuClient();
        clientPage.addNewClient(firstName, lastName, password, contactNumber, gender, email, username, filepath);
        clientPage.deleteClient(username);
        clientPage.verifyClientNotDisplayedAfterDelete(username);
    }
}
