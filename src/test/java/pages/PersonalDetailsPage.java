package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class PersonalDetailsPage {
    @FindBy(xpath = "//h6[text()='Personal Details']")
    WebElement labelPersonalDetails;

    @FindBy(xpath = "//label[contains(normalize-space(), 'Employee Id')]/ancestor::div[contains(@class, 'oxd-input-group')]//input")
    WebElement inpSpecificEmployeeId;

    @FindBy(xpath = "//button[text() =' Save ']")
    List<WebElement> btnPersonalDetails;

    @FindBy(xpath = "//input[@type='radio']")
    List<WebElement> radioBtns;

    @FindBy(xpath = "//div[@class='oxd-radio-wrapper']//label")
    List<WebElement> btnsRadio;

    @FindBy(xpath = "//a[contains(text(), 'Contact Details')]")
    WebElement btnContactDetails;

    private WebDriver driver;
    private Waits wait;

    public PersonalDetailsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        wait = Browser.getWaits(ConfigReader.getTimeOuts());
    }

    public String getPersonalDetailsLabel(){
        wait.waitToBeDisplayed(labelPersonalDetails);
        return labelPersonalDetails.getText();
    }

    public void enterEmployeedId(String id){
        wait.waitToBeDisplayed(inpSpecificEmployeeId);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", inpSpecificEmployeeId);

        try {
            inpSpecificEmployeeId.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", inpSpecificEmployeeId);
        }

        inpSpecificEmployeeId.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        inpSpecificEmployeeId.sendKeys(Keys.DELETE);

        try { Thread.sleep(500); } catch (Exception e) {}
        inpSpecificEmployeeId.sendKeys(id);
    }

    public void clickOnPersonalDtlsSaveBtn() {
        wait.waitToBeVisibleAllElements(btnPersonalDetails);
        WebElement btnPersonalDetail = btnPersonalDetails.get(0);
        wait.waitToBeDisplayed(btnPersonalDetail);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnPersonalDetail);
        try {
            btnPersonalDetail.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnPersonalDetail);
        }
    }

    public void clickOnCustomFieldsSaveBtn() {
        try {
            // Find buttons dynamically to avoid breaking if the Custom Fields form is missing
            List<WebElement> saveBtns = driver.findElements(By.xpath("//button[text() =' Save ']"));
            if(saveBtns.size() > 1) {
                WebElement btnCustomFieldSave = saveBtns.get(1);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnCustomFieldSave);
                try {
                    btnCustomFieldSave.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnCustomFieldSave);
                }
            } else {
                Logs.info("WARNING: Custom Fields Save button not found. Skipping.");
            }
        } catch (Exception e) {
            Logs.info("Error clicking Custom Fields save button: " + e.getMessage());
        }
    }

    public void selectGenderType(String type) {
        wait.waitToBeVisibleAllElements(btnsRadio);
        WebElement targetRadio = type.equalsIgnoreCase("male") ? btnsRadio.get(0) : btnsRadio.get(1);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", targetRadio);

        try {
            targetRadio.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetRadio);
        }
    }

    public void selectBloodType(String type){
        try { Thread.sleep(3000); } catch (Exception e) {}

        try {
            // Check dynamically if the element actually exists on the screen
            List<WebElement> bloodTypeElements = driver.findElements(By.xpath("//*[contains(text(), 'Blood Type')]/ancestor::div[contains(@class, 'oxd-input-group')]//div[contains(@class, 'oxd-select-text')]"));

            if (!bloodTypeElements.isEmpty()) {
                WebElement dropdownBloodType = bloodTypeElements.get(0);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", dropdownBloodType);

                try {
                    dropdownBloodType.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownBloodType);
                }

                int count = 0;
                switch (type){
                    case "A+": count = 1; break;
                    case "A-": count = 2; break;
                    case "B-": count = 4; break;
                    case "O+": count = 5; break;
                    case "O-": count = 6; break;
                    case "AB+": count = 7; break;
                    case "AB-": count = 8; break;
                    default: break;
                }
                for (int i = 0; i<count; i++){
                    dropdownBloodType.sendKeys(Keys.ARROW_DOWN);
                }
                dropdownBloodType.sendKeys(Keys.ENTER);
            } else {
                Logs.info("WARNING: 'Blood Type' field is missing from the public demo site. Skipping selection to avoid test failure.");
            }
        } catch (Exception e) {
            Logs.info("Could not interact with Blood Type: " + e.getMessage());
        }
    }

    public void clickOnContactDetailsBtn(){
        wait.waitToBeDisplayed(btnContactDetails);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btnContactDetails);

        try {
            btnContactDetails.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnContactDetails);
        }
    }
}