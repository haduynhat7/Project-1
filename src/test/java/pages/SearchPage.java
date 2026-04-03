package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Browser;
import utils.ConfigReader;
import utils.Logs;
import utils.Waits;
import java.util.List;

public class SearchPage {
    // Explicit XPath for Employee Name Search
    @FindBy(xpath = "//label[contains(normalize-space(), 'Employee Name')]/ancestor::div[contains(@class, 'oxd-input-group')]//input")
    WebElement inpSpecificEmployeeName;

    // Explicit XPath for Employee Id Search
    @FindBy(xpath = "//label[contains(normalize-space(), 'Employee Id')]/ancestor::div[contains(@class, 'oxd-input-group')]//input")
    WebElement inpSpecificEmployeeId;

    @FindBy(xpath = "//button[text()=' Search ']")
    WebElement btnSearch;

    @FindBy(xpath = "//span[contains(@class, 'oxd-text') and contains(normalize-space(), 'Found')]")
    WebElement labelUserFound;

    @FindBy(xpath = "//div[contains(@class, 'oxd-table-cell')]")
    List<WebElement> userRecords;

    WebDriver driver;
    private Waits wait;

    public SearchPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        wait = Browser.getWaits(ConfigReader.getTimeOuts());
    }

    public void enterName(String name)  {
        wait.waitToBeDisplayed(inpSpecificEmployeeName);
        Logs.info("Entering name in the employee name search field");

        inpSpecificEmployeeName.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        inpSpecificEmployeeName.sendKeys(Keys.DELETE);
        try { Thread.sleep(500); } catch (Exception e) {}

        inpSpecificEmployeeName.sendKeys(name);
    }

    public void enterId(String id)  {
        wait.waitToBeDisplayed(inpSpecificEmployeeId);
        Logs.info("Entering employee id in the search field");

        inpSpecificEmployeeId.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        inpSpecificEmployeeId.sendKeys(Keys.DELETE);
        try { Thread.sleep(500); } catch (Exception e) {}

        inpSpecificEmployeeId.sendKeys(id);
    }

    public void clickOnSearchBtn() {
        try { Thread.sleep(2000); } catch (Exception e){}
        wait.waitToBeDisplayed(btnSearch);
        btnSearch.click();
        try { Thread.sleep(3000); } catch (Exception e){}
    }

    public String getUserFoundTxt(){
        wait.waitToBeDisplayed(labelUserFound);
        return labelUserFound.getText();
    }

    public void clickOnFirstRecord() {
        wait.waitToBeVisibleAllElements(userRecords);
        userRecords.get(1).click();
        try { Thread.sleep(3000); } catch (Exception e){}
    }
}