package com.phat.HRMTest.testcases;

import com.phat.HRMTest.pages.BasePage;
import com.phat.HRMTest.pages.LoginPage;
import com.phat.HRMTest.pages.TaskPage;
import com.phat.common.BaseTest;
import com.phat.dataprovider.DataProviderFactory;
import com.phat.driver.DriverManager;
import com.phat.helpers.PropertiesHelper;
import io.qameta.allure.*;
import org.testng.annotations.Test;

public class TaskTest extends BaseTest {

    @Owner("Thanh Phat")
    @Epic("Task Management")
    @Feature("Add new task")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test add new task function")
    @Test(dataProvider = "data_addNewTask", dataProviderClass = DataProviderFactory.class)
    public void testAddNewTask(String titleTask, String startDate, String endDate, String project, String summary) {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        BasePage basePage = new BasePage(DriverManager.getDriver());
        TaskPage taskPage = new TaskPage(DriverManager.getDriver());

        loginPage.loginHRM(PropertiesHelper.getValue("ADMIN_USERNAME"), PropertiesHelper.getValue("ADMIN_PASSWORD"));
        basePage.clickMenuTask();
        taskPage.addNewTask(titleTask, startDate, endDate, project, summary);
        taskPage.verifyAddTaskSuccess(titleTask, startDate, endDate, project, summary);
    }

    @Owner("Thanh Phat")
    @Epic("Task Management")
    @Feature("Delete task")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test delete task function")
    @Test(dataProvider = "data_deleteTask", dataProviderClass = DataProviderFactory.class)
    public void testDeleteTask(String titleTask, String startDate, String endDate, String project, String summary) {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        BasePage basePage = new BasePage(DriverManager.getDriver());
        TaskPage taskPage = new TaskPage(DriverManager.getDriver());

        loginPage.loginHRM(PropertiesHelper.getValue("ADMIN_USERNAME"), PropertiesHelper.getValue("ADMIN_PASSWORD"));
        basePage.clickMenuTask();
        taskPage.addNewTask(titleTask, startDate, endDate, project, summary);
        taskPage.deleteTask(titleTask);
        taskPage.verifyTaskNotDisplayedAfterDelete(titleTask);
    }
}