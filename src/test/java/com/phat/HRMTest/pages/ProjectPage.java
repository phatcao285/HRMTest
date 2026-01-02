package com.phat.HRMTest.pages;

import com.phat.helpers.DateHelper;
import com.phat.helpers.SystemHelper;
import com.phat.keywords.WebUI;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import java.time.LocalDate;
import java.util.List;

public class ProjectPage extends BasePage {

    private final WebDriver driver;

    public ProjectPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    // ===== Add project form =====
    @FindBy(xpath = "//a[normalize-space()='Add New']")
    private WebElement buttonAddNewProject;

    @FindBy(xpath = "//input[@placeholder='Title']")
    private WebElement inputTitle;

    @FindBy(xpath = "//label[@for='client_id']/following::span[contains(@id,'client')]")
    private WebElement selectClient;

    @FindBy(xpath = "//input[@type='search' and @role='searchbox' and contains(@aria-activedescendant,'client')]")
    private WebElement inputSearchClient;

    @FindBy(xpath = "//input[@placeholder='Start Date']")
    private WebElement inputStartDate;

    @FindBy(xpath = "//input[@placeholder='End Date']")
    private WebElement inputEndDate;

    @FindBy(xpath = "//textarea[@id='summary']")
    private WebElement inputSummary;

    @FindBy(xpath = "//span[normalize-space()='Save']/parent::button")
    private WebElement buttonSave;

    // ===== Date picker form =====
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

    // ===== Status project =====
    @FindBy(xpath = "//span[normalize-space()='Completed']/ancestor::div/h2")
    private WebElement totalProjectsCompleted;

    @FindBy(xpath = "//span[normalize-space()='In Progress']/ancestor::div/h2")
    private WebElement totalProjectsInProgress;

    @FindBy(xpath = "//span[normalize-space()='Not Started']/ancestor::div/h2")
    private WebElement totalProjectsNotStarted;

    @FindBy(xpath = "//span[normalize-space()='On Hold']/ancestor::div/h2")
    private WebElement totalProjectsOnHold;

    // ===== Common =====
    @FindBy(xpath = "//div[contains(@class,'toast-success')]")
    private WebElement alertSuccess;

    @FindBy(xpath = "//input[@type='search' and @aria-controls='xin_table']")
    private WebElement inputSearch;

    @FindBy(xpath = "//button/parent::a[contains(@href,'project-detail')]")
    private WebElement buttonViewDetail;

    @FindBy(xpath = "//button/parent::span[@data-original-title='Delete']")
    private WebElement buttonDeleteProject;

    @FindBy(xpath = "//div[@class='modal-content']//span[normalize-space()='Confirm']")
    private WebElement buttonConfirmDelete;

    // ===== Overview tab =====
    @FindBy(xpath = "//span[normalize-space()='Update Project']/parent::button")
    private WebElement buttonUpdateProject;

    @FindBy(xpath = "//div[@id='pills-overview']//tr[1]/td[2]")
    private WebElement textTitle;

    @FindBy(xpath = "//div[@id='pills-overview']//tr[2]/td[2]")
    private WebElement textClient;

    @FindBy(xpath = "//div[@id='pills-overview']//tr[5]/td[2]")
    private WebElement textStartDate;

    @FindBy(xpath = "//div[@id='pills-overview']//tr[6]/td[2]")
    private WebElement textEndDate;

    @FindBy(xpath = "//div[@id='pills-overview']//div[3]")
    private WebElement textSummary;

    // ===== Edit tab =====
    @FindBy(xpath = "//a[normalize-space()='Edit']")
    private WebElement tabEdit;

    // ===== Status controls =====
    @FindBy(xpath = "//span[@class='irs-single']")
    private WebElement sliderValue;

    @FindBy(xpath = "//select[@name='priority']/following-sibling::span")
    private WebElement selectPriority;

    @FindBy(xpath = "//span[normalize-space()='Update Status']/parent::button")
    private WebElement buttonUpdateStatus;

    // ===== Attach tab =====
    @FindBy(xpath = "//a[normalize-space()='Attach files']")
    private WebElement tabAttachFile;

    @FindBy(xpath = "//input[@name='file_name']")
    private WebElement inputFileName;

    @FindBy(xpath = "//input[@id='attachment_file']")
    private WebElement buttonChooseFile;

    @FindBy(xpath = "//span[normalize-space()='Add File']/parent::button")
    private WebElement buttonAddFile;

    private int totalProjectsCompletedBefore;
    private int totalProjectsInProgressBefore;
    private int totalProjectsNotStartedBefore;
    private int totalProjectsOnHoldBefore;

    private int getTotalProjectsCompleted() {
        return Integer.parseInt(WebUI.getElementText(totalProjectsCompleted));
    }

    private int getTotalProjectsInProgress() {
        return Integer.parseInt(WebUI.getElementText(totalProjectsInProgress));
    }

    private int getTotalProjectsNotStarted() {
        return Integer.parseInt(WebUI.getElementText(totalProjectsNotStarted));
    }

    private int getTotalProjectsOnHold() {
        return Integer.parseInt(WebUI.getElementText(totalProjectsOnHold));
    }

    private void clickAddNewProject() {
        WebUI.waitForElementVisible(buttonAddNewProject);
        WebUI.clickElement(buttonAddNewProject);
    }

    private void searchTitle(String title) {
        WebUI.waitForElementVisible(inputSearch);
        WebUI.setText(inputSearch, title);
        WebUI.sleep(1);
    }

    private void clickViewDetail() {
        WebUI.hoverMouse(buttonViewDetail);
        WebUI.waitForElementVisible(buttonViewDetail);
        WebUI.clickElement(buttonViewDetail);
    }

    private void clickDeleteProject() {
        WebUI.hoverMouse(buttonDeleteProject);
        WebUI.waitForElementVisible(buttonDeleteProject);
        WebUI.clickElement(buttonDeleteProject);
    }

    private void selectDate(String date) {
        LocalDate getDate = DateHelper.parseDate(date);
        String expectDay = String.format("%02d", getDate.getDayOfMonth());
        String expectMonth = getDate.getMonth().toString().substring(0, 3).toUpperCase();
        String expectYear = String.valueOf(getDate.getYear());

        while (true) {
            String actualMonth = currentMonth.getText();
            if (actualMonth.equalsIgnoreCase(expectMonth)) {
                break;
            }
            if (DateHelper.convertMonthToNumber(expectMonth) < DateHelper.convertMonthToNumber(actualMonth)) {
                iconBackMonth.click();
            } else {
                iconForwardMonth.click();
            }
        }
        WebUI.logConsole("Select month: " + expectMonth);
        Allure.step("Select month: " + expectMonth);

        while (true) {
            String actualYear = currentYear.getText();
            if (actualYear.equalsIgnoreCase(expectYear)) {
                break;
            }
            if (Integer.parseInt(expectYear) < Integer.parseInt(actualYear)) {
                iconBackYear.click();
            } else {
                iconForwardYear.click();
            }
        }
        WebUI.logConsole("Select year: " + expectYear);
        Allure.step("Select year: " + expectYear);

        for (WebElement element : currentDate) {
            if (element.getText().equalsIgnoreCase(expectDay)) {
                element.click();
                break;
            }
        }
        WebUI.logConsole("Select date: " + expectDay);
        Allure.step("Select date: " + expectDay);
    }

    public void addNewProject(String title, String client, String startDate, String endDate, String summary) {
        clickAddNewProject();
        WebUI.setText(inputTitle, title);
        WebUI.clickElement(selectClient);
        WebUI.setText(inputSearchClient, client);
        WebElement optionClient = driver.findElement(By.xpath("//li[normalize-space()='" + client + "']"));
        WebUI.clickElement(optionClient);
        WebUI.clickElement(inputStartDate);
        selectDate(startDate);
        WebUI.clickElement(buttonOK);
        WebUI.clickElement(inputEndDate);
        selectDate(endDate);
        WebUI.clickElement(buttonOK);
        WebUI.setText(inputSummary, summary);
        WebUI.clickElement(buttonSave);
    }

    public void editProject(String title, String updateEndDate) {
        searchTitle(title);
        clickViewDetail();
        WebUI.clickElement(tabEdit);
        WebUI.clickElement(inputEndDate);
        selectDate(updateEndDate);
        WebUI.clickElement(buttonOK);
        WebUI.clickElement(buttonUpdateProject);
    }

    public void editStatus(String title, String progress, String status, String priority) {
        WebUI.refreshPage();
        totalProjectsCompletedBefore = getTotalProjectsCompleted();
        totalProjectsInProgressBefore = getTotalProjectsInProgress();
        totalProjectsOnHoldBefore = getTotalProjectsOnHold();
        totalProjectsNotStartedBefore = getTotalProjectsNotStarted();
        searchTitle(title);
        clickViewDetail();
        WebUI.waitForElementVisible(buttonUpdateStatus);
        WebElement sliderLine = driver.findElement(By.xpath("//input[@id='progres_val']"));
        WebUI.setValueToSlider(sliderLine, progress);
        WebUI.clickElement(By.xpath("//a[normalize-space(@data-rating-text)='" + status + "']"));
        WebUI.clickElement(selectPriority);
        WebUI.clickElement(By.xpath("//ul/li[normalize-space()='" + priority + "']"));
        WebUI.clickElement(buttonUpdateStatus);
    }

    public void deleteProject(String title) {
        WebUI.refreshPage();
        totalProjectsNotStartedBefore = getTotalProjectsNotStarted();
        searchTitle(title);
        clickDeleteProject();
        WebUI.clickElement(buttonConfirmDelete);
    }

    public void addAttachFile(String title, String fileName, String filePath) {
        searchTitle(title);
        clickViewDetail();
        WebUI.waitForElementVisible(tabAttachFile);
        WebUI.clickElement(tabAttachFile);
        WebUI.setText(inputFileName, fileName);
        WebUI.uploadFile(buttonChooseFile, SystemHelper.getCurrentDir() + filePath);
        WebUI.clickElement(buttonAddFile);
    }

    public void verifyAddProjectSuccess(String title, String client, String startDate, String endDate, String summary) {
        int actualTotal = getTotalProjectsNotStarted();
        WebUI.refreshPage();
        int expectTotal = actualTotal + 1;
        WebUI.softVerifyEqual(getTotalProjectsNotStarted(), expectTotal, "Total project notstarted not match with expected");
        searchTitle(title);
        String actualTextTitle = WebUI.getElementText(By.xpath("//table[@id='xin_table']//td[1]"));
        WebUI.softVerifyEqual(actualTextTitle, title, title + "Title in table not match with expected");
        clickViewDetail();
        WebUI.softVerifyEqual(WebUI.getElementText(textTitle), title, "Title not match with expected");
        WebUI.softVerifyEqual(WebUI.getElementText(textClient), client, "Client not match with expected");
        WebUI.softVerifyEqual(WebUI.getElementText(textStartDate).trim(), startDate, "Start date not match with expected");
        WebUI.softVerifyEqual(WebUI.getElementText(textEndDate).trim(), endDate, "End date not match with expected");
        WebUI.softVerifyEqual(WebUI.getElementText(textSummary).replace("Summary", "").trim(), summary, "Summary not match with expected");
        WebUI.assertAll();
    }

    public void verifyUploadAttachSuccess(String fileName) {
        WebUI.sleep(4);
        WebUI.clickElement(tabAttachFile);
        By hyperlink = By.xpath("//h6[@class='mb-0' and normalize-space(text()[1])='" + fileName + "']/parent::a/following-sibling::div//a[contains(@href,'download')]");
        WebUI.verifyFileUpLoaded(hyperlink, "File not uploaded");
    }

    public void verifyDetailProjectAfterUpdate(String updateEndDate) {
        WebUI.waitForElementVisible(textEndDate);
        WebUI.softVerifyEqual(WebUI.getElementText(textEndDate).trim(), updateEndDate, "End date not match with expected");
        WebUI.assertAll();
    }

    private void handleCompleted() {
        int actualTotalCompleted = getTotalProjectsCompleted();
        int expectTotalCompleted = totalProjectsCompletedBefore + 1;
        WebUI.softVerifyEqual(actualTotalCompleted, expectTotalCompleted, "Total project completed not match with expected");
        int actualTotalNotStarted = getTotalProjectsNotStarted();
        int expectTotalNotStarted = totalProjectsNotStartedBefore - 1;
        WebUI.softVerifyEqual(actualTotalNotStarted, expectTotalNotStarted, "Total project notstarted not match with expected");
    }

    private void handleInProgress() {
        int actualTotalInProgress = getTotalProjectsInProgress();
        int expectTotalInProgress = totalProjectsInProgressBefore + 1;
        WebUI.softVerifyEqual(actualTotalInProgress, expectTotalInProgress, "Total project inprogress not match with expected");
        int actualTotalNotStarted = getTotalProjectsNotStarted();
        int expectTotalNotStarted = totalProjectsNotStartedBefore - 1;
        WebUI.softVerifyEqual(actualTotalNotStarted, expectTotalNotStarted, "Total project notstarted not match with expected");
    }

    private void handleOnHold() {
        int actualTotalOnHold = getTotalProjectsOnHold();
        int expectTotalOnHold = totalProjectsOnHoldBefore + 1;
        WebUI.softVerifyEqual(actualTotalOnHold, expectTotalOnHold, "Total project onhold not match with expected");
        int actualTotalNotStarted = getTotalProjectsNotStarted();
        int expectTotalNotStarted = totalProjectsNotStartedBefore - 1;
        WebUI.softVerifyEqual(actualTotalNotStarted, expectTotalNotStarted, "Total project notstarted not match with expected");
    }

    private void handleCancled() {
        int actualTotalNotStarted = getTotalProjectsNotStarted();
        int expectTotalNotStarted = totalProjectsNotStartedBefore + 1;
        WebUI.softVerifyEqual(actualTotalNotStarted, expectTotalNotStarted, "Total project notstarted not match with expected");
    }

    public void verifyStatusProjectAfterUpdate(String status, String priority, String progress) {
        WebUI.refreshPage();
        String actualStatus = WebUI.getElementText(By.xpath("//div[@class='br-current-rating' and normalize-space()='" + status + "']"));
        WebUI.softVerifyEqual(actualStatus, status, "Status not match with expected");
        String actualPriority = WebUI.getElementText(selectPriority);
        WebUI.softVerifyEqual(actualPriority, priority, "Priority not match with expected");
        String actualProgress = WebUI.getElementText(sliderValue);
        WebUI.softVerifyEqual(actualProgress, progress, "Progress not match with expected");
        WebUI.backToPreviousPage();
        switch (status) {
            case "Completed":
                handleCompleted();
                break;
            case "In Progress":
                handleInProgress();
                break;
            case "On Hold":
                handleOnHold();
                break;
            case "Cancelled":
                handleCancled();
                break;
            default:
                WebUI.logConsole(status + " is not exist");
                break;
        }
        WebUI.assertAll();
    }

    public void verifyProjectNotDisplayedAfterDelete(String title) {
        WebUI.refreshPage();
        int actualTotalNotStarted = getTotalProjectsNotStarted();
        int expectTotalNotStarted = totalProjectsNotStartedBefore - 1;
        WebUI.softVerifyEqual(actualTotalNotStarted, expectTotalNotStarted, "Total project notstarted not match with expected");
        searchTitle(title);
        List<WebElement> listProject = driver.findElements(By.xpath("//table//td[1][normalize-space()='" + title + "']"));
        WebUI.verifyNotDisplay(listProject, title, title + " still display in table after delete");
        WebUI.assertAll();
    }
}
