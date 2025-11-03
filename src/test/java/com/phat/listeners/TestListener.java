package com.phat.listeners;

import com.phat.helpers.CaptureHelper;
import com.phat.helpers.PropertiesHelper;
import com.phat.mail.EmailSender;
import com.phat.reports.AllureManager;
import com.phat.utils.LogUtils;
import org.testng.*;

import java.io.IOException;

public class TestListener implements ISuiteListener, ITestListener {
    public static int count_totalTCs;
    static int count_passedTCs;
    static int count_skippedTCs;
    static int count_failedTCs;

    @Override
    public void onStart(ISuite suite) {
        LogUtils.info("********** RUN STARTED **********");
    }

    @Override
    public void onFinish(ISuite suite) {
        LogUtils.info("********** RUN FINISHED **********");
        LogUtils.info("Test Summary:");
        LogUtils.info("Total TCs: " + count_totalTCs);
        LogUtils.info("Passed TCs: " + count_passedTCs);
        LogUtils.info("Failed TCs: " + count_failedTCs);
        LogUtils.info("Skipped TCs: " + count_skippedTCs);

        if ("yes".equalsIgnoreCase(PropertiesHelper.getValue("SEND_EMAIL_TO_USERS"))) {
            ProcessBuilder pb = new ProcessBuilder(
                    "D:\\allure-2.35.1\\bin\\allure.bat", "generate",
                    "--single-file",
                    "target/allure-results",
                    "--clean",
                    "-o", "target/allure-report"
            );
            try {
                Process process = pb.start();
                int check = process.waitFor();
                if (check == 0) {
                    LogUtils.info("Allure report generated successfully!");
                } else {
                    LogUtils.info("Failed to generate Allure report!");
                }
            } catch (IOException | InterruptedException e) {
                LogUtils.error("Error while generating Allure report: " + e);
            }

            EmailSender.sendMail(
                    System.getProperty("os.name"),
                    PropertiesHelper.getValue("BROWSER"),
                    count_totalTCs,
                    count_passedTCs,
                    count_failedTCs,
                    count_skippedTCs
            );
            LogUtils.info("Sending result email to users successful");
        }
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        LogUtils.info("Test case: " + iTestResult.getMethod().getMethodName() + " is starting...");
        count_totalTCs++;
        if ("yes".equalsIgnoreCase(PropertiesHelper.getValue("RECORD_VIDEO"))) {
            CaptureHelper.startRecord(iTestResult.getMethod().getMethodName());
        }
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        LogUtils.info("Test case: " + iTestResult.getMethod().getMethodName() + " is passed.");
        count_passedTCs++;
        // Align with AllureListener: attach screenshot on pass
        try {
            AllureManager.saveScreenshotPNG();
        } catch (Exception ignored) {
        }
        if ("yes".equalsIgnoreCase(PropertiesHelper.getValue("RECORD_VIDEO"))) {
            CaptureHelper.stopRecord();
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        LogUtils.error("Test case: " + iTestResult.getMethod().getMethodName() + " is failed.");
        LogUtils.error("Reason: " + iTestResult.getThrowable());
        count_failedTCs++;
        // Align with AllureListener: attach screenshot on fail
        try {
            AllureManager.saveScreenshotPNG();
        } catch (Exception ignored) {
        }
        if ("yes".equalsIgnoreCase(PropertiesHelper.getValue("RECORD_VIDEO"))) {
            CaptureHelper.stopRecord();
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        LogUtils.warn("Test case: " + iTestResult.getMethod().getMethodName() + " is skipped.");
        LogUtils.warn("Reason: " + iTestResult.getThrowable());
        count_skippedTCs++;
        if ("yes".equalsIgnoreCase(PropertiesHelper.getValue("RECORD_VIDEO"))) {
            CaptureHelper.stopRecord();
        }
    }
}

