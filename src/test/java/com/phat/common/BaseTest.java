package com.phat.common;

import com.phat.driver.DriverManager;
import com.phat.helpers.PropertiesHelper;
import com.phat.helpers.SoftAssertHelper;
import com.phat.listeners.TestListener;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public class BaseTest {

    public WebDriver driver;

    @BeforeSuite
    public void runConfig() {
        PropertiesHelper.loadAllFiles();
    }

    @BeforeMethod
    public void openBrowser() {
        // ❌ KHÔNG khai báo lại WebDriver driver ở đây
        String browser   = PropertiesHelper.getValue("BROWSER").trim().toLowerCase();
        boolean headless = "true".equalsIgnoreCase(PropertiesHelper.getValue("HEADLESS"));

        Allure.step("Open " + browser + " browser");

        switch (browser) {
            case "chrome" -> {
                ChromeOptions opts = new ChromeOptions();
                if (headless) {
                    opts.addArguments("--headless=new", "--window-size=1920,1080");
                }
                // "--guest" chỉ thực sự có ý nghĩa với Chrome
                opts.addArguments("--guest");
                driver = new ChromeDriver(opts);
            }
            case "firefox" -> {
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) {
                    opts.addArguments("-headless"); // cú pháp headless của Firefox
                }
                driver = new FirefoxDriver(opts);
            }
            case "edge" -> {
                EdgeOptions opts = new EdgeOptions();
                if (headless) {
                    opts.addArguments("--headless", "--window-size=1920,1080");
                }
                driver = new EdgeDriver(opts);
            }
            default -> {
                ChromeOptions opts = new ChromeOptions();
                if (headless) {
                    opts.addArguments("--headless=new", "--window-size=1920,1080");
                }
                opts.addArguments("--guest");
                driver = new ChromeDriver(opts);
            }
        }

        // Gắn vào DriverManager + maximize
        DriverManager.setDriver(driver);
        if (!headless) {
            DriverManager.getDriver().manage().window().maximize();
        }

        // Reset SoftAssert trước mỗi test
        SoftAssertHelper.resetSoftAssert();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver drv = DriverManager.getDriver();
        if (drv != null) {
            drv.quit();
            Allure.step("Close browser");
        }
    }
}
