package com.phat.listeners;

import com.phat.driver.DriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

public class AllureListener implements TestLifecycleListener {

    @Override
    public void beforeTestSchedule(TestResult result) {
    }

    @Override
    public void afterTestSchedule(TestResult result) {
    }

    @Override
    public void beforeTestUpdate(TestResult result) {
    }

    @Override
    public void afterTestUpdate(TestResult result) {
    }

    @Override
    public void beforeTestStart(TestResult result) {
    }

    @Override
    public void afterTestStart(TestResult result) {
    }

    @Override
    public void beforeTestStop(TestResult result) {
        Status status = result.getStatus();
        if (status == Status.PASSED) {
            attachScreenshotSafe(result.getName(), "Passed");
        } else if (status == Status.FAILED) {
            attachScreenshotSafe(result.getName(), "Failed");
        }
    }

    private void attachScreenshotSafe(String testName, String outcome) {
        try {
            Object driver = DriverManager.getDriver();
            if (driver instanceof TakesScreenshot) {
                byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(testName + "_" + outcome + "_Screenshot", new ByteArrayInputStream(bytes));
            }
        } catch (Exception ignored) {
            // Avoid breaking the test lifecycle if screenshot capture fails
        }
    }

    @Override
    public void afterTestStop(TestResult result) {
    }

    @Override
    public void beforeTestWrite(TestResult result) {
    }

    @Override
    public void afterTestWrite(TestResult result) {
    }

}
