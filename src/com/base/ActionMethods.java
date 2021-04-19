package com.base;


import cucumber.api.Scenario;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Reporter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class ActionMethods  {
    private TestConfiguration testConfiguration;
    private String winHandleBefore;
    private WebDriver driver;
    private DriverManager driverManager;


    public ActionMethods (TestContext testContext) {
        driverManager=(DriverManager)testContext;
        driver=driverManager.getDriver();
    }
    protected Logger logger = Logger.getLogger(this.getClass().getName());

    public void windowMinimize() throws Exception {
        driver.manage().window().setPosition(new Point(-2000, 0));

    }

    private By findBy(By locator) throws Exception {
        try {
            logger.info("findBy method execution started");
            logger.info("findBy method execution completed");
            return locator;
        } catch (Exception e) {
            logger.error("findBy method execution failed: " + e.getMessage());
            throw e;
        }
    }
    public void quitDriver() {
        driver.quit();
    }

    public void closeDriver() {
        driver.close();
    }

    public void goToUrl(String url) throws Exception {
        try {
            logger.info("goToUrl method execution started");
            driver.get(url);
            waitForPageLoad();
            logger.info("goToUrl findBy method execution completed");
        } catch (Exception e) {
            logger.error("goToUrl method execution failed: " + e.getMessage());
            throw e;
        }
    }

    public void click(By locator) throws Exception {
        try {
            logger.info("Started Click method execution");
            driver.findElement(locator).click();
            logger.info("Successfully Clicked on Element - " + locator);
        } catch (Exception e) {
            logger.error("Failed to Click on Element - " + locator + e.getMessage());
            throw e;
        }
    }

    public void waitAndClick1(By locator) throws Exception {
        try {
            logger.info("Started waitAndClick method execution");
            for(int i=0;i<=30;i++)
            {
                try {
                    Thread.sleep(1000);
                    driver.findElement(this.findBy(locator)).isDisplayed();
                    break;
                } catch (Exception e) {
                }
            }

            driver.findElement(this.findBy(locator)).click();
            logger.info("Successfully Clicked on Element - " + locator);
        } catch (Exception e) {
            logger.error("Failed to Click on Element - " + locator + e.getMessage());
            throw e;
        }
    }

    public void waitAndClick(By locator) throws Exception {
        try {
            logger.info("Started waitAndClick method execution");
            try {
                waitOnlocator(locator);
            }catch (Exception e){}
            scrollDown(locator);
            driver.findElement(this.findBy(locator)).click();
            waitForPageLoad();
            logger.info("Successfully Clicked on Element - " + locator);
        } catch (Exception e) {
            logger.error("Failed to Click on Element - " + locator + e.getMessage());
            throw e;
        }
    }
    public void waitAndClickWithJavaScriptExecutor(By locator) throws Exception {
        try {
            try {
                waitOnlocator(locator);
            }
            catch (Exception e){}
            scrollDown(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", driver.findElement(findBy(locator)));
        } catch (Exception e) {
            logger.error("Failed to Click on Element - " + locator + e.getMessage());
            throw e;
        }
    }

    public int getXcoordinate(By locator) throws Exception {
        Point p = driver.findElement(findBy(locator)).getLocation();
        return p.getX();
    }

    public int getYcoordinate(By locator) throws Exception {
        Point p = driver.findElement(findBy(locator)).getLocation();
        return p.getY();
    }

    public void waitAndClickWithJavaScriptExecutor(WebElement locator) throws Exception {
        try {
            scrollDown(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", locator);
        } catch (Exception e) {
            logger.error("Failed to Click on Element - " +e.getMessage());
            throw e;
        }
    }
    public void scrollDown(By locator) throws Exception{
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(this.findBy(locator)));

    }

    public void scrollDown(WebElement locator) throws Exception{
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", locator);

    }

    public void fireChangeEvent(By locator) throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("$(arguments[0]).change();", driver.findElement(this.findBy(locator)));
    }


    public void clickRadioButton(By locator) throws Exception {
        WebElement element;
        try {
            logger.info("Started clickRadioButton method execution");
            element = driver.findElement(this.findBy(locator));
            if(!element.isSelected()) {
                element.click();
                logger.info("Successfully Clicked on Radio Button - " + locator);
            } else
                logger.info("Already clicked Radio Button - " + locator);
        } catch (Exception e) {
            logger.error("Failed to Click on Radio Button " + locator + e.getMessage());
            throw e;
        }
    }

    public void uncheckCheckbox(By locator) throws Exception {
        logger.info("Started uncheckCheckbox method execution");
        WebElement element = driver.findElement(this.findBy(locator));
        try {
            if (element.getAttribute("checked") != null) {
                element.click();
                logger.info("Successfully Unchecked Checkbox - " + locator);
            } else
                logger.info("Checkbox was allready Unchecked - " + locator);
        } catch (Exception e) {
            logger.error("Failed to Click on Element - " + locator + e.getMessage());
            throw e;
        }
    }

    public void checkCheckbox(By locator) throws Exception {
        logger.info("Started checkCheckbox method execution");
        WebElement element = driver.findElement(this.findBy(locator));
        try {
            if (element.getAttribute("checked") == null) {
                waitAndClickWithJavaScriptExecutor(locator);
                logger.info("Successfully checked Checkbox - " + locator);
            } else
                logger.info("Checkbox was already checked - " + locator);
        } catch (Exception e) {
            logger.error("Failed to Click on Element - " + locator + e.getMessage());
            throw e;
        }

    }

    public boolean isVerticalScrollBarDisplayed() throws Exception {
        try {
            logger.info("Started isVerticalScrollBarDisplayed method execution");
            JavascriptExecutor javascript = (JavascriptExecutor) driver;
            boolean VertscrollStatus = (boolean) javascript.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight;");
            logger.info("isVerticalScrollBarDisplayed method execution completed");
            return VertscrollStatus;
        } catch (Exception e) {
            logger.error("checkScrollBar method execution failed - " + e.getMessage());
            return false;
        }
    }

    public void enterValue(By locator, CharSequence... value) throws Exception {
        try {
            logger.info("Started enterValue method execution");
            waitForVisibilityOfElement(locator);
            scrollDown(locator);
            findElement(locator).sendKeys(value);
            logger.info("Successfully Entered" + value + "in Textbox - " + locator);
        } catch (Exception e) {
            logger.error("Failed to find Element - " + locator + "in enterValue method" + e.getMessage());
            throw e;
        }

    }

    public void enterValue(By locator, Keys keys) throws Exception {
        try {
            logger.info("Started enterValue method execution");
            waitForVisibilityOfElement(locator);
            driver.findElement(locator).sendKeys(keys);
            logger.info("Successfully Entered" + keys + "in Textbox - " + locator);
        } catch (Exception e) {
            logger.error("Failed to find Element - " + locator + "in enterValue method" + e.getMessage());
            throw e;
        }
    }

    public WebElement webElement(By locator) throws Exception{
        try {
            waitForVisibilityOfElement(locator);
            return driver.findElement(locator);
            } catch (Exception e) {
        logger.error("Failed to find Element - " + locator + "in enterValue method" + e.getMessage());
        throw e;
    }
 }

    public void waitForVisibilityOfElement(By locator) throws Exception {
        logger.info("Started waitForVisibilityOfElement method execution");
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(10, TimeUnit.SECONDS)
                .pollingEvery(200, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element is visible");
        } catch (Exception e) {
            logger.error("waitForVisibilityOfElement method failed: " + e.getMessage());
            throw e;
        }
    }

    public void waitForVisibilityOfElement(By locator,int timeinSec) throws Exception {
        logger.info("Started waitForVisibilityOfElement method execution");
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeinSec, TimeUnit.SECONDS)
                .pollingEvery(200, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element is visible");
        } catch (Exception e) {
            logger.error("waitForVisibilityOfElement method failed: " + e.getMessage());
            throw e;
        }
    }

    public void waitForPresenceOfElement(By locator,int timeinSec) throws Exception {
        logger.info("Started waitForVisibilityOfElement method execution");
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeinSec, TimeUnit.SECONDS)
                .pollingEvery(200, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            logger.info("Element is visible");
        } catch (Exception e) {
            logger.error("waitForVisibilityOfElement method failed: " + e.getMessage());
            throw e;
        }
    }

    public void waitForElementToBeClickable(By locator) throws Exception {
        logger.info("Started waitForVisibilityOfElement method execution");
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(50, TimeUnit.SECONDS)
                .pollingEvery(200, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(findBy(locator)));
            logger.info("Element is visible");
        } catch (Exception e) {
            logger.error("waitForVisibilityOfElement method failed: " + e.getMessage());
            throw e;
        }
    }

    public boolean isElementNotDisplayed(By locator) {
        try {
            logger.info("Started waitForVisibilityOfElement method execution");
            boolean elementState = findElement(locator).isDisplayed();
            if (elementState) {
                logger.info("Successfully verified Element is not Displayed - " + locator);
                return false;
            } else {
                logger.info("Element is Displayed - " + locator);
                return true;
            }
        } catch (Exception e) {
            logger.error("Failed , identify Element is present - " + locator + e.getMessage());
            return true;
        }

    }

    public boolean isElementDisplayed(By locator) {
        try {
            waitForVisibilityOfElement(locator,30);
            return driver.findElement(this.findBy(locator)).isDisplayed();
        } catch (Exception e) {
            logger.error("Failed to identify Element - " + locator + e.getMessage());
            return false;
        }
    }
    public boolean isElementDisplayed1(By locator) {
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            return driver.findElement(this.findBy(locator)).isDisplayed();
        }catch(Exception e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
        }
    }

    public void clearTextBox(By locator) throws Exception {
        try {
            logger.info("Started clearTextBox method execution");
            driver.findElement(findBy(locator)).clear();
            logger.info("Successfully Cleared value from Text box - " + locator);
        } catch (Exception e) {
            logger.error("failed to find the element - " + locator + e.getMessage());
            throw e;
        }
    }

    public boolean isElementEnabled(By locator) throws Exception {
        try {
            waitForVisibilityOfElement(locator);
            return findElement(locator).isEnabled();
        } catch (Exception e) {
            logger.error("Failed to identify Element - " + locator + e.getMessage());
            throw e;
        }
    }

    public boolean isElementDisabled(By locator) throws Exception {
        try {
            logger.info("Started isElementDisabled method execution");
            boolean elementState = findElement(locator).isEnabled();
            if (!elementState) {
                logger.info("Successfully Verified Element - " + locator + "is disabled.");
                return true;
            } else {
                logger.info("Successfully Verified Element - " + locator + "is enabled.");
                return false;
            }

        } catch (Exception e) {
            logger.error("Failed to Verify on Element - " + e.getMessage());
            throw e;
        }
    }

    public void waitForPageLoad() throws Exception{
        logger.info("Started waitForPageLoad method execution");
        Thread.sleep(5000);
        long timeoutInSeconds = 60;
        long sleepTimeInMilliSeconds = 200;
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };
        try {
            Thread.sleep(sleepTimeInMilliSeconds);
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
            wait.until(expectation);
            logger.info("Completed waitForPageLoad method execution");
        } catch (Exception e) {
            logger.error("Failed to Verify on Element - " + e.getMessage());
            throw e;
        }
    }

    public void moveToElement(By locator) throws Exception {
        try {
            logger.info("Started moveToElement method execution");
            Actions mouse = new Actions(driver);
            mouse.moveToElement(driver.findElement(this.findBy(locator))).perform();
            logger.info("Successfully moved to Element - " + locator);
        } catch (Exception e) {
            logger.error("Failed to move to Element - " + locator + e.getMessage());
            throw e;
        }
    }

    public void moveToElementAndClick(int x, int y) throws Exception {
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.tagName("body")), 0, 0);
        actions.moveByOffset(x,y).click().build().perform();
    }

    public void moveToElementAndClick(By locator) throws Exception {
        try {
            logger.info("Started moveToElementAndClick method execution");
            moveToElement(locator);
            new Actions(driver).click();
            logger.info("Successfully moved and clicked Element - " + locator);
        } catch (Exception e) {
            logger.error("Failed to move to Element - " + locator + e.getMessage());
            throw e;
        }
    }

    public void clickOnLinkText(String str) throws Exception {
        try {
            logger.info("Started clickOnLinkText method execution");
            WebElement element = driver.findElement(By.linkText(str));
            if (element.isDisplayed())
                element.click();
            else
                throw new Exception();
            logger.info("Successfully Clicked on link - " + str);
        } catch (Exception e) {
            logger.error("Failed to Clicked on link - " + str + e.getMessage());
            throw e;
        }
    }


    public String clickAndSwitchToWindow(By locator) throws Exception {
        try {
            logger.info("Started clickAndSwitchToWindow method execution");
            long sleepTimeInMilliSeconds = 5000;
            winHandleBefore = driver.getWindowHandle();
            waitAndClick(locator);
            Thread.sleep(sleepTimeInMilliSeconds);
            Set<String> windows = driver.getWindowHandles();
            for (String winHandle : windows) {
                if(!winHandle.equals(winHandleBefore)) {
                    driver.switchTo().window(winHandle);
                }
            }
            logger.info("Successfully Clicked on element - " + locator + "and Switched to window");
            return winHandleBefore;
        } catch (Exception e) {
            logger.error("Failed to switch to window - " + e.getMessage());
            throw e;
        }
    }

    public void closeCurrentWindow() throws Exception {
        try {
            logger.info("closeCurrentWindow method execution started");
            int countWindows= driver.getWindowHandles().size();
            driver.close();
            waitForWindowCountToBe(countWindows-1);
            logger.info("closeCurrentWindow method execution completed");
        } catch (Exception e) {
            logger.error("closeCurrentWindow method execution failed: " + e.getMessage());
            throw e;
        }

    }

    public void switchToWindow(String windowTitle) throws Exception {
        try {
            logger.info("Started switchToWindow method execution");
            Thread.sleep(20000);
            Set<String> winHandles = driver.getWindowHandles();
            boolean isSwitched = false;
            for (String winHandle : winHandles) {
                driver.switchTo().window(winHandle);
                String title = driver.getTitle();
                if (title.equals(windowTitle)) {
                    logger.info("successfully switched to the window: " + windowTitle);
                    isSwitched = true;
                    break;
                }
            }
            if(!isSwitched) {
                throw new Exception("Unable to switch to new window. New window may not be loaded properly");
            }
            logger.info("Successfully Switched to window - " + windowTitle);
        } catch (Exception e) {
            logger.error("Failed to switch to window - " + windowTitle + e.getMessage());
            throw e;
        }
    }

    public void switchToWindowAndClose(String windowTitle) throws Exception {
        try {
            logger.info("Started switchToWindowAndClose method execution");
            Set<String> windows = driver.getWindowHandles();
            for (String winHandle : windows) {
                driver.switchTo().window(winHandle);
                if (driver.getTitle().contains(windowTitle)) {
                    driver.close();
                    break;
                }
            }
            logger.info("Successfully switched to window and closed - " + windowTitle);
        } catch (Exception e) {
            logger.error("Failed to switched to window and closeCurrentWindow - " + windowTitle + e.getMessage());
            throw e;
        }
    }

    public void switchToFrame() throws Exception {
        try {
            logger.info("Started switchToFrame method execution");
            driver.switchTo().activeElement();
            logger.info("Successfully switched to frame");
        } catch (Exception e) {
            logger.error("Failed to switched to window and closeCurrentWindow - " + e.getMessage());
            throw e;
        }
    }

    public void switchToFrameByID(By locator) throws Exception {
        try {
            logger.info("Started switchToFrame method execution");
            WebElement fr = driver.findElement(findBy(locator));
            driver.switchTo().frame(fr);
            logger.info("Successfully switched to frame");
        } catch (Exception e) {
            logger.error("Failed to switched to window and closeCurrentWindow - " + e.getMessage());
            throw e;
        }
    }

    public void switchToActiveElement() throws Exception {
        try {
            logger.info("Started switchToFrame method execution");
            driver.switchTo().activeElement();
            logger.info("Successfully Activated popup");
        } catch (Exception e) {
            logger.error("Failed to switched to window and closeCurrentWindow - " + e.getMessage());
            throw e;
        }
    }

    public Select findSelect(By locator) throws Exception {
        try {
            logger.info("findSelect method execution started");
            WebElement listElement = driver.findElement(findBy(locator));
            Select select = new Select(listElement);
            logger.info("findSelect method execution completed");
            return select;
        } catch (Exception e) {
            logger.error("findSelect method execution failed: " + e.getMessage());
            throw e;
        }
    }

    public void selectByValue(By locator, String value) throws Exception {
        try {
            logger.info("Started selectByValue method execution");
            Select select = findSelect(locator);
            select.selectByValue(value);
            logger.info("Successfully Selected value - " + value + " from element " + locator);
        } catch (Exception e) {
            logger.error("Failed to Select value - " + value + " from element " + locator + e.getMessage());
            throw e;
        }
    }

    public void selectByVisibleText(By locator, String value) throws Exception {
        try {
            logger.info("Started selectByVisibleText method execution");
            click(locator);
            Thread.sleep(2000);
            Select select = findSelect(locator);
            Thread.sleep(2000);
            select.selectByVisibleText(value);
            Thread.sleep(2000);
            logger.info("Successfully Selected value - " + value + " from element " + locator);
        } catch (Exception e) {
            logger.error("Failed to Select value - " + value + " from element " + locator + e.getMessage());
            throw e;
        } finally {
            if(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser").equalsIgnoreCase("android")) {
                click(locator);
                Thread.sleep(2000);
            }
        }
    }
    public void waitForPageLoad(WebDriver driver1) throws Exception{
        logger.info("Started waitForPageLoad method execution");
        Thread.sleep(5000);
        long timeoutInSeconds = 60;
        long sleepTimeInMilliSeconds = 200;
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };
        try {
            Thread.sleep(sleepTimeInMilliSeconds);
            WebDriverWait wait = new WebDriverWait(driver1, timeoutInSeconds);
            wait.until(expectation);
            logger.info("Completed waitForPageLoad method execution");
        } catch (Exception e) {
            logger.error("Failed to Verify on Element - " + e.getMessage());
            throw e;
        }
    }

    public boolean waitOnlocator(WebElement locator, WebDriver driver) throws Exception {
        try {
            logger.info("Started waitOnlocator method execution");
            long timeoutInSeconds = 50;
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            if (element != null) {
                logger.info("Completed waitOnlocator method execution");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed  method" + e.getMessage());
            throw e;
        }
    }

    public void selectByIndex(By locator, int index) throws Exception {
        try {
            logger.info("Started selectByIndex method execution");
            Select select = findSelect(locator);
            select.selectByIndex(index);
            logger.info("Successfully Selected value at index - " + index + " from element " + locator);
        } catch (Exception e) {
            logger.error("Failed to select value at index - " + index + " from element " + locator + e.getMessage());
            throw e;
        }
    }

    public String getText(By locator) throws Exception {
        try {
            logger.info("Started getText method execution");
            waitForVisibilityOfElement(locator);
            scrollDown(locator);
            String elementText = driver.findElement(this.findBy(locator)).getText().trim();
            logger.info("Successfully returned text - " + elementText + " from element " + locator);
            return elementText;
        } catch (Exception e) {
            logger.error("Failed to get text from element - " + locator + e.getMessage());
            throw e;
        }
    }

    public String getTextAfterSwitchingToDefaultContent(By locator) throws Exception {
        try {
            logger.info("Started getText method execution");
            driver.switchTo().defaultContent();
            Thread.sleep(5000);
            waitForVisibilityOfElement(locator);
            String elementText = driver.findElement(this.findBy(locator)).getText();
            logger.info("Successfully returned text - " + elementText + " from element " + locator);
            return elementText;
        } catch (Exception e) {
            logger.error("Failed to get text from element - " + locator + e.getMessage());
            throw e;
        }
    }

    public boolean isElementSelected(By locator) throws Exception {
        try {
            logger.info("Started isElementSelected method execution");
            waitForVisibilityOfElement(locator);
            boolean isSelected = findElement(locator).isSelected();
            logger.info("Successfully returned value - " + isSelected + " from element " + locator);
            return isSelected;
        } catch (Exception e) {
            logger.error("Failed to get value from element - " + locator + e.getMessage());
            throw e;
        }
    }

    public void clearAndEnterValue(By locator, String strg) throws Exception {
        try {
            logger.info("Started clearAndEnterValue method execution");
            WebElement element = driver.findElement(this.findBy(locator));
            element.clear();
            element.sendKeys(strg);
            logger.info("Successfully set text - " + strg + " to element " + locator);
        } catch (Exception e) {
            logger.error("Failed to set text - " + strg + " to element " + locator + e.getMessage());
            throw e;
        }
    }

    public void setTextUsingJS(By locator, String strg) throws Exception {
        try {
            WebElement element = driver.findElement(this.findBy(locator));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].setAttribute('value', '" + strg +"')", element);

        } catch (Exception e) {
            logger.error("Failed to set text - " + strg + " to element " + locator + e.getMessage());
            throw e;
        }
    }

    public String getPageTitle() {
        logger.info("Started getPageTitle method execution");
        String title = driver.getTitle();
        logger.info("Successfully returned page title - " + title);
        return title;
    }

    public void acceptAlert() throws Exception {
        try {
            logger.info("Started acceptAlert method execution");
            Alert alert = driver.switchTo().alert();
            alert.accept();
            logger.info("Successfully clicked OK on Alert.");
        } catch (Exception e) {
            logger.error("Failed to click OK on Alert - " + e.getMessage());
            throw e;
        }
    }

    public String getTextOfAlert() throws Exception {
        try {
            logger.info("Started getTextOfAlert method execution");
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            logger.info("Successfully get alert text - " + alertText);
            return alertText;
        } catch (Exception e) {
            logger.error("Failed to get text from Alert - " + e.getMessage());
            throw e;
        }
    }

    public void switchBackToDefaultWindow() throws Exception {
        try {
            logger.info("Started switchBackToDefaultWindow method execution");
            driver.switchTo().defaultContent();
            logger.info("Successfully switched to default content");
        } catch (Exception e) {
            logger.error("Failed to switch to default content - " + e.getMessage());
            throw e;
        }
    }

    public List<WebElement> findElements(By locators) throws Exception {
        try {
            logger.info("Started findElements method execution");
            return driver.findElements(this.findBy(locators));
        } catch (Exception e) {
            logger.error("Failed to find elements: " + e.getMessage());
            throw e;
        }
    }

    public void dismissAlert() throws Exception {
        try {
            logger.info("Started closeWindowsSecurityPopup method execution");
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
            logger.info("Successfully closed Windows Security Popup");
        } catch (Exception e) {
            logger.error("Failed to switch to default content - " + e.getMessage());
            throw e;
        }
    }

    public boolean waitOnlocator(By locator) throws Exception {
        try {
            logger.info("Started waitOnlocator method execution");
            long timeoutInSeconds = 50;
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(this.findBy(locator)));
            if (element != null) {
                logger.info("Completed waitOnlocator method execution");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed  method" + e.getMessage());
            throw e;
        }
    }

    public String getSelectedOption(By locator) throws Exception {
        try {
            logger.info("Started getSelectedOption method execution");
            Select select = findSelect(locator);
            String selectedOption = select.getFirstSelectedOption().getText();
            logger.info("Successfully retured selected option - " + selectedOption + " from " + locator);
            return selectedOption;

        } catch (Exception e) {
            logger.error("Failed to return selected option from " + locator + e.getMessage());
            throw e;
        }
    }

    public String getEnteredText(By locator) throws Exception {
        try {
            logger.info("Started getTextEntered method execution");
            String enteredText = driver.findElement(findBy(locator)).getAttribute("value");
            logger.info("Successfully retured selected option - " + enteredText + " from " + locator);
            return enteredText;

        } catch (Exception e) {
            logger.error("Failed to return selected option from " + locator + e.getMessage());
            throw e;
        }
    }

    public boolean isAlertPresent() throws Exception {
        try {
            logger.info("Started isAlertPresent method execution");
            long timeoutInSeconds = 7;
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            logger.info("No alert");
            return false;
        }
    }

    public String getAttributeValue(By locator, String attributeName) throws Exception {
        try {
            logger.info("Started getAttributeValue method execution");
            String getAttributeName = driver.findElement(this.findBy(locator)).getAttribute(attributeName);
            logger.info("Successfully returned selected option - " + getAttributeName + " from " + locator);
            return getAttributeName;
        } catch (Exception e) {
            logger.error("Failed to return selected option - " + locator + e.getMessage());
            throw e;
        }
    }

    public int[] getElementCoordinates(By locator) throws Exception {
        try {
            logger.info("Started getCoordinates method execution");
            Point coordinate = driver.findElement(this.findBy(locator)).getLocation();
            int xCordinate = coordinate.getX();
            int yCordinate = coordinate.getY();
            int[] coordinates = new int[2];
            coordinates[0] = xCordinate;
            coordinates[1] = yCordinate;
            logger.info("Successfully returned  the X and Y coordinates of the Element" + "X coordinate = "+xCordinate +" and yCoordinate = "+ yCordinate );
            return coordinates;
        } catch (Exception e) {
            logger.error("Failed to return the X and Y coordinate " + locator + e.getMessage());
            throw e;
        }
    }

    public int[] getElementSize(By locator) throws Exception {
        try {
            logger.info("Started getElementSize method execution");
            Dimension sizeOfElement = driver.findElement(this.findBy(locator)).getSize();
            int[] elementSize = new int[2];
            elementSize[0] = sizeOfElement.getHeight();
            elementSize[1] = sizeOfElement.getWidth();
            logger.info("Successfully returned element height and width" + "height = " +elementSize[0] +"Width = " + elementSize[1]);
            return elementSize;
        } catch (Exception e) {
            logger.error("Failed to return element height and width" + locator + e.getMessage());
            throw e;
        }
    }

    public boolean waitForInvisibilityOfElement(By locator) throws Exception {
        logger.info("Started waitForInvisibilityOfElement method execution");
        long timeoutInSeconds = 20;
        long pollingTimeoutInMilliSeconds = 200;
        FluentWait<WebDriver> wait = new FluentWait<>(driver).withTimeout(timeoutInSeconds, TimeUnit.SECONDS).pollingEvery(pollingTimeoutInMilliSeconds, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(findBy(locator)));
            logger.info("Successfully wait for invisibility of element - " + locator);
            return true;
        } catch (Exception e) {
            logger.error("Failed to wait on element - " + locator + e.getMessage());
            throw e;
        }
    }

    public boolean waitForInvisibilityOfElement(By locator, long timeOut) throws Exception {
        logger.info("Started waitForInvisibilityOfElement method execution");
        try {
            WebDriverWait wd = new WebDriverWait(driver,timeOut);
            wd.until(ExpectedConditions.invisibilityOfElementLocated(findBy(locator)));
            return true;
        } catch (Exception e) {
            logger.error("Failed to wait on element - " + locator + e.getMessage());
            return false;

        }
    }
    public boolean waitForInvisibilityOfElement1(By locator, long timeOut) throws Exception {
        logger.info("Started waitForInvisibilityOfElement method execution");
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            WebDriverWait wd = new WebDriverWait(driver, timeOut);
            wd.until(ExpectedConditions.invisibilityOfElementLocated(findBy(locator)));
            return true;
        } catch (Exception e) {
            logger.error("Failed to wait on element - " + locator + e.getMessage());
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
        }
    }

    public List<WebElement> getDropdownOptions(By locator) throws Exception {
        try {
            Select select = findSelect(locator);
            return select.getOptions();
        } catch (Exception e) {
            logger.error("Failed to get dropdown values - " + locator + " " + e.getMessage());
            throw e;
        }

    }

    public List<String> getDropdownValues(By locator) throws Exception {
        try {
            logger.info("Started getDropdownValues method execution");
            List<String> options = new ArrayList<>();
            List<WebElement> optionList = getDropdownOptions(locator);
            for (WebElement option : optionList) {
                options.add(option.getText());
            }
            logger.info("Completed getDropdownValues method execution");
            return options;
        } catch (Exception e) {
            logger.error("Failed to get dropdown values - " + locator + " " + e.getMessage());
            throw e;
        }
    }

    public void enterKeyUsingRobot() throws Exception {
        logger.info("Started enterKeyUsingRobot method execution");
        Robot robot;
        int milliSeconds = 400;
        try {
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.delay(milliSeconds);
            logger.info("Successfully pressed Enter Key from keyboard");
        } catch (Exception e) {
            logger.error("Failed to execute enterKeyUsingRobot method - " + e.getMessage());
            throw e;
        }
    }

    public WebElement findElement(By locator) throws Exception {
        try {
            return driver.findElement(this.findBy(locator));
        } catch (Exception e) {
            logger.error("findElement method executino failed: " + e.getMessage());
            throw e;
        }
    }

    //***** Trigger a change event on textbox. Sometimes textbox doesn't hold the value entered by webxriver, in that case we need to use this method *****
    public void triggerChangeEventOnTextbox(By locator) throws Exception {
        try {
            logger.info("Started triggerChangeEventOnTextbox method execution");
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("$(arguments[0]).change();", findElement(locator));
            logger.info("Completed triggerChangeEventOnTextbox method execution");
        } catch (Exception e) {
            logger.error("Failed to execute triggerChangeEventOnTextbox method - " + e.getMessage());
            throw e;
        }
    }

    public String getCssValue(By locator, String cssAttribute) throws Exception {
        try {
            return findElement(locator).getCssValue(cssAttribute);
        } catch (Exception e) {
            logger.error("getCssValue method execution failed: " + e.getMessage());
            throw e;
        }
    }


    public void clickUsingJavaScriptExcuter1(By locator) throws Exception {
        try {
            logger.info("Started clickUsingJavaScriptExcuter method execution");
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].click();", findElement(locator));
            logger.info("Successfully clicked on element");
        } catch (Exception e) {
            logger.error("Failed to switch to default content - " + e.getMessage());
            throw e;
        }
    }

    public boolean waitForAlert() throws Exception {
        try {
            logger.info("Started isAlertPresent method execution");
            long timeoutInSeconds = 50;
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            logger.info("waitForAlert method failed: "+e.getMessage());
            throw e;
        }
    }


    public boolean waitForWindowCountToBe(int count) throws Exception {
        try {
            logger.info("Started isAlertPresent method execution");
            long timeoutInSeconds = 50;
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
            wait.until(ExpectedConditions.numberOfWindowsToBe(count));
            return true;
        } catch (Exception e) {
            logger.info("waitForAlert method failed: "+e.getMessage());
            throw e;
        }
    }


    public void switchToWindowS(String windowTitle) throws Exception {
        try {
            logger.info("Started switchToWindow method execution");
            String windowHandle = driver.getWindowHandle();
            Set<String> winHandles = driver.getWindowHandles();
            winHandles.remove(windowHandle);
            for (String winHandle : winHandles) {
                driver.switchTo().window(winHandle);
                if (driver.getTitle().contains(windowTitle))
                    break;
            }
            logger.info("Successfully Switched to window - " + windowTitle);
        } catch (Exception e) {
            logger.error("Failed to switch to window - " + windowTitle + e.getMessage());
            throw e;
        }
    }

    public boolean waitForFrameToBeAvailableAndSwitchToIt(By locator) throws Exception {
        logger.info("Started waitForInvisibilityOfElement method execution");
        long timeoutInSeconds = 50;
        long pollingTimeoutInMilliSeconds = 300;
        FluentWait<WebDriver> wait = new FluentWait<>(driver).withTimeout(timeoutInSeconds, TimeUnit.SECONDS).pollingEvery(pollingTimeoutInMilliSeconds, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(findBy(locator)));
            logger.info("Successfully wait for invisibility of element - " + locator);
            return true;
        } catch (Exception e) {
            logger.error("Failed to wait on element - " + locator + e.getMessage());
            throw e;
        }
    }

    public String getTagName(By locator) throws Exception {
        try {
            return findElement(locator).getTagName();
        } catch (Exception e) {
            logger.error("getTagName method execution failed: "+ e.getMessage());
            throw  e;
        }
    }

    public String getPageUrl() throws Exception {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            logger.error("getPageUrl method execution failed: "+ e.getMessage());
            throw  e;
        }
    }

    public void presenceOfElementLocated(By locator) throws Exception {
        logger.info("Started waitForInvisibilityOfElement method execution");
        long timeoutInSeconds = 50;
        long pollingTimeoutInMilliSeconds = 300;
        FluentWait<WebDriver> wait = new FluentWait<>(driver).withTimeout(timeoutInSeconds, TimeUnit.SECONDS).pollingEvery(pollingTimeoutInMilliSeconds, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(findBy(locator)));
            logger.info("Successfully wait for invisibility of element - " + locator);
        } catch (Exception e) {
            logger.error("Failed to wait on element - " + locator + e.getMessage());
            throw e;
        }
    }



    public void captureScreenshot (Scenario scenario,String udid){
        try {
                byte[] exception = (byte[]) ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.embed(exception, "image/jpeg");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Object> captureXYCoordinatesOfEndCallBtn(){
        Point p = driver.findElement(By.xpath("(//label[text()='中断']//preceding::button)[5]")).getLocation();
        int x_coordinate = p.getX();
        int y_coordinate = p.getY();
        ArrayList<Object> al = new ArrayList<Object>();
        al.add(x_coordinate);
        al.add(y_coordinate);
        return al;
    }

    public void performClickOperation(int x, int y) {
        new Actions(driver).moveByOffset(x, y).click().build().perform();
    }
    public void openNewWindowInWeb(String windowUrl) throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.open('"+windowUrl+"')");
    }

    public void switchToDefaultWindow(){
        driver.switchTo().window(winHandleBefore);
        driver.switchTo().defaultContent();
    }

}