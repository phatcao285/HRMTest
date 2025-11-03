package com.phat.keywords;

import com.phat.driver.DriverManager;
import com.phat.helpers.CaptureHelper;
import com.phat.helpers.PropertiesHelper;
import com.phat.helpers.SoftAssertHelper;
import com.phat.reports.AllureManager;
import com.phat.utils.LogUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class WebUI {

    // ================== CONFIG ==================
    private static int TIMEOUT = Integer.parseInt(PropertiesHelper.getValue("WAIT_EXPLICIT"));
    private static double STEP_TIME = Double.parseDouble(PropertiesHelper.getValue("SLEEP_TIME"));
    private static final Duration POLL = Duration.ofMillis(500);

    // ================== BASIC UTILS ==================
    public static void sleep(double second) {
        try { Thread.sleep((long) (1000 * second)); }
        catch (InterruptedException e) { throw new RuntimeException(e); }
    }

    public static void logConsole(Object message) { LogUtils.info(message); }

    // ================== NAVIGATION ==================
    @Step("Refresh page")
    public static void refreshPage() {
        DriverManager.getDriver().navigate().refresh();
        logConsole("Refresh page: " + DriverManager.getDriver().getTitle());
        AllureManager.saveTextLog("Page: " + DriverManager.getDriver().getTitle());
    }

    @Step("Back to previous page")
    public static void backToPreviousPage() {
        DriverManager.getDriver().navigate().back();
        logConsole("Back to previous page: " + DriverManager.getDriver().getTitle());
        AllureManager.saveTextLog("Page: " + DriverManager.getDriver().getTitle());
    }

    public static String getCurrentURL() {
        String currentURL = DriverManager.getDriver().getCurrentUrl();
        logConsole("Current URL: " + currentURL);
        AllureManager.saveTextLog("URL: " + currentURL);
        return currentURL;
    }

    @Step("Open URL: {0}")
    public static void openURL(String url) {
        DriverManager.getDriver().get(url);
        sleep(STEP_TIME);
        logConsole("🌐 Open URL: " + url);
    }

    // ================== FRESH HELPERS (NEW) ==================

    // Wait present cho WebElement (PageFactory)
    @Step("Wait for element present (WebElement)")
    public static void waitForElementPresent(WebElement element) {
        try {
            new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), POLL)
                    .until(d -> {
                        try { element.getTagName(); return true; }
                        catch (StaleElementReferenceException ex) { return false; }
                    });
        } catch (Throwable error) {
            onWaitFail("Present (WebElement)", safeDescribe(element), error);
        }
    }

    // Đợi URL chứa đoạn text (sau điều hướng)
    @Step("Wait for URL contains: {0}")
    public static void waitForUrlContains(String fragment) {
        try {
            new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), POLL)
                    .until(ExpectedConditions.urlContains(fragment));
        } catch (Throwable e) {
            onWaitFail("URL contains '" + fragment + "'", "current=" + getCurrentURL(), e);
        }
    }

    // Ngăn trình duyệt tự restore vị trí scroll sau điều hướng
    @Step("Force manual scroll restoration")
    public static void setScrollRestorationManual() {
        try {
            ((JavascriptExecutor) DriverManager.getDriver())
                    .executeScript("if ('scrollRestoration' in history) { history.scrollRestoration='manual'; }");
        } catch (Exception ignored) {}
    }

    // Đóng dropdown/overlay phổ biến (profile menu, modal overlay,…)
    @Step("Ensure overlays & dropdowns are closed")
    public static void ensureCleanViewport() {
        try { DriverManager.getDriver().switchTo().activeElement().sendKeys(Keys.ESCAPE); } catch (Exception ignored) {}
        try { ((JavascriptExecutor) DriverManager.getDriver()).executeScript("document.body.click();"); } catch (Exception ignored) {}
        try {
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript(
                    "document.querySelectorAll('.dropdown-menu.show').forEach(el=>el.classList.remove('show'));"
            );
        } catch (Exception ignored) {}
    }

    // Scroll main content về top (tránh header che)
    @Step("Scroll main content to top")
    public static void scrollMainToTopIfAny() {
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript(
                "const c=document.querySelector('.pc-container .pc-content') || document.scrollingElement; " +
                        "c.scrollTo({top:0,left:0,behavior:'instant'});"
        );
    }

    // Scroll target vào giữa viewport (tránh header che)
    @Step("Scroll into view CENTER (WebElement)")
    public static void scrollIntoViewCenter(WebElement el) {
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
    }

    // Cuộn target bên trong container (PageFactory)
    @Step("Scroll target inside container (PageFactory)")
    public static void scrollInContainerToElement(WebElement container, WebElement target) {
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript(
                "const cArg=arguments[0], t=arguments[1];" +
                        "function scrollableAncestor(el){let e=el;while(e&&e!==document.body){" +
                        " const cs=getComputedStyle(e);" +
                        " if((e.scrollHeight>e.clientHeight)&&/(auto|scroll)/.test(cs.overflow+cs.overflowY))return e;" +
                        " e=e.parentElement;} return document.scrollingElement;}" +
                        "const c=(cArg&&cArg.scrollHeight>cArg.clientHeight)?cArg:scrollableAncestor(t);" +
                        "const tr=t.getBoundingClientRect();" +
                        "const cr=c.getBoundingClientRect?c.getBoundingClientRect():{top:0,height:window.innerHeight};" +
                        "const offset=(tr.top-cr.top)-(cr.height/2 - tr.height/2);" +
                        "c.scrollBy({top:offset,left:0,behavior:'instant'});",
                container, target
        );
        sleep(0.2);
    }

    // Click không gây scroll body (dùng JS)
    @Step("Click on element (WebElement) - NO PAGE SCROLL")
    public static void clickElementNoScroll(WebElement element) {
        try {
            new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), POLL)
                    .until(ExpectedConditions.elementToBeClickable(element));
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", element);
            logConsole("Click via JS (no-scroll) → " + safeDescribe(element));
        } catch (Throwable e) {
            onWaitFail("Clickable (no-scroll)", safeDescribe(element), e);
        }
    }

    // Chờ OR-condition: 1 trong n locators visible
    @SafeVarargs
    @Step("Wait until any visible")
    public static void waitUntilAnyVisible(By... locators) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), POLL);
            wait.until(ExpectedConditions.or(
                    Arrays.stream(locators)
                            .map(ExpectedConditions::visibilityOfElementLocated)
                            .toArray(ExpectedCondition[]::new)
            ));
        } catch (Throwable e) {
            onWaitFail("Any Visible", Arrays.toString(locators), e);
        }
    }

    // Thử switch sang iframe chứa locator
    @Step("Try switch to iframe containing locator")
    public static boolean trySwitchToFrameContaining(By locator) {
        WebDriver d = DriverManager.getDriver();
        d.switchTo().defaultContent();
        List<WebElement> iframes = d.findElements(By.tagName("iframe"));
        for (int i = 0; i < iframes.size(); i++) {
            try {
                d.switchTo().frame(i);
                if (!d.findElements(locator).isEmpty()) return true;
                d.switchTo().defaultContent();
            } catch (Exception ignored) {
                d.switchTo().defaultContent();
            }
        }
        return false;
    }

    // ================== ELEMENT CORE (BY) ==================
    public static WebElement getWebElement(By by) { return DriverManager.getDriver().findElement(by); }
    public static List<WebElement> getWebElements(By by) { return DriverManager.getDriver().findElements(by); }

    // ================== HIGHLIGHT / SCROLL ==================
    @Step("Scroll to element (By) {0}")
    public static void scrollToElementAtTop(By by) {
        sleep(STEP_TIME);
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].scrollIntoView(true);", getWebElement(by));
    }

    @Step("Scroll to element (WebElement)")
    public static void scrollToElementAtTop(WebElement element) {
        sleep(STEP_TIME);
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void scrollToElementAtTop(List<WebElement> elements, int index) {
        sleep(STEP_TIME);
        WebElement element = elements.get(index);
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void highlightElement(By by) {
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].style.border='3px solid red';", getWebElement(by));
    }

    public static void highlightElement(WebElement element) {
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].style.border='3px solid red';", element);
    }

    public static void highlightElement(List<WebElement> elements, int index) {
        WebElement element = elements.get(index);
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].style.border='3px solid red';", element);
    }

    // ================== WAITS (BY) ==================
    public static void waitForElementVisible(By by) {
        try {
            new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), POLL)
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            onWaitFail("Visible", by.toString(), error);
        }
    }

    public static void waitForElementPresent(By by) {
        try {
            new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), POLL)
                    .until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            logConsole("Element not present. " + by);
            Assert.fail("Element not present. " + by);
        }
    }

    public static void waitForElementClickable(By by) {
        try {
            new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), POLL)
                    .until(ExpectedConditions.elementToBeClickable(by));
        } catch (Throwable error) {
            onWaitFail("Clickable", by.toString(), error);
        }
    }

    // ================== WAITS (WEBELEMENT) ==================
    public static void waitForElementVisible(WebElement element) {
        try {
            new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), POLL)
                    .until(ExpectedConditions.visibilityOf(element));
        } catch (Throwable error) {
            onWaitFail("Visible (WebElement)", safeDescribe(element), error);
        }
    }

    public static void waitForElementClickable(WebElement element) {
        try {
            new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT), POLL)
                    .until(ExpectedConditions.elementToBeClickable(element));
        } catch (Throwable error) {
            onWaitFail("Clickable (WebElement)", safeDescribe(element), error);
        }
    }

    // ================== CLICK (BY) ==================
    @Step("Click on element (By): {0}")
    public static void clickElement(By by) {
        waitForElementClickable(by);
        sleep(STEP_TIME);
        WebElement el = getWebElement(by);
        safeClick(el, "By: " + by);
    }

    // ================== CLICK (WEBELEMENT) ==================
    @Step("Click on element (WebElement)")
    public static void clickElement(WebElement element) {
        waitForElementClickable(element);
        sleep(STEP_TIME);
        safeClick(element, "WebElement: " + safeDescribe(element));
    }

    // Core click với fallbacks
    private static void safeClick(WebElement element, String label) {
        try {
            scrollIntoViewCenter(element);
            element.click();
            logConsole("Click → " + label);
        } catch (ElementClickInterceptedException | StaleElementReferenceException e1) {
            try {
                new Actions(DriverManager.getDriver()).moveToElement(element).click().perform();
                logConsole("Click via Actions → " + label);
            } catch (Exception e2) {
                ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].click();", element);
                logConsole("Click via JS → " + label);
            }
        }
    }

    // ================== TEXT / ATTRIBUTE (BY) ==================
    @Step("Clear text (By) {0}")
    public static void clearTextWithKey(By by) {
        waitForElementVisible(by);
        sleep(STEP_TIME);
        WebElement element = getWebElement(by);
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.DELETE);
        logConsole("Clear text: " + by);
    }

    @Step("Set text {1} (By {0})")
    public static void setText(By by, String value) {
        waitForElementVisible(by);
        sleep(STEP_TIME);
        getWebElement(by).sendKeys(value);
        logConsole("Set text \"" + value + "\" → " + by);
    }

    @Step("Upload file {1} (By {0})")
    public static void uploadFile(By by, String filePath) {
        sleep(STEP_TIME);
        getWebElement(by).sendKeys(filePath);
        logConsole("Upload file: " + filePath + " → " + by);
    }

    @Step("Get text (By) {0}")
    public static String getElementText(By by) {
        waitForElementVisible(by);
        String text = getWebElement(by).getText();
        logConsole("TEXT: " + text);
        AllureManager.saveTextLog("TEXT: " + text);
        return text;
    }

    @Step("Get attribute {1} (By {0})")
    public static String getElementAttribute(By by, String attributeName) {
        waitForElementVisible(by);
        String value = getWebElement(by).getAttribute(attributeName);
        logConsole(attributeName + ": " + value);
        AllureManager.saveTextLog(attributeName + ": " + value);
        return value;
    }

    // ================== TEXT / ATTRIBUTE (WEBELEMENT) ==================
    @Step("Clear text (WebElement)")
    public static void clearTextWithKey(WebElement element) {
        waitForElementVisible(element);
        sleep(STEP_TIME);
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.DELETE);
        logConsole("Clear text → " + safeDescribe(element));
    }

    @Step("Set text {1} (WebElement)")
    public static void setText(WebElement element, String value) {
        waitForElementVisible(element);
        sleep(STEP_TIME);
        element.sendKeys(value);
        logConsole("Set text \"" + value + "\" → " + safeDescribe(element));
    }

    @Step("Upload file {1} (WebElement)")
    public static void uploadFile(WebElement element, String filePath) {
        sleep(STEP_TIME);
        element.sendKeys(filePath);
        logConsole("Upload file: " + filePath + " → " + safeDescribe(element));
    }

    @Step("Get text (WebElement)")
    public static String getElementText(WebElement element) {
        waitForElementVisible(element);
        String text = element.getText();
        logConsole("TEXT: " + text);
        AllureManager.saveTextLog("TEXT: " + text);
        return text;
    }

    @Step("Get attribute {1} (WebElement)")
    public static String getElementAttribute(WebElement element, String attributeName) {
        waitForElementVisible(element);
        String value = element.getAttribute(attributeName);
        logConsole(attributeName + ": " + value);
        AllureManager.saveTextLog(attributeName + ": " + value);
        return value;
    }

    // ================== HOVER ==================
    public static void hoverMouse(List<WebElement> elements, int index) {
        try {
            sleep(STEP_TIME);
            WebElement element = elements.get(index);
            new Actions(DriverManager.getDriver()).moveToElement(element).perform();
            logConsole("Hover → " + element.getText());
        } catch (Exception e) {
            logConsole("Error hover: " + e.getMessage());
            Assert.fail("Error hover: " + e.getMessage());
        }
    }

    public static void hoverMouse(WebElement element) {
        try {
            sleep(STEP_TIME);
            new Actions(DriverManager.getDriver()).moveToElement(element).perform();
            logConsole("Hover → " + element.getText());
        } catch (Exception e) {
            logConsole("Error hover: " + e.getMessage());
            Assert.fail("Error hover: " + e.getMessage());
        }
    }

    @Step("Hover (By) {0}")
    public static void hoverMouse(By by) {
        try {
            waitForElementVisible(by);
            new Actions(DriverManager.getDriver()).moveToElement(getWebElement(by)).perform();
            logConsole("Hover → " + by);
        } catch (Exception e) {
            logConsole("Error hover " + by + ": " + e.getMessage());
            Assert.fail("Error hover " + by + ": " + e.getMessage());
        }
    }

    // ================== STATE CHECKS ==================
    public static boolean isElementDisplayed(By by) {
        try { waitForElementVisible(by); return getWebElement(by).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public static boolean isElementDisplayed(WebElement element) {
        try { waitForElementVisible(element); return element.isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public static boolean isElementSelected(By by) {
        try { return getWebElement(by).isSelected(); }
        catch (Exception e) { return false; }
    }

    public static boolean isElementEnabled(By by) {
        try { waitForElementVisible(by); return getWebElement(by).isEnabled(); }
        catch (Exception e) { return false; }
    }

    public static boolean isElementEnabled(WebElement element) {
        try { waitForElementVisible(element); return element.isEnabled(); }
        catch (Exception e) { return false; }
    }

    // ================== SLIDER ==================
    @Step("Set slider to {1}")
    public static void setValueToSlider(WebElement element, String value) {
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].value='" + Integer.parseInt(value) + "';", element);
        logConsole("Set slider → " + value);
    }

    // ================== VERIFY (BY) ==================
    public static void verifySelect(By by, boolean check, String message) {
        String text = getWebElement(by).getText();
        String stepName = check
                ? "✅ PASS: Verify element [" + text + "] is selected"
                : "❌ FAIL: Verify element [" + text + "] is selected | " + message;
        if (!check) onStepFailWithScreenshot(by.toString());
        Allure.step(stepName);
        Assert.assertTrue(check, message);
        logConsole(stepName);
    }

    public static void softVerifySelect(By by, boolean check, String message) {
        String text = getWebElement(by).getText();
        String stepName = check
                ? "✅ PASS: Verify element [" + text + "] is selected"
                : "❌ FAIL: Verify element [" + text + "] is selected | " + message;
        if (!check) onStepFailWithScreenshot(by.toString());
        Allure.step(stepName);
        SoftAssertHelper.getSoftAssert().assertTrue(check, message);
        logConsole(stepName);
    }

    public static void verifyEqual(Object actual, Object expected, String message) {
        boolean isEqual = String.valueOf(actual).equals(String.valueOf(expected));
        String stepName = isEqual
                ? "✅ PASS: Verify equals | expected=[" + expected + "] and actual=[" + actual + "]"
                : "❌ FAIL: Verify equals | " + message + " | expected=[" + expected + "] but found=[" + actual + "]";
        if (!isEqual) onStepFailWithScreenshot(null);
        Allure.step(stepName);
        Assert.assertEquals(actual, expected, message);
        logConsole(stepName);
    }

    public static void softVerifyEqual(Object actual, Object expected, String message) {
        boolean isEqual = String.valueOf(actual).equals(String.valueOf(expected));
        String stepName = isEqual
                ? "✅ PASS: Verify equals | expected=[" + expected + "] and actual=[" + actual + "]"
                : "❌ FAIL: Verify equals | " + message + " | expected=[" + expected + "] but found=[" + actual + "]";
        if (!isEqual) onStepFailWithScreenshot(null);
        Allure.step(stepName);
        SoftAssertHelper.getSoftAssert().assertEquals(actual, expected, message);
        logConsole(stepName);
    }

    public static void verifyDisplay(By by, boolean check, String message) {
        String text = getWebElement(by).getText();
        String stepName = check
                ? "✅ PASS: Verify element [" + text + "] is displayed"
                : "❌ FAIL: Verify element [" + text + "] is displayed | " + message;
        if (!check) onStepFailWithScreenshot(by.toString());
        Allure.step(stepName);
        Assert.assertTrue(check, message);
        logConsole(stepName);
    }

    public static void verifyDisplay(WebElement element, boolean check, String message) {
        String text = element.getText();
        Assert.assertTrue(check, message);
        logConsole("Verify [" + text + "] is displayed");
    }

    public static void verifyNotDisplay(List<WebElement> elements, String elementName, String message) {
        boolean isNotDisplayed = elements.isEmpty();
        String stepName = isNotDisplayed
                ? "✅ PASS: Verify [" + elementName + "] is not displayed"
                : "❌ FAIL: Verify [" + elementName + "] is not displayed | " + message;
        if (!isNotDisplayed) onStepFailWithScreenshot(elementName);
        Allure.step(stepName);
        Assert.assertTrue(isNotDisplayed, message);
        logConsole(stepName);
    }

    // ================== VERIFY UPLOADED (BY) ==================
    public static void verifyImageUpLoaded(By by, String message) {
        waitForElementPresent(by);
        String src = getElementAttribute(by, "src");
        verifyResourceOK("image", src, message);
    }

    public static void verifyFileUpLoaded(By by, String message) {
        waitForElementPresent(by);
        String href = getElementAttribute(by, "href");
        verifyResourceOK("file", href, message);
    }

    // ================== SOFT ASSERT ALL ==================
    public static void assertAll() {
        SoftAssertHelper.getSoftAssert().assertAll();
        SoftAssertHelper.resetSoftAssert();
    }

    // ================== INTERNAL HELPERS ==================
    private static void onWaitFail(String state, String target, Throwable error) {
        try {
            if ("yes".equalsIgnoreCase(PropertiesHelper.getValue("SCREENSHOT"))) {
                CaptureHelper.takeScreenshotBrowser("failed");
            }
            AllureManager.saveScreenshotPNG();
        } catch (Exception ignored) { }
        String msg = "Timeout waiting for element " + state + ". " + target;
        logConsole(msg);
        Assert.fail(msg + " | " + error.getMessage());
    }

    private static void onStepFailWithScreenshot(String target) {
        try {
            if ("yes".equalsIgnoreCase(PropertiesHelper.getValue("SCREENSHOT"))) {
                CaptureHelper.takeScreenshotBrowser("failed");
            }
            AllureManager.saveScreenshotPNG();
        } catch (Exception ignored) { }
        if (target != null) logConsole("Fail at: " + target);
    }

    private static void verifyResourceOK(String type, String url, String message) {
        String stepName;
        if (url == null || url.isEmpty()) {
            stepName = "❌ FAIL: element has no URL for " + type;
            SoftAssertHelper.getSoftAssert().fail(message);
            Allure.step(stepName);
            logConsole(stepName);
            return;
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            if (connection.getResponseCode() >= 400) {
                stepName = "❌ FAIL: Verify " + type + " [" + url + "] is loaded | " + message;
                onStepFailWithScreenshot(url);
                SoftAssertHelper.getSoftAssert().fail(message);
            } else {
                stepName = "✅ PASS: Verify " + type + " [" + url + "] is loaded";
                SoftAssertHelper.getSoftAssert().assertTrue(true);
            }
        } catch (Exception e) {
            stepName = "❌ FAIL: Invalid URL: " + url + " | " + e.getMessage();
            SoftAssertHelper.getSoftAssert().fail(e.getMessage());
        }
        Allure.step(stepName);
        logConsole(stepName);
    }

    private static String safeDescribe(WebElement el) {
        try {
            String tag = el.getTagName();
            String id = el.getAttribute("id");
            String cls = el.getAttribute("class");
            String txt = el.getText();
            return String.format("<%s id='%s' class='%s' text='%s'>", tag, id, cls, txt);
        } catch (Exception e) {
            return el.toString();
        }
    }
}
