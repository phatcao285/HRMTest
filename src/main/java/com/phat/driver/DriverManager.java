package com.phat.driver;

import org.openqa.selenium.WebDriver;

public class DriverManager {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();// Execute in parrallel with distinct WebDriver instance per thread
    public static WebDriver getDriver(){
        return driver.get();// get value of driver in ThreadLocal
    }


    public static void setDriver(WebDriver driver){
        DriverManager.driver.set(driver);
        // assign Webdriver to ThreadLocal
    }
    public static void quit(){
        DriverManager.driver.get().quit();// Close current driver session
        driver.remove();// Remove Thread
    }

}
// If run parallel with single driver in basetest will cause NoSuchSessionException
// ThreadLocal store distinct driver thread initialized
