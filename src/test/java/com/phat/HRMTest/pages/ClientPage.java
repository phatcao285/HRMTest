package com.phat.HRMTest.pages;

import com.phat.helpers.SystemHelper;
import com.phat.keywords.WebUI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import java.util.List;

public class ClientPage extends BasePage {
    private final WebDriver driver;

    public ClientPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    // ====== FORM ELEMENTS ======
    @FindBy(xpath = "//a[normalize-space()='Add New']")
    private WebElement buttonAddNewClient;

    @FindBy(xpath = "//input[@placeholder='First Name']")
    private WebElement inputFirstName;

    @FindBy(xpath = "//input[@placeholder='Last Name']")
    private WebElement inputLastName;

    @FindBy(xpath = "//input[@placeholder='Password']")
    private WebElement inputPassword;

    @FindBy(xpath = "//input[@placeholder='Contact Number']")
    private WebElement inputContactNumber;

    // Trigger dropdown giới tính
    @FindBy(xpath = "//label[@for='gender']/following::span[contains(@id,'gender')]")
    private WebElement selectGender;

    @FindBy(xpath = "//input[@placeholder='Email']")
    private WebElement inputEmail;

    @FindBy(xpath = "//input[@placeholder='Username']")
    private WebElement inputUsername;

    @FindBy(xpath = "//input[@name='file']")
    private WebElement uploadAttachment;

    @FindBy(xpath = "//span[normalize-space()='Save']/parent::button")
    private WebElement buttonSave;

    // Country (edit)
    @FindBy(xpath = "//label[@for='country']/following::span[contains(@id,'country')]")
    private WebElement selectCountry;

    @FindBy(xpath = "//input[@type='search' and @role='searchbox']")
    private WebElement inputSearchCountry;

    @FindBy(xpath = "//div[@class='card-body']//button[@type='submit']")
    private WebElement buttonSubmit;

    // Toast / alert
    @FindBy(xpath = "//div[contains(@class,'toast-success')]")
    private WebElement toastMessageSuccess;

    // ====== TABLE / LIST ======
    @FindBy(xpath = "//input[@type='search']")
    private WebElement inputSearch;

    // Hành động trên dòng đầu (sau khi search)
    @FindBy(xpath = "//button/parent::a[contains(@href,'view-client')]")
    private WebElement buttonViewDetail;

    @FindBy(xpath = "//button/parent::span[@data-original-title='Delete']")
    private WebElement buttonDeleteClient;

    @FindBy(xpath = "//div[@class='modal-content']//span[normalize-space()='Confirm']")
    private WebElement buttonConfirmDelete;

    @FindBy(xpath = "//div[@class='certificated-badge']/preceding-sibling::img")
    private WebElement avatarClient;

    // ====== PRIVATE HELPERS (dynamic WebElement) ======
    private WebElement genderOption(String text) {
        return driver.findElement(By.xpath("//li[normalize-space()='" + text + "']"));
    }

    private WebElement countryOption(String text) {
        // đa số dropdown kiểu select2/choices render <li>
        return driver.findElement(By.xpath("//li[normalize-space()='" + text + "']"));
    }

    private WebElement usernameCell(String username) {
        return driver.findElement(By.xpath("//table//td[2][normalize-space()='" + username + "']"));
    }

    public void clickAddNewClient() {
        // đề phòng header dropdown còn mở do click trước đó
//        WebUI.ensureCleanViewport();
        WebUI.waitForElementVisible(buttonAddNewClient);
        WebUI.clickElement(buttonAddNewClient);
    }


    private void searchClient(String username) {
        WebUI.waitForElementVisible(inputSearch);
        WebUI.clearTextWithKey(inputSearch);
        WebUI.setText(inputSearch, username);
        WebUI.sleep(1.0);
    }

    private void clickViewDetail() {
        // nếu cần hover trước để lộ action
        WebUI.hoverMouse(buttonViewDetail);
        WebUI.waitForElementVisible(buttonViewDetail);
        WebUI.clickElement(buttonViewDetail);
    }

    private void clickDeleteClient() {
        WebUI.hoverMouse(buttonDeleteClient);
        WebUI.waitForElementVisible(buttonDeleteClient);
        WebUI.clickElement(buttonDeleteClient);
    }

    // ====== ACTIONS ======
    public void addNewClient(String firstName, String lastName, String password, String contactNumber,
                             String gender, String email, String username, String filepath) {

        clickAddNewClient();

        WebUI.setTextStrict(inputFirstName, firstName);
        WebUI.setTextStrict(inputLastName, lastName);
        WebUI.setTextStrict(inputPassword, password);
        WebUI.setTextStrict(inputContactNumber, contactNumber);

        WebUI.clickElement(selectGender);
        WebUI.waitForElementVisible(genderOption(gender));
        WebUI.clickElement(genderOption(gender));

        WebUI.setTextStrict(inputEmail, email);
        WebUI.setTextStrict(inputUsername, username);

        WebUI.uploadFile(uploadAttachment, SystemHelper.getCurrentDir() + filepath);

        WebUI.clickElement(buttonSave);
    }

    public void editClient(String searchUsername, String country) {
        searchClient(searchUsername);
        clickViewDetail();
        WebUI.clickElement(selectCountry);
        WebUI.setText(inputSearchCountry, country);
        WebUI.waitForElementVisible(countryOption(country));
        WebUI.clickElement(countryOption(country));
        WebUI.clickElement(buttonSubmit);
    }

    public void deleteClient(String username) {
        searchClient(username);
        clickDeleteClient();
        WebUI.waitForElementVisible(buttonConfirmDelete);
        WebUI.clickElement(buttonConfirmDelete);
    }

    // ====== VERIFICATIONS ======
    public void verifyAddNewClientSuccess(String username) {
        WebUI.waitForElementVisible(toastMessageSuccess);
        String actualToast = WebUI.getElementText(toastMessageSuccess);
        WebUI.softVerifyEqual(actualToast.trim(), "Client added.", "Toast message không khớp");

        searchClient(username);
        String actualUsername = WebUI.getElementText(usernameCell(username));
        WebUI.verifyEqual(actualUsername, username, "Username hiển thị trên bảng không khớp");

        WebUI.assertAll();
    }

    public void verifyDetailClientAfterAddNew(String firstName, String lastName, String contactNumber,
                                              String gender, String email, String username) {
        clickViewDetail();

        WebUI.softVerifyEqual(WebUI.getElementAttribute(inputFirstName, "value"), firstName, "First name không khớp");
        WebUI.softVerifyEqual(WebUI.getElementAttribute(inputLastName, "value"), lastName, "Last name không khớp");
        WebUI.softVerifyEqual(WebUI.getElementAttribute(inputContactNumber, "value"), contactNumber, "Contact number không khớp");
        WebUI.softVerifyEqual(WebUI.getElementAttribute(inputEmail, "value"), email, "Email không khớp");
        WebUI.softVerifyEqual(WebUI.getElementAttribute(inputUsername, "value"), username, "Username không khớp");

        // tuỳ UI: selectGender có thể lưu title/text khác nhau
        String actualGender = WebUI.getElementAttribute(selectGender, "title");
        WebUI.softVerifyEqual(actualGender != null ? actualGender.trim() : "", gender, "Gender không khớp");

        WebUI.verifyImageUpLoaded(By.xpath("//div[@class='certificated-badge']/preceding-sibling::img"), "Avatar client not loaded");
        WebUI.assertAll();
    }

    public void verifyDetailClientAfterUpdate(String country) {
        WebUI.refreshPage();
        String actualCountry = WebUI.getElementAttribute(selectCountry, "title");
        WebUI.softVerifyEqual(actualCountry != null ? actualCountry.trim() : "", country, "Country không khớp");
        WebUI.assertAll();
    }

    public void verifyClientNotDisplayedAfterDelete(String username) {
        searchClient(username);
        WebUI.refreshPage();
        searchClient(username);

        // nếu muốn thuần PageFactory, có thể đổi sang driver.findElements trong helper
        List<WebElement> listClient = driver.findElements(By.xpath("//table//td[2][normalize-space()='" + username + "']"));
        WebUI.verifyNotDisplay(listClient, username, username + " still display in table after delete");
    }
}
