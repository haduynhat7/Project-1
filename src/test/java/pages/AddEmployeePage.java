package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Browser;
import utils.ConfigReader;
import utils.Logs;
import utils.Waits;

import java.util.List;

public class AddEmployeePage {
    @FindBy(xpath="//input[contains(@class, 'oxd-input')]")
    List<WebElement> txtInp;

    @FindBy(className = "oxd-switch-input")
    WebElement btnToggle;

    @FindBy(css="[type=submit]")
    WebElement btnSubmit;

    private WebDriver driver;
    Waits wait;

    public AddEmployeePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        wait = Browser.getWaits(ConfigReader.getTimeOuts());
    }

    public void inputFirstName(String firstName){
        wait.waitToBeDisplayed(txtInp.get(1));
        WebElement inpFirstName = txtInp.get(1);
        Logs.info("Inserting firstname");
        inpFirstName.sendKeys(firstName);
    }

    public void inputLastName(String lastName){
        WebElement inpLastName = txtInp.get(3);
        wait.waitToBeDisplayed(inpLastName);
        Logs.info("Inserting lastname");
        inpLastName.sendKeys(lastName);
    }

    public void inputEmployeeId(String id){
        WebElement empId = txtInp.get(4);
        wait.waitToBeDisplayed(empId);

        // Scroll slightly just to be safe before interacting
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", empId);
        empId.click();
        empId.clear();
        empId.sendKeys(id);
    }

    public void inputUserName(String userName){
        wait.waitToBeDisplayed(txtInp.get(5));
        WebElement inpUserName = txtInp.get(5);
        Logs.info("Inserting username");
        inpUserName.sendKeys(userName);
    }

    public void inputPassword(String password){
        wait.waitToBeDisplayed(txtInp.get(6));
        WebElement inpPassword = txtInp.get(6);
        Logs.info("Inserting password");
        inpPassword.sendKeys(password);
    }

    public void inputConfirmPassword(String password){
        wait.waitToBeDisplayed(txtInp.get(7));
        WebElement inpConfirmPassword = txtInp.get(7);
        Logs.info("Inserting password in confirm password txt field");
        inpConfirmPassword.sendKeys(password);
    }

    public void clickOnLoginDetailsToggleBtn(){
        Logs.info("Clicking on Login Details toggle button");
        wait.waitToBeDisplayed(btnToggle);

        // Scroll to center
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnToggle);

        try {
            btnToggle.click();
        } catch (Exception e) {
            Logs.info("Toggle click intercepted, using JS fallback");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnToggle);
        }
    }

    public void clickOnSaveBtn(){
        Logs.info("Clicking on save button");
        wait.waitToBeDisplayed(btnSubmit);

        // Scroll to center
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnSubmit);

        try {
            btnSubmit.click();
        } catch (Exception e) {
            Logs.info("Save button click intercepted, using JS fallback");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnSubmit);
        }
    }
}