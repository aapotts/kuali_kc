/*
 * Copyright 2005-2010 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.test.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;
import org.kuali.kra.test.infrastructure.lifecycle.KcUnitTestSeleniumLifecycle;
import org.kuali.rice.test.web.HtmlUnitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.SystemClock;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

@RunWith(KcSeleniumTestRunner.class)
public class KcSeleniumTestBase extends KcUnitTestBase {
    
    protected static final String QUICKSTART_USER = "quickstart";
    
    protected static WebDriver driver;
    
    private static KcUnitTestSeleniumLifecycle LIFECYCLE = new KcUnitTestSeleniumLifecycle();
    private static RunListener RUN_LISTENER = new KcUnitTestRunListener(LIFECYCLE);
    
    private static final String BROWSER_PROTOCOL = "http";
    private static final String BROWSER_ADDRESS = "127.0.0.1";
    private static final String PORTAL_ADDRESS = "kc-dev/portal.jsp";
    private static final String TIMEOUT = "60000";
    
    private static final String RESEARCHER_TAB_TITLE = "Researcher";
    private static final String UNIT_TAB_TITLE = "Unit";
    private static final String CENTRAL_ADMIN_TAB_TITLE = "Central Admin";
    private static final String MAINTENANCE_TAB_TITLE = "Maintenance";
    private static final String SYSTEM_ADMIN_TAB_TITLE = "System Admin";
    
    private static final String HELP_PAGE_TITLE = "Kuali Research Administration Online Help";
    
    private static final String DOT = ".";
    private static final String METHOD_TO_CALL_PREFIX = "methodToCall";
    private static final String SAVE_BUTTON = METHOD_TO_CALL_PREFIX + DOT + "save";
    private static final String RELOAD_BUTTON = METHOD_TO_CALL_PREFIX + DOT + "reload";
    private static final String ROUTE_BUTTON = METHOD_TO_CALL_PREFIX + DOT + "route";
    private static final String CLOSE_BUTTON = METHOD_TO_CALL_PREFIX + DOT + "close";
    
    private static final String ERRORS_FOUND_ON_PAGE = "error(s) found on page";
    private static final String SAVE_SUCCESS_MESSAGE = "Document was successfully saved";
    
    @BeforeClass
    public static final void seleniumBeforeClass() {
        if (!LIFECYCLE.isPerSuiteStarted()) {
            LIFECYCLE.startPerSuite();
            driver = LIFECYCLE.getWebDriver();
        }
        LIFECYCLE.startPerClass();
    }
    
    @AfterClass
    public static final void seleniumAfterClass() {
        LIFECYCLE.stopPerClass();
    }
    
    @Before
    public void seleniumBeforeTest() {
        LIFECYCLE.startPerTest(transactional);
    }
    
    @After
    public void seleniumAfterTest() {
        LIFECYCLE.stopPerTest();
    }
    
    /**
     * This method returns the {@code RunListener} needed to ensure the KC persistent lifecycles shut down properly
     * @return the RunListener responsible for shutting down all KC persistent lifecycles
     */
    public static RunListener getRunListener() {
        return RUN_LISTENER;
    }
    
    /**
     * Returns the timeout value to wait for a page to load.  May be overridden by subclasses.
     * @return the timeout value for page loads
     */
    protected String getTimeoutValue() {
        return TIMEOUT;
    }
    
    /**
     * Checks for the Login web page and if it exists, logs the user in.
     */
    protected final void login() {
        login(QUICKSTART_USER);
    }
    
    /**
     * Checks for the Login web page and if it exists, logs the user in.
     * 
     * @param username the user's id
     */
    protected final void login(final String username) {
        if (StringUtils.equals(driver.getTitle(), "Login")) {
            set("__login_user", username);
            click("//input[@value='Login']");
        }
    }
    
    /**
     * Checks for the Logout button and if it exists, logs the current user out.
     */
    protected void logout() {
        click("imageField");
    }
    
    /**
     * Clicks the Researcher tab.
     */
    protected final void clickResearcherTab() {
        openPortalPage();
        
        click(RESEARCHER_TAB_TITLE);
    }
    
    /**
     * Clicks the Unit tab.
     */
    protected final void clickUnitTab() {
        openPortalPage();
        
        click(UNIT_TAB_TITLE);
    }
    
    /**
     * Clicks the Central Admin tab.
     */
    protected final void clickCentralAdminTab() {
        openPortalPage();
        
        click(CENTRAL_ADMIN_TAB_TITLE);
    }
    
    /**
     * Clicks the Maintenance tab.
     */
    protected final void clickMaintenanceTab() {
        openPortalPage();
        
        click(MAINTENANCE_TAB_TITLE);
    }
    
    /**
     * Click the System Admin tab.
     */
    protected final void clickSystemAdminTab() {
        openPortalPage();
        
        click(SYSTEM_ADMIN_TAB_TITLE);
    }
    
    /**
     * Click the Expand All button.
     */
    protected final void clickExpandAll() {
        click("methodToCall.showAllTabs");
    }
    
    /**
     * Click the Collapse All button.
     */
    protected final void clickCollapseAll() {
        click("methodToCall.hideAllTabs");
    }

    /**
     * Gets the value of a control field.
     *
     * @param locator the id, partial name, partial title, or partial link name of the element to click on
     * @return the value of the element
     */
    protected final String get(final String locator) {
        return getElement(locator).getValue();
    }
    
    /**
     * Sets the value of a control field.
     *
     * @param locator the id, partial name, partial title, or partial link name of the element to click on
     * @param value the new value of the element
     */
    protected final void set(final String locator, final String value) {
        WebElement element = getElement(locator);
        String elementType = element.getAttribute("type");
        
        if (StringUtils.equals(elementType, "radio")) {
            WebElement radio = new ElementExistsWaiter(locator + " with value " + value + " not found").until(
                new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        WebElement element = null;
                        
                        for (WebElement radio : getElementsByName(locator)) {
                            String radioValue = radio.getValue();
                            if (StringUtils.equals(radioValue, value)) {
                                element = radio;
                                break;
                            }
                        }
                        
                        return element;
                    }
                }
            );
            
            radio.click();
        } else {
            element.clear();
            element.sendKeys(value);
        }
        
    }

    /**
     * Clicks on an element in the web page.
     * <p>
     * Using any of the {@code click()} methods is the preferred way to click on an element due to the login process.  If the login web page is 
     * encountered, the user will be automatically logged in and the given button will be clicked.
     *
     * @param locator the id, partial name, partial title, or partial link name of the element to click on
     */
    protected final void click(final String locator) {
        click(locator, null);
    }
    
    /**
     * Clicks on an element in the web page, asserting that the next page contains {@code nextPageTitle}.
     * <p>
     * Using any of the {@code click()} methods is the preferred way to click on an HTML element due to the login process.  If the login web page is 
     * encountered, the user will be automatically logged in and the given button will be clicked.
     *
     * @param locator the id, partial name, partial title, or partial link name of the element to click on
     * @param nextPageTitle the expected title of the next web page (may be null)
     */
    protected final void click(final String locator, final String nextPageTitle) {
        getElement(locator).click();
        
        login();
        
        if (nextPageTitle != null) {
            assertPageContains(nextPageTitle);
        }
    }
    
    /**
     * Opens the tab with index {@code index} on the web page.  The {@code index} should be a number between {@code 0} and the number of active 
     * tabs on the page.  It does not count inactive hidden tabs on the page.
     *
     * @param index the index of the tab on the web page
     */
    protected final void openTab(final int index) {
        WebElement tab = new ElementExistsWaiter("Tab with index " + index + " not found on page").until(
            new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    WebElement tab = null;
                    
                    List<WebElement> tabs = getElementsByName("methodToCall.toggleTab");
                    if (0 <= index && index < tabs.size()) {
                        tab = tabs.get(index);
                    }
                    
                    return tab;
                }
            }
        );
        
        clickTab(tab, "open");
    }
    
    /**
     * Closes the tab with index {@code index} on the web page.  The {@code index} should be a number between {@code 0} and the number of active 
     * tabs on the page.  It does not count inactive hidden tabs on the page.
     *
     * @param index the index of the tab on the web page
     */
    protected final void closeTab(final int index) {
        WebElement tab = new ElementExistsWaiter("Tab with index " + index + " not found on page").until(
            new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    WebElement tab = null;
                    
                    List<WebElement> tabs = getElementsByName("methodToCall.toggleTab");
                    if (0 <= index && index < tabs.size()) {
                        tab = tabs.get(index);
                    }
                    
                    return tab;
                }
            }
        );

        clickTab(tab, "close");
    }
    
    /**
     * Clicks the {@code tab} that contains the text {@code command} (typically 'open' or 'close').
     * 
     * @param tab the tab to click
     * @param command the instruction to either open or close the tab
     */
    private void clickTab(final WebElement tab, final String command) {
        String tabTitle = tab.getAttribute("title");
        
        if (StringUtils.contains(tabTitle, command)) {
            tab.click();
        }
    }
    
    /**
     * Gets the document number from a document's web page.  It is expected to be in an HTML table in a table labeled "headerarea".
     *
     * @return the document's number
     */
    protected final String getDocumentNumber() {
        final String locator = "//div[@id='headerarea']/div/table/tbody/tr[1]/td[1]";
        
        WebElement documentNumber = new ElementExistsWaiter(locator + " not found").until(
            new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return getElementByXPath(locator);
                }
            }
        );
        
        return documentNumber.getText();
    }
    
    /**
     * Do a document search looking for the a specific document based upon its document number.  The following occurs on a Document Search:
     * <ol>
     * <li>The Portal Page is opened</li>
     * <li>The Doc Search button is clicked on</li>
     * <li>In the Doc Search web page, the document number is filled in with the given value</li>
     * <li>The first item in the results is returned</li>
     * <li>The document number link is clicked on</li>
     * </ol>
     *
     * @param documentNumber the document number to search for
     */
    protected final void docSearch(final String documentNumber) {
        openPortalPage();
        
        click("Document Search");
        
        set("routeHeaderId", documentNumber);
        
        click("methodToCall.search");
        
        click(documentNumber);
    }
    
    /**
     * Performs a single value Lookup.  The following occurs on a Lookup:
     * <ol>
     * <li>The Lookup button is clicked on</li>
     * <li>In the Lookup web page, the search button is clicked on</li>
     * <li>The first item in the results is returned</li>
     * <li>The web page resulting from clicking on "Return Value" is returned</li>
     * </ol>
     * To find the Lookup button, the name attribute of all Lookup buttons are examined to see if it contains the given {@code tag}.  Make sure to pick 
     * a {@code tag} that is unique for that Lookup.
     * <p>
     * Using any of the {@code lookup()} methods is the preferred way to perform a lookup due to the login process.  If the login web page is 
     * encountered, the user will be automatically logged in and the lookup will be performed.
     *
     * @param tag identifies the Lookup button to click on
     */
    protected final void lookup(final String tag) {
       lookup(tag, null, null);
    }

    /**
     * Performs a single value Lookup.  The following occurs on a Lookup:
     * <ol>
     * <li>The Lookup button is clicked on</li>
     * <li>In the Lookup web page, the given field is filled in with the given value</li>
     * <li>In the Lookup web page, the search button is clicked on</li>
     * <li>The first item in the results is returned</li>
     * </ol>
     * To find the Lookup button, the name attribute of all Lookup buttons are examined to see if it contains the given {@code tag}.  Make sure to pick 
     * a {@code tag} that is unique for that Lookup.
     * <p>
     * Using any of the {@code lookup()} methods is the preferred way to perform a lookup due to the login process.  If the login web page is 
     * encountered, the user will be automatically logged in and the lookup will be performed.
     *
     * @param tag identifies the Lookup button to click on
     * @param searchFieldId the id of the search field (may be null)
     * @param searchFieldValue the value to insert into the search field (may be null if id is null)
     */
    protected final void lookup(final String tag, final String searchFieldId, final String searchFieldValue) {    
        clickLookup(tag);

        if (searchFieldId != null) {
            assertNotNull("searchValue is null", searchFieldValue);
            set(searchFieldId, searchFieldValue);
        }

        click("methodToCall.search");

        assertTableCellValue("row", 0, 0, "return value");
        
        click("return value");
    }

    /**
     * Performs a multiple value Lookup.  The following occurs on a Lookup:
     * <ol>
     * <li>The Lookup icon is clicked on</li>
     * <li>In the Lookup web page, the search button is clicked on</li>
     * <li>The "Select All from All Pages" button is clicked on</li>
     * <li>The web page resulting from clicking on "Return Selected" is returned</li>
     * </ol>
     * To find the Lookup button, the name attribute of all Lookup buttons are examined to see if it contains the given {@code tag}.  Make sure to pick 
     * a {@code tag} that is unique for that Lookup.
     * <p>
     * Using any of the {@code multiLookup()} methods is the preferred way to perform a lookup due to the login process.  If the login web page is 
     * encountered, the user will be automatically logged in and the lookup will be performed.
     * 
     * @param tag identifies the Lookup button to click on
     */
    protected final void multiLookup(final String tag) {
        multiLookup(tag, null, null);
    }

    /**
     * Performs a multiple value Lookup.  The following occurs on a Lookup:
     * <ol>
     * <li>The Lookup icon is clicked on</li>
     * <li>The search field is filled in with the given search value.</li>
     * <li>In the Lookup web page, the search button is clicked on</li>
     * <li>The "Select All from All Pages" button is clicked on</li>
     * <li>The web page resulting from clicking on "Return Selected" is returned</li>
     * </ol>
     * To find the Lookup button, the name attribute of all Lookup buttons are examined to see if it contains the given {@code tag}.  Make sure to pick 
     * a {@code tag} that is unique for that Lookup.
     * <p>
     * Using any of the {@code multiLookup()} methods is the preferred way to perform a lookup due to the login process.  If the login web page is 
     * encountered, the user will be automatically logged in and the lookup will be performed.
     *
     * @param tag identifies the Lookup button to click on
     * @param searchFieldId the id of the search field (may be null)
     * @param searchFieldValue the value to insert into the search field (may be null if id is null)
     */
    protected final void multiLookup(final String tag, final String searchFieldId, final String searchFieldValue) {
        clickLookup(tag);

        if (searchFieldId != null) {
            assertNotNull("searchValue is null", searchFieldValue);
            set(searchFieldId, searchFieldValue);
        }

        click("methodToCall.search");
        
        assertTableCellValue("row", 0, 1, searchFieldValue);
        
        click("methodToCall.selectAll");

        click("methodToCall.prepareToReturnSelectedResults");
    }
    
    /**
     * Clicks a Lookup element that has a name attribute containing {@code tag}.
     *
     * @param tag identifies the Lookup button to click on
     */
    private void clickLookup(final String tag) {
        final String locator = "//input[starts-with(@name,'methodToCall.performLookup')and contains(@name,'" + tag + "')]";

        WebElement lookup = new ElementExistsWaiter(locator + " not found").until(
            new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return getElementByXPath(locator);
                }
            }
        );
        
        lookup.click();
        
        login();
    }
    
    /**
     * Reload a document by clicking on the Reload button.
     */
    protected final void reloadDocument() {
        click(RELOAD_BUTTON);
    }

    /**
     * Save a document by clicking on the Save button.
     */
    protected final void saveDocument() {
        click(SAVE_BUTTON);
    }
    
    /**
     * Closes a document without saving.
     */
    protected final void closeDocument() {
        closeDocument(false);
    }

    /**
     * Closes a document, optionally saving if {@code save} is set to true.
     *
     * @param save whether or not the document should be saved before closing
     */
    protected final void closeDocument(boolean save) {
        if (save) {
            saveDocument();
        }
        
        click(CLOSE_BUTTON);
        // TODO: this may or may not show up depending on the document, but getElement will never return null...
        if (getElement("methodToCall.processAnswer.button1") != null) {
            click("methodToCall.processAnswer.button1");
        }
    }

    /**
     * Saves and closes document and then performs a document search to retrieve the document.
     */
    protected final void closeAndSearchDocument() {
        String documentNumber = getDocumentNumber();
        
        closeDocument(true);

        docSearch(documentNumber);
    }
    
    /**
     * Routes the document.
     */
    protected final void routeDocument() {
        click(ROUTE_BUTTON);
    }
    
    /**
     * Asserts that the document has been saved with no errors.
     */
    protected final void assertSave() {
        assertPageDoesNotContain(ERRORS_FOUND_ON_PAGE);
        assertPageContains(SAVE_SUCCESS_MESSAGE);
    }
    
    /**
     * Asserts that the value of the element identified by {@code locator} contains {@code value}.
     * 
     * @param locator the id, partial name, partial title, or partial link name of the element to search for
     * @param value the value to look for in the element
     */
    protected final void assertElementContains(String locator, String value) {
        clickExpandAll();

        WebElement element = getElement(locator);
        assertTrue("Element " + locator + " does not contain " + value, StringUtils.contains(element.getValue(), value)); 
    }
    
    /**
     * Asserts that the value of the element identified by {@code locator} does <b>not</b> contain {@code value}.
     * 
     * @param locator the id, partial name, partial title, or partial link name of the element to search for
     * @param value the value to look for in the element
     */
    protected final void assertElementDoesNotContain(String locator, String value) {
        clickExpandAll();
        
        WebElement element = getElement(locator);
        assertFalse("Element " + locator + " contains " + value, StringUtils.contains(element.getValue(), value)); 
    }

    /**
     * Asserts that the web page contains {@code text}.
     * 
     * @param text the string to look for in the web page.
     */
    protected final void assertPageContains(final String text) {
        clickExpandAll();
        
        new ElementExistsWaiter("Page does not contain " + text).until(
            new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return getElementByText(text);
                }
            }
        ); 
    }
    
    /**
     * Asserts that the web page does <b>not</b> contain {@code text}.
     * 
     * @param text the string to look for in the web page.
     */
    protected final void assertPageDoesNotContain(final String text) {
        clickExpandAll();
        
        new ElementDoesNotExistWaiter("Page contains " + text).until(
            new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return getElementByText(text);
                }
            }
         );
    }
    
    /**
     * Asserts that the web page title contains {@code title}.
     * 
     * @param title the title to look for in the web page.
     */
    protected final void assertTitleContains(final String title) {
        String pageSource = driver.getPageSource();
        
        if (!StringUtils.contains(pageSource, title)) {
            if (switchToIFramePortlet()) {
                pageSource = driver.getPageSource();
            }
        }

        assertTrue("Page does not contain " + title, StringUtils.contains(pageSource, title));
    }
    
    /**
     * Asserts that the web page title does <b>not</b> contain {@code title}.
     * 
     * @param title the title to look for in the web page.
     */
    protected final void assertTitleDoesNotContain(final String title) {
        String pageSource = driver.getPageSource();
        
        if (StringUtils.contains(pageSource, title)) {
            if (switchToIFramePortlet()) {
                pageSource = driver.getPageSource();
            }
        }

        assertTrue("Page contains" + title, !StringUtils.contains(pageSource, title));
    }
    
    /**
     * Assert that the list of options identified by {@code locator} contains {@code text}.
     *
     * @param locator the id, partial name, partial title, or partial link name of the element to search for
     * @param text the string to look for in the select options
     */
    protected final void assertSelectOptionsContains(final String locator, final String text) {
        Select select = new Select(getElement(locator));
        
        List<String> selectedValues = new ArrayList<String>();
        for (WebElement option : select.getAllSelectedOptions()) {
            selectedValues.add(option.getText());
        }
        
        assertTrue("Selected options for " + locator + " do not contain " + text, selectedValues.contains(text));
    }
    
    /**
     * Asserts that the Expanded Text Area is providing a popup window in which to change its value.  Verifies that the that this is working properly by 
     * performing the following:
     * <ol>
     * <li>The text area is set to the {@code originalText} value</li>
     * <li>The pencil button is clicked on, opening in a popup window</li>
     * <li>The text in the popup window is examined to verify that it is equal to {@code originalText}</li>
     * <li>The popup window text area is changed to {@code expandedAreaText}</li>
     * <li>The "Continue" button is clicked on, closing the popup window</li>
     * <li>The resulting web page is examined to verify that the text area has changed to the value of {@code expandedAreaText}</li>
     * </ol>
     *
     * @param textAreaId identifies the text area
     * @param originalText the string to set the original text area to
     * @param expandedAreaText the string to set in the popup window text area
     */
    protected final void assertExpandedTextArea(final String textAreaId, final String originalText, final String expandedAreaText) {
        set(textAreaId, originalText);
        
        final String locator = "//input[starts-with(@name,'methodToCall.updateTextArea') and contains(@name, '" + textAreaId + "')]";
        
        WebElement textAreaButton = new ElementExistsWaiter("Expand button for " + textAreaId + " not found").until(
            new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return getElementByXPath(locator);
                }
            }
        );

        textAreaButton.click();
        
        driver.switchTo().window("null");
        
        assertEquals(originalText, get(textAreaId));

        set(textAreaId, expandedAreaText);
        
        click("methodToCall.postTextAreaToParent");

        assertEquals(expandedAreaText, get(textAreaId));
    }
    
    /**
     * Asserts that all of the Help links on a web page (identified by the {@code helpWindow} target) are bringing up a page with the appropriate
     * Help Page title.
     */
    protected final void assertHelpLinks() {
        List<WebElement> helpLinks = getElementsByXPath("//node()[@target='helpWindow']");
        for (WebElement helpLink : helpLinks) {
            helpLink.click();
            assertTitleContains(HELP_PAGE_TITLE);
        }
    }
    
    /**
     * Asserts that the text in the table identified by {@code id} at row {@code row} and column {@code column} matches the given 
     * {@code text}.
     *
     * @param id identifies the table to search
     * @param row the 0-valued row number to search
     * @param column the 0-valued column number to search
     * @param text the text to verify
     */
    protected final void assertTableCellValue(final String id, final int row, final int column, final String text) {
        String rowString = String.valueOf(row + 1);
        String columnString = String.valueOf(column + 1);
        
        final String locator = "//table[@id='" + id + "']//tr[" + rowString + "]/td[" + columnString + "]";
        
        new ElementExistsWaiter(text + " not found for table " + id + " at row " + rowString + " and column " + columnString).until(
            new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    WebElement cell = getElementByXPath(locator);
                    return cell != null && StringUtils.equals(text, cell.getText());
                }
            }
        );
    }
    
    /**
     * Asserts that the page contains one or more errors.
     */
    protected final void assertPageErrors() {
        clickExpandAll();
        
        new ElementExistsWaiter("Page does not contain errors").until(
            new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return getElementByText(ERRORS_FOUND_ON_PAGE) != null 
                        || getElementByText("Errors Found in Document") != null 
                        || getElementByText("Kuali :: Incident Report") != null;
                }
            }
        );
    }
    
    /**
     * Asserts that the page contains no errors.
     */
    protected final void assertNoPageErrors() {
        clickExpandAll();
        
        new ElementDoesNotExistWaiter("Page contains errors").until(
            new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return getElementByText(ERRORS_FOUND_ON_PAGE) != null 
                        || getElementByText("Errors Found in Document") != null 
                        || getElementByText("Kuali :: Incident Report") != null;
                }
            }
        );
    }
    
    /**
     * Asserts that one or more of the errors contained in {@code panelId} contains {@code text}.
     *
     * @param panelId the id attribute of the panel
     * @param text the string to look for in the errors
     */
    protected final void assertError(final String panelId, final String expectedText) {
        clickExpandAll();
        
        List<String> errorValues = new ArrayList<String>();
        for (WebElement error : getErrors(panelId)) {
            errorValues.add(error.getValue());
        }
        
        assertTrue("Errors in " + panelId + " do not contain " + expectedText, errorValues.contains(expectedText));
    }

    /**
     * Asserts that there are {@code expectedErrorCount} errors contained in {@code panelId}.
     *
     * @param panelId the id attribute of the panel
     * @param expectedErrorCount the number of errors expected on the page
     */
    protected final void assertErrorCount(final String panelId, final int expectedErrorCount) {
        clickExpandAll();
        
        List<WebElement> errors = getErrors(panelId);
        assertEquals("Error count of " + errors.size() + " did not match the expected error count of " + expectedErrorCount, expectedErrorCount, errors.size());
    }
    
    /**
     * Opens the Portal Web Page. The portal page is the starting point for many web tests in order to simulate a user.
     */
    private void openPortalPage() {
        driver.get(BROWSER_PROTOCOL + "://" + BROWSER_ADDRESS + ":" + HtmlUnitUtil.getPort().toString() + "/" + PORTAL_ADDRESS);
    }
    
    /**
     * Gets an element in the web page.  To find the element, the following algorithm is used:
     * <ol>
     * <li>Search for an active element with an {@code id} attribute that matches {@code locator}</li>
     * <li>If not found, search for the first active element with a {@code name} attribute that contains {@code locator}</li>
     * <li>If not found, search for the first active element with a {@code title} attribute that contains {@code locator}</li>
     * <li>If not found, search for the first active link element containing {@code locator}</li>
     * </ol>
     *
     * @param locator the id, partial name, partial title, or partial link name of the element to search for
     */
    private WebElement getElement(final String locator) {
        return new ElementExistsWaiter(locator + " not found").until(
            new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    WebElement element = getElementById(locator);
                    
                    if (element == null) {
                        element = getElementByName(locator);
                        if (element == null) {
                            element = getElementByTitle(locator);
                            if (element == null) {
                                element = getElementByLinkText(locator);
                            }
                        }
                    }
                    
                    return element;
                }
            }
        );
    }
    
    /**
     * Gets the first active element in the web page with an {@code id} attribute that matches {@code id}.
     * 
     * @param id the id of the element to search for
     */
    private WebElement getElementById(final String id) {
        WebElement element = null;
        
        List<WebElement> elements = getElementsById(id);
        if (!elements.isEmpty()) {
            element = elements.get(0);
        }
        
        return element;
    }

    /**
     * Gets the first active element in the web page with a {@code name} attribute that contains {@code name}.
     * 
     * @param name the partial name of the element to search for
     */
    private WebElement getElementByName(final String name) {
        WebElement element = null;
        
        List<WebElement> elements = getElementsByName(name);
        if (!elements.isEmpty()) {
            element = elements.get(0);
        }
        
        return element;
    }
    
    /**
     * Gets the first active element in the web page with a {@code title} attribute that contains {@code title}.
     * 
     * @param title the partial title of the element to search for
     */
    private WebElement getElementByTitle(final String title) {
        WebElement element = null;
        
        List<WebElement> elements = getElementsByTitle(title);
        if (!elements.isEmpty()) {
            element = elements.get(0);
        }
        
        return element;
    }
    
    /**
     * Gets the first active element in the web page with link text that contains {@code linkText}.
     * 
     * @param linkText the partial link text of the element to search for
     */
    private WebElement getElementByLinkText(final String linkText) {
        WebElement element = null;
        
        List<WebElement> elements = getElementsByLinkText(linkText);
        if (!elements.isEmpty()) {
            element = elements.get(0);
        }
        
        return element;
    }
    
    /**
     * Gets the first active element in the web page that matches the XPath in {@code xPath}.
     * 
     * @param xPath an XPath expression for the element to search for
     */
    private WebElement getElementByXPath(final String xPath) {
        WebElement element = null;
        
        List<WebElement> elements = getElementsByXPath(xPath);
        if (!elements.isEmpty()) {
            element = elements.get(0);
        }
        
        return element;
    }
    
    /**
     * Gets the first active element in the web page with text that contains {@code text}.
     * 
     * @param text the text in the element to search for
     */
    private WebElement getElementByText(final String text) {
        WebElement element = null;
        
        List<WebElement> elements = getElementsByText(text);
        if (!elements.isEmpty()) {
            element = elements.get(0);
        }
        
        return element;
    }
    
    /**
     * Gets all active elements in the web page with an {@code id} attribute that matches {@code id}.
     * 
     * @param id the id of the element to search for
     */
    private List<WebElement> getElementsById(final String id) {
        List<WebElement> elements = getActiveElementsById(id);
        
        if (switchToIFramePortlet()) {
            elements.addAll(getActiveElementsById(id));
        }
        
        return elements;
    }
    
    /**
     * Gets all active elements in the current frame with an {@code id} attribute that matches {@code id}.
     * 
     * @param id the id of the element to search for
     */
    private List<WebElement> getActiveElementsById(final String id) {
        List<WebElement> elements = new ArrayList<WebElement>();

        for (WebElement element : driver.findElements(By.id(id))) {
            if (((RenderedWebElement) element).isDisplayed()) {
                elements.add(element);
            }
        }
        
        return elements;
    }
    
    /**
     * Gets all active elements in the web page with a {@code name} attribute that contains {@code name}.
     * 
     * @param name the partial name of the element to search for
     */
    private List<WebElement> getElementsByName(final String name) {
        List<WebElement> elements = getActiveElementsByName(name);
        
        if (switchToIFramePortlet()) {
            elements.addAll(getActiveElementsByName(name));
        }
        
        return elements;
    }
    
    /**
     * Gets all active elements in the current frame with a {@code name} attribute that contains {@code name}.
     * 
     * @param name the partial name of the element to search for
     */
    private List<WebElement> getActiveElementsByName(final String name) {
        List<WebElement> elements = new ArrayList<WebElement>();

        for (WebElement element : driver.findElements(By.xpath(getAttributeContainsXPath("name", name)))) {
            if (((RenderedWebElement) element).isDisplayed()) {
                elements.add(element);
            }
        }
        
        return elements;
    }
    
    /**
     * Gets all active elements in the web page with a {@code title} attribute that contains {@code title}.
     * 
     * @param title the partial title of the element to search for
     */
    private List<WebElement> getElementsByTitle(final String title) {
        List<WebElement> elements = getActiveElementsByTitle(title);
        
        if (switchToIFramePortlet()) {
            elements.addAll(getActiveElementsByTitle(title));
        }
        
        return elements;
    }
    
    /**
     * Gets all active elements in the current frame with a {@code title} attribute that contains {@code title}.
     * 
     * @param title the partial title of the element to search for
     */
    private List<WebElement> getActiveElementsByTitle(final String title) {
        List<WebElement> elements = new ArrayList<WebElement>();

        for (WebElement element : driver.findElements(By.xpath(getAttributeContainsXPath("title", title)))) {
            if (((RenderedWebElement) element).isDisplayed()) {
                elements.add(element);
            }
        }
        
        return elements;
    }
    
    /**
     * Gets all active elements in the web page with link text that contains {@code linkText}.
     * 
     * @param linkText the partial link text of the element to search for
     */
    private List<WebElement> getElementsByLinkText(final String linkText) {
        List<WebElement> elements = getActiveElementsByLinkText(linkText);
        
        if (switchToIFramePortlet()) {
            elements.addAll(getActiveElementsByLinkText(linkText));
        }
        
        return elements;
    }
    
    /**
     * Gets all active elements in the current frame with link text that contains {@code linkText}.
     * 
     * @param linkText the partial link text of the element to search for
     */
    private List<WebElement> getActiveElementsByLinkText(final String linkText) {
        List<WebElement> elements = new ArrayList<WebElement>();

        for (WebElement element : driver.findElements(By.partialLinkText(linkText))) {
            if (((RenderedWebElement) element).isDisplayed()) {
                elements.add(element);
            }
        }
        
        return elements;
    }
    
    /**
     * Gets all active elements in the web page that match the XPath in {@code xPath}.
     * 
     * @param xPath an XPath expression for the element to search for
     */
    private List<WebElement> getElementsByXPath(final String xPath) {
        List<WebElement> elements = getActiveElementsByXPath(xPath);
        
        if (switchToIFramePortlet()) {
            elements.addAll(getActiveElementsByXPath(xPath));
        }
        
        return elements;
    }
    
    /**
     * Gets all active elements in the current frame that match the XPath in {@code xPath}.
     * 
     * @param xPath an XPath expression for the element to search for
     */
    private List<WebElement> getActiveElementsByXPath(final String xPath) {
        List<WebElement> elements = new ArrayList<WebElement>();

        for (WebElement element : driver.findElements(By.xpath(xPath))) {
            if (((RenderedWebElement) element).isDisplayed()) {
                elements.add(element);
            }
        }
        
        return elements;
    }
    
    /**
     * Gets all active elements in the web page with text that contains {@code text}.
     * 
     * @param text the text in the element to search for
     */
    private List<WebElement> getElementsByText(final String text) {
        List<WebElement> elements = getActiveElementsByText(text);
        
        if (switchToIFramePortlet()) {
            elements.addAll(getActiveElementsByText(text));
        }
        
        return elements;
    }
    
    /**
     * Gets all active elements in the current frame with text that contains {@code text}.
     * 
     * @param text the text in the element to search for
     */
    private List<WebElement> getActiveElementsByText(final String text) {
        List<WebElement> elements = new ArrayList<WebElement>();

        for (WebElement element : driver.findElements(By.xpath("//*[contains(text(), '" + text + "')]"))) {
            if (((RenderedWebElement) element).isDisplayed()) {
                elements.add(element);
            }
        }
        
        return elements;
    }
    
    /**
     * Attempts to switch to KC's inner frame, named 'iframeportlet'.
     *
     * @return true if the driver successfully switched to the inner frame, false otherwise
     */
    private boolean switchToIFramePortlet() {
        boolean switchToIFramePortletSuccessful = true;
        
        try {
            driver.switchTo().frame("iframeportlet");
        } catch (Exception e) {
            switchToIFramePortletSuccessful = false;
        }
        
        return switchToIFramePortletSuccessful;
    }
    
   /**
    * Returns the XPath string that searches for elements that have an {@code attribute} that contains {@code text}.
    * 
    * @param attribute the name of the attribute
    * @param text the text to search for in the attribute
    * @return an XPath expression for elements that have an {@code attribute} that contains {@code text}
    */
    private String getAttributeContainsXPath(final String attribute, final String text) {
        return "//*[contains(@" + attribute + ", '" + text + "')]";
    }
    
    /**
     * Returns the list of elements that contain errors in the element identified by {@code panelId}.
     * 
     * @param panelId the id attribute of the panel
     * @return a list of errors contained in {@code panelId}
     */
    private List<WebElement> getErrors(final String panelId) {
        return getElementsByXPath("//div[@id='" + panelId + "']//div[@style='display: list-item; margin-left: 20px;']");
    }
    
    /**
     * Implements a {@code Wait<WebDriver>} class for waiting for elements (especially Ajax elements) to appear on the page within a specified timeout.  
     * Modified from {@code WebDriverWait} in order to integrate custom JUnit4 assertion messages. 
     * 
     * @see org.openqa.selenium.support.ui.Wait
     * @see org.openqa.selenium.support.ui.WebDriverWait
     */
    private class ElementExistsWaiter implements Wait<WebDriver> {
        
        private final Clock clock = new SystemClock();
        private final long testTimeOut = 10000;
        private final long sleepTimeOut = 500;
        
        private String message;
        
        protected ElementExistsWaiter(String message) {
            this.message = message;
        }
        
        /**
         * {@inheritDoc}
         * @see org.openqa.selenium.support.ui.Wait#until(com.google.common.base.Function)
         */
        public <T> T until(Function<WebDriver, T> exists) {
            long end = clock.laterBy(testTimeOut);
            while (clock.isNowBefore(end)) {
                T value = exists.apply(driver);
                
                if (value != null) {
                    if (Boolean.class.equals(value.getClass())) {
                        if (Boolean.TRUE.equals(value)) {
                            return value;
                        }
                    } else {
                        return value;
                    }
                }
                
                sleep();
            }
            
            throw new AssertionError(message);
        }
        
        private void sleep() {
            try {
                Thread.sleep(sleepTimeOut);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Implements a {@code Wait<WebDriver>} class for waiting for elements (especially Ajax elements) to not appear on the page within a specified timeout.  
     * Modified from {@code WebDriverWait} in order to integrate custom JUnit4 assertion messages and add the not condition. 
     * 
     * @see org.openqa.selenium.support.ui.Wait
     * @see org.openqa.selenium.support.ui.WebDriverWait
     */
    private class ElementDoesNotExistWaiter implements Wait<WebDriver> {
        
        private final Clock clock = new SystemClock();
        private final long testTimeOut = 1000;
        private final long sleepTimeOut = 500;
        
        private String message;
        
        protected ElementDoesNotExistWaiter(String message) {
            this.message = message;
        }
        
        /**
         * {@inheritDoc}
         * @see org.openqa.selenium.support.ui.Wait#until(com.google.common.base.Function)
         */
        public <T> T until(Function<WebDriver, T> exists) {
            long end = clock.laterBy(testTimeOut);
            while (clock.isNowBefore(end)) {
                T value = exists.apply(driver);
                
                if (value != null) {
                    if (Boolean.class.equals(value.getClass())) {
                        if (Boolean.TRUE.equals(value)) {
                            throw new AssertionError(message);
                        }
                    } else {
                        throw new AssertionError(message);
                    }
                }
                
                sleep();
            }
            
            return null;
        }
        
        private void sleep() {
            try {
                Thread.sleep(sleepTimeOut);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}