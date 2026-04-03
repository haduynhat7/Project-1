package testrunner;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.*;
import setup.BaseTest;
import utils.RandomInfoUtils;
import utils.Utils;

public class PIMTestRunner extends BaseTest {
    LoginPage loginPage;
    PIMPage pimPage;
    LogoutPage logoutPage;
    SearchPage srchPage;
    AddEmployeePage addEmpPage;
    PersonalDetailsPage prsonalDtlsPage;
    SidePanelPage sidePnlPage;
    ContactDetailsPage contactPage;


    @BeforeTest
    public void doLoginAsAdmin() {
        loginPage = new LoginPage(driver);
        loginPage.doLogin("Admin", "admin123");
        pimPage = new PIMPage(driver);
        srchPage = new SearchPage(driver);
        addEmpPage = new AddEmployeePage(driver);
        prsonalDtlsPage = new PersonalDetailsPage(driver);
        logoutPage = new LogoutPage(driver);
        sidePnlPage = new SidePanelPage(driver);
        contactPage = new ContactDetailsPage(driver);
    }

    @Test(priority = 1, description = "Adding first employee")
    public void addFirstEmployee() {
        prsonalDtlsPage = new PersonalDetailsPage(driver);

        pimPage.clickOnPIMFromDashboard();
        pimPage.clickOnAddBtn();

        addEmpPage.clickOnLoginDetailsToggleBtn();
        String firstName = RandomInfoUtils.getFirstName();
        addEmpPage.inputFirstName(firstName);
        String lastName = RandomInfoUtils.getLastName();
        addEmpPage.inputLastName(lastName);
        String userName = RandomInfoUtils.getUserName();
        String id = RandomInfoUtils.getUserId();
        addEmpPage.inputEmployeeId(id);
        addEmpPage.inputUserName(userName);
        String password = RandomInfoUtils.getPassword();
        addEmpPage.inputPassword(password);
        addEmpPage.inputConfirmPassword(password);
        addEmpPage.clickOnSaveBtn();

        String header_actual = prsonalDtlsPage.getPersonalDetailsLabel();
        String header_expected = "Personal Details";

        Assert.assertEquals(header_actual, header_expected);
        Utils.addJsonArray(firstName, lastName, userName, password);

    }

    @Test(priority = 2, description = "Adding second employee")
    public void addSecondEmployee() {
        pimPage.clickOnPIMFromDashboard();
        pimPage.clickOnAddBtn();

        addEmpPage.clickOnLoginDetailsToggleBtn();
        String firstName = RandomInfoUtils.getFirstName();
        addEmpPage.inputFirstName(firstName);
        String lastName = RandomInfoUtils.getLastName();
        addEmpPage.inputLastName(lastName);
        String id = RandomInfoUtils.getUserId();
        addEmpPage.inputEmployeeId(id);
        String userName = RandomInfoUtils.getUserName();
        addEmpPage.inputUserName(userName);
        String password = RandomInfoUtils.getPassword();
        addEmpPage.inputPassword(password);
        addEmpPage.inputConfirmPassword(password);
        addEmpPage.clickOnSaveBtn();

        String header_actual = prsonalDtlsPage.getPersonalDetailsLabel();
        String header_expected = "Personal Details";
        Assert.assertEquals(header_actual, header_expected);

        Utils.addJsonArray(firstName, lastName, userName, password);
    }

    @Test(priority = 3, description = "Searching User By name")
    public void searchUserByName() {
        String fileLocation = "./src/test/resources/NewUser.json";
        int jsonId = Utils.getUserJsonIdx(fileLocation);
        pimPage.clickOnEmployeeListBtn();
        String userName = Utils.getProperty(fileLocation, jsonId-2, "firstname");
        srchPage.enterName(userName);
        srchPage.clickOnSearchBtn();

        String isUserFound_actual = srchPage.getUserFoundTxt();
        System.out.println("Search by Name result: " + isUserFound_actual);

        String isUserFound_expected = "Record Found";
        Assert.assertTrue(isUserFound_actual.contains(isUserFound_expected), "Record not found");
    }

    @Test(priority = 4, description = "Update user id")
    public void updateUserId() {
        String fileLocation = "./src/test/resources/NewUser.json";
        int jsonId = Utils.getUserJsonIdx(fileLocation);
        srchPage.clickOnFirstRecord();
        String personalDetailsLabel_actual = prsonalDtlsPage.getPersonalDetailsLabel();
        String personalDetailsLabel_expected = "Personal Details";
        Assert.assertTrue(personalDetailsLabel_actual.contains(personalDetailsLabel_expected));

        String id =""+RandomInfoUtils.getUserId();
        prsonalDtlsPage.enterEmployeedId(id);
        prsonalDtlsPage.clickOnPersonalDtlsSaveBtn();

        // Them thoi gian cho de DB cua OrangeHRM kip cap nhat ID moi
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Utils.updateProperty(fileLocation, jsonId-2, "userid", id);
    }

    @Test(priority = 5, description = "Search user by id")
    public void searchUserById() {
        String fileLocation = "./src/test/resources/NewUser.json";
        int jsonId = Utils.getUserJsonIdx(fileLocation);

        pimPage.clickOnEmployeeListBtn();

        // Refresh trang de dam bao form tim kiem khong bi luu cache ten tu test case truoc
        driver.navigate().refresh();

        String userId = Utils.getProperty(fileLocation, jsonId-2, "userid");
        srchPage.enterId(userId);
        srchPage.clickOnSearchBtn();

        String isUserFound_actual = srchPage.getUserFoundTxt();
        System.out.println("Search by ID result: " + isUserFound_actual);

        String isUserFound_expected = "Record Found";
        Assert.assertTrue(isUserFound_actual.contains(isUserFound_expected), "Error: Expected 'Record Found' but actual text was: " + isUserFound_actual);
    }

    @Test(priority = 6, description = "Logout")
    public void doLogOut(){
        logoutPage.clickOnUserDropdownBtn();
        logoutPage.clickOnLogoutBtn();
        String loginLabel_actual = logoutPage.getLoginLabel();
        String loginLabel_expected = "Login";
        Assert.assertTrue(loginLabel_actual.contains(loginLabel_expected), "Logout unsuccessful");
    }

    @Test(priority = 7, description = "Loggin in as a second user")
    public void doLoginAs2ndUser() {
        String fileLocation = "./src/test/resources/NewUser.json";
        int jsonId = Utils.getUserJsonIdx(fileLocation);

        String username = Utils.getProperty(fileLocation, jsonId-1, "username");
        String password = Utils.getProperty(fileLocation, jsonId-1, "password");
        loginPage.doLogin(username, password);

        String userDropdownName_actual = loginPage.getUserDropdownBtnName();
        String userDropdownName_expected = Utils.getProperty(fileLocation, jsonId-1, "firstname");;
        Assert.assertTrue(userDropdownName_actual.contains(userDropdownName_expected), "Login successful");

        sidePnlPage.clickOnMyInfoBtn();
        String personalDetailsLabel_actual = prsonalDtlsPage.getPersonalDetailsLabel();
        String personalDetailsLabel_expected = "Personal Details";
        Assert.assertEquals(personalDetailsLabel_actual, personalDetailsLabel_expected, "Not in the personal details page");
    }

    @Test(priority = 8, description = "Updating Gendertype and blood type")
    public void updateUserGenderBloodType() {
        String fileLocation = "./src/test/resources/NewUser.json";
        int jsonId = Utils.getUserJsonIdx(fileLocation);

        String type="female";
        prsonalDtlsPage.selectGenderType(type);
        prsonalDtlsPage.clickOnPersonalDtlsSaveBtn();
        Utils.updateProperty(fileLocation, jsonId-1, "gendertype", type);

        String bloodType ="AB+";
        prsonalDtlsPage.selectBloodType(bloodType);
        prsonalDtlsPage.clickOnCustomFieldsSaveBtn();
        Utils.updateProperty(fileLocation, jsonId-1, "bloodtype", bloodType);
    }

    @Test(priority = 9, description = "Update Contact details and Email")
    public void updateContactDetailsAndEmail() {
        String fileLocation = "./src/test/resources/NewUser.json";
        int jsonId = Utils.getUserJsonIdx(fileLocation);

        prsonalDtlsPage.clickOnContactDetailsBtn();

        String streetAddress = RandomInfoUtils.getStreetAddress();
        contactPage.enterStreet1(streetAddress);

        String city = RandomInfoUtils.getCity();
        contactPage.enterCity(city);

        String state = RandomInfoUtils.getState();
        contactPage.enterStateProvince(state);

        String zipCode = RandomInfoUtils.getZipCode();
        contactPage.enterZipCode(zipCode);

        String country = "Australia";

        contactPage.selectCountry(country);

        String email = RandomInfoUtils.getEmail();
        contactPage.enterEmail(email);

        contactPage.clickOnSaveBtn();

        Utils.updateProperty(fileLocation, jsonId-1, "streetaddress", streetAddress);
        Utils.updateProperty(fileLocation, jsonId-1, "entercity", city);
        Utils.updateProperty(fileLocation, jsonId-1, "state", state);
        Utils.updateProperty(fileLocation, jsonId-1, "zipcode", zipCode);
        Utils.updateProperty(fileLocation, jsonId-1, "country", country);
        Utils.updateProperty(fileLocation, jsonId-1, "email", email);
    }
}