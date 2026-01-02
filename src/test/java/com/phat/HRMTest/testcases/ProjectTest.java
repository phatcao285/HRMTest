package com.phat.HRMTest.testcases;

import com.phat.HRMTest.pages.BasePage;
import com.phat.HRMTest.pages.LoginPage;
import com.phat.HRMTest.pages.ProjectPage;
import com.phat.common.BaseTest;
import com.phat.dataprovider.DataProviderFactory;
import com.phat.driver.DriverManager;
import com.phat.helpers.PropertiesHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;

public class ProjectTest extends BaseTest {

    private LoginPage loginPage() {
        return new LoginPage(DriverManager.getDriver());
    }

    private BasePage basePage() {
        return new BasePage(DriverManager.getDriver());
    }

    private ProjectPage projectPage() {
        return new ProjectPage(DriverManager.getDriver());
    }

    private void loginAsAdminAndOpenProjectMenu() {
        loginPage().loginHRM(PropertiesHelper.getValue("ADMIN_USERNAME"), PropertiesHelper.getValue("ADMIN_PASSWORD"));
        basePage().clickMenuProject();
    }

    @Owner("Phat Cao")
    @Epic("Project Management")
    @Feature("Add new project")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test add new project function")
    @Test(dataProvider = "data_AddNewProject", dataProviderClass = DataProviderFactory.class, priority = 2)
    public void testAddNewProject(String title, String client, String startDate, String endDate, String summary) {
        loginAsAdminAndOpenProjectMenu();
        ProjectPage projectPage = projectPage();
        projectPage.addNewProject(title, client, startDate, endDate, summary);
        projectPage.verifyAddProjectSuccess(title, client, startDate, endDate, summary);
    }

    @Owner("Phat Cao")
    @Epic("Project Management")
    @Feature("Edit project")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test edit project function")
    @Test(dataProvider = "data_editProject", dataProviderClass = DataProviderFactory.class, priority = 3)
    public void testEditProject(String title, String client, String startDate, String endDate, String summary, String updateEndDate) {
        loginAsAdminAndOpenProjectMenu();
        ProjectPage projectPage = projectPage();
        projectPage.addNewProject(title, client, startDate, endDate, summary);
        projectPage.editProject(title, updateEndDate);
        projectPage.verifyDetailProjectAfterUpdate(updateEndDate);
    }

    @Owner("Phat Cao")
    @Epic("Project Management")
    @Feature("Delete project")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test delete project function")
    @Test(dataProvider = "data_deleteProject", dataProviderClass = DataProviderFactory.class, priority = 4)
    public void testDeleteProject(String title, String client, String startDate, String endDate, String summary) {
        loginAsAdminAndOpenProjectMenu();
        ProjectPage projectPage = projectPage();
        projectPage.addNewProject(title, client, startDate, endDate, summary);
        projectPage.deleteProject(title);
        projectPage.verifyProjectNotDisplayedAfterDelete(title);
    }

    @Owner("Phat Cao")
    @Epic("Project Management")
    @Feature("Edit status project")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test edit status project function")
    @Test(dataProvider = "data_editStatusProject", dataProviderClass = DataProviderFactory.class, priority = 1)
    public void testEditStatusProject(String title, String client, String startDate, String endDate, String summary,
                                      String status, String priority, String progress) {
        loginAsAdminAndOpenProjectMenu();
        ProjectPage projectPage = projectPage();
        projectPage.addNewProject(title, client, startDate, endDate, summary);
        projectPage.editStatus(title, progress, status, priority);
        projectPage.verifyStatusProjectAfterUpdate(status, priority, progress);
    }

    @Owner("Phat Cao")
    @Epic("Project Management")
    @Feature("Add attach file for project")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test add attach file function")
    @Test(dataProvider = "data_addAttachFileProject", dataProviderClass = DataProviderFactory.class)
    public void testAddAttachFileProject(String title, String client, String startDate, String endDate, String summary,
                                         String fileName, String filePath) {
        loginAsAdminAndOpenProjectMenu();
        ProjectPage projectPage = projectPage();
        projectPage.addNewProject(title, client, startDate, endDate, summary);
        projectPage.addAttachFile(title, fileName, filePath);
        projectPage.verifyUploadAttachSuccess(fileName);
    }
}
