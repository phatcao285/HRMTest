package com.phat.HRMTest.pages;

import com.phat.driver.DriverManager;
import com.phat.helpers.DateHelper;
import com.phat.keywords.WebUI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.LocalDate;
import java.util.List;

public class TaskPage {
    private WebDriver driver;

    // Add Task form
    @FindBy(xpath = "//a[normalize-space()='Add New']")
    private WebElement buttonAddNewTask;

    @FindBy(xpath = "//a[normalize-space()='Tasks' and @id='pills-tasks-tab']")
    private WebElement tabTask;

    @FindBy(xpath = "//input[@placeholder='Title' and @name='task_name']")
    private WebElement inputTitleTask;

    @FindBy(xpath = "//input[@placeholder='Start Date']")
    private WebElement inputStartDateTask;

    @FindBy(xpath = "//input[@placeholder='End Date']")
    private WebElement inputEndDateTask;

    @FindBy(xpath = "//label[@for='project_ajax']/following::span[contains(@id,'project')]")
    private WebElement selectProject;

    @FindBy(xpath = "//input[@type='search' and @role='searchbox' and contains(@aria-activedescendant,'project')]")
    private WebElement inputSearchProject;

    @FindBy(xpath = "//textarea[@id='summary']")
    private WebElement inputSummary;

    @FindBy(xpath = "//span[normalize-space()='Save']/parent::button")
    private WebElement buttonSave;

    // Date picker form
    @FindBy(xpath = "//div[@class='dtp animated fadeIn']//div[contains(@class,'actual-month')]")
    private WebElement currentMonth;

    @FindBy(xpath = "//div[@class='dtp animated fadeIn']//a[@class='dtp-select-month-after']")
    private WebElement iconForwardMonth;

    @FindBy(xpath = "//div[@class='dtp animated fadeIn']//a[@class='dtp-select-month-before']")
    private WebElement iconBackMonth;

    @FindBy(xpath = "//div[@class='dtp animated fadeIn']//div[contains(@class,'actual-year')]")
    private WebElement currentYear;

    @FindBy(xpath = "//div[@class='dtp animated fadeIn']//a[@class='dtp-select-year-after']")
    private WebElement iconForwardYear;

    @FindBy(xpath = "//div[@class='dtp animated fadeIn']//a[@class='dtp-select-year-before']")
    private WebElement iconBackYear;

    @FindBy(xpath = "//div[@class='dtp animated fadeIn']//table[contains(@class,'picker-days')]//td")
    private List<WebElement> currentDate;

    @FindBy(xpath = "//div[@class='dtp animated fadeIn']//div[@class='dtp-buttons']/button[text()='OK']")
    private WebElement buttonOK;

    // Status task
    @FindBy(xpath = "//span[normalize-space()='Not Started']/ancestor::div/h2")
    private WebElement totalTasksNotStarted;

    // Common
    @FindBy(xpath = "//input[@type='search' and @aria-controls='xin_table']")
    private WebElement inputSearch;

    @FindBy(xpath = "//button/parent::a[contains(@href,'task-detail')]")
    private WebElement buttonViewDetail;

    @FindBy(xpath = "//button/parent::span[@data-original-title='Delete']")
    private WebElement buttonDeleteTask;

    @FindBy(xpath = "//div[@class='modal-content']//span[normalize-space()='Confirm']")
    private WebElement buttonConfirmDelete;

    // Overview task tab
    @FindBy(xpath = "//div[@id='pills-overview']//tr[1]/td[2]")
    private WebElement textTitle;

    @FindBy(xpath = "//div[@id='pills-overview']//tr[2]/td[2]")
    private WebElement textStartDate;

    @FindBy(xpath = "//div[@id='pills-overview']//tr[3]/td[2]")
    private WebElement textEndDate;

    @FindBy(xpath = "//div[@id='pills-overview']//tr[5]/td[2]")
    private WebElement textProject;

    @FindBy(xpath = "//div[@id='pills-overview']//div[3]")
    private WebElement textSummary;

    @FindBy(xpath = "//tbody/tr[@role='row']")
    private WebElement taskRow;

    private int TotalTaskNotStartedBefore;

    // Constructor - No parameters, uses DriverManager
    public TaskPage(WebDriver driver) {
        this.driver = DriverManager.getDriver();
        PageFactory.initElements(this.driver, this);
    }

    private int getTotalTasksNotStarted() {
        String total_text = totalTasksNotStarted.getText();
        return Integer.parseInt(total_text);
    }

    private void clickAddNewTask() {
        WebUI.waitForElementVisible(buttonAddNewTask);
        buttonAddNewTask.click();
    }

    private void searchTitle(String title) {
        WebUI.waitForElementVisible(inputSearch);
        inputSearch.clear();
        inputSearch.sendKeys(title);
        WebUI.sleep(1);
    }

    private void clickViewDetail() {
        WebUI.hoverMouse(taskRow);
        WebUI.waitForElementVisible(buttonViewDetail);
        buttonViewDetail.click();
    }

    private void clickDeleteTask() {
        WebUI.hoverMouse(taskRow);
        WebUI.waitForElementVisible(buttonDeleteTask);
        buttonDeleteTask.click();
    }

    private void selectDate(String date) {
        LocalDate getdate = DateHelper.parseDate(date);
        String expect_day = String.format("%02d", getdate.getDayOfMonth());
        String expect_month = String.valueOf(getdate.getMonth().toString().substring(0, 3).toUpperCase());
        String expect_year = String.valueOf(getdate.getYear());

        // Handle month
        while (true) {
            String actual_month = currentMonth.getText();
            if (actual_month.equalsIgnoreCase(expect_month)) {
                break;
            }
            if (DateHelper.convertMonthToNumber(expect_month) < DateHelper.convertMonthToNumber(actual_month)) {
                iconBackMonth.click();
            } else {
                iconForwardMonth.click();
            }
        }
        WebUI.logConsole("Select month: " + expect_month);

        // Handle year
        while (true) {
            String actual_year = currentYear.getText();
            if (actual_year.equalsIgnoreCase(expect_year)) {
                break;
            }
            if (Integer.parseInt(expect_year) < Integer.parseInt(actual_year)) {
                iconBackYear.click();
            } else {
                iconForwardYear.click();
            }
        }
        WebUI.logConsole("Select year: " + expect_year);

        for (WebElement e : currentDate) {
            if (e.getText().equalsIgnoreCase(expect_day)) {
                e.click();
                break;
            }
        }
        WebUI.logConsole("Select date: " + expect_day);
    }

    public void addNewTask(String titleTask, String startDateTask, String endDateTask, String titleProject, String summary) {
        clickAddNewTask();
        WebUI.waitForElementVisible(inputTitleTask);
        inputTitleTask.clear();
        inputTitleTask.sendKeys(titleTask);
        inputStartDateTask.click();
        selectDate(startDateTask);
        buttonOK.click();
        inputEndDateTask.click();
        selectDate(endDateTask);
        buttonOK.click();
        selectProject.click();
        inputSearchProject.clear();
        inputSearchProject.sendKeys(titleProject);

        // Dynamic element for project option
        WebElement optionProject = driver.findElement(
                org.openqa.selenium.By.xpath("//li[normalize-space()='" + titleProject + "']")
        );
        optionProject.click();

        inputSummary.clear();
        inputSummary.sendKeys(summary);
        buttonSave.click();
    }

    public void deleteTask(String titleTask) {
        WebUI.refreshPage();
        TotalTaskNotStartedBefore = getTotalTasksNotStarted();
        searchTitle(titleTask);
        clickDeleteTask();
        buttonConfirmDelete.click();
    }

    public void verifyAddTaskSuccess(String titleTask, String startDateTask, String endDateTask, String project, String summary) {
        TotalTaskNotStartedBefore = getTotalTasksNotStarted();
        WebUI.refreshPage();
        int expect_TotalTaskNotStarted = TotalTaskNotStartedBefore + 1;
        WebUI.softVerifyEqual(getTotalTasksNotStarted(), expect_TotalTaskNotStarted, "Total task notstarted not match with expected");
        searchTitle(titleTask);
        clickViewDetail();
        WebUI.softVerifyEqual(textTitle.getText(), titleTask, "Title task not match with expected");
        WebUI.softVerifyEqual(textStartDate.getText().trim(), startDateTask, "Start date task not match with expected");
        WebUI.softVerifyEqual(textEndDate.getText().trim(), endDateTask, "End date task not match with expected");
        WebUI.softVerifyEqual(textProject.getText().trim(), project, "Project task not match with expected");
        WebUI.softVerifyEqual(textSummary.getText().replace("Summary", "").trim(), summary, "Summary task not match with expected");
        WebUI.assertAll();
    }

    public void verifyTaskNotDisplayedAfterDelete(String titleTask) {
        WebUI.refreshPage();
        int actual_TotalTasksNotStarted = getTotalTasksNotStarted();
        int expect_TotalTasksNotStarted = TotalTaskNotStartedBefore - 1;
        WebUI.softVerifyEqual(actual_TotalTasksNotStarted, expect_TotalTasksNotStarted, "Total tasks notstarted not match with expected");
        searchTitle(titleTask);

        // Dynamic element for task list
        List<WebElement> list_task = driver.findElements(
                org.openqa.selenium.By.xpath("//table//td[1][normalize-space()='" + titleTask + "']")
        );
        WebUI.verifyNotDisplay(list_task, titleTask, titleTask + " still display in table after delete");
        WebUI.assertAll();
    }
}