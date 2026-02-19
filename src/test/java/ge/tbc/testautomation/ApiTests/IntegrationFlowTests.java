package ge.tbc.testautomation.ApiTests;

import ge.tbc.testautomation.model.Employee;
import ge.tbc.testautomation.model.User;
import ge.tbc.testautomation.steps.DatabaseSteps;
import ge.tbc.testautomation.steps.RestSteps;
import ge.tbc.testautomation.steps.SoapSteps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.annotations.*;

import javax.xml.soap.SOAPMessage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class IntegrationFlowTests {
    private SoapSteps soapSteps;
    private RestSteps restSteps;
    private DatabaseSteps dbSteps;

    private String testEmail;
    private String password = "TestPassword123!";
    private String newPassword = "NewPassword456!";
    private String newEmail;
    private String jwtToken;
    private Long employeeId;

    @BeforeClass
    public void setup() {
        soapSteps = new SoapSteps();
        restSteps = new RestSteps();
        dbSteps = new DatabaseSteps();


        testEmail = "test." + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        newEmail = "new." + UUID.randomUUID().toString().substring(0, 8) + "@example.com";


        Employee employee = new Employee();
        employee.setName("Integration Test");
        employee.setEmail(testEmail);
        employee.setDepartment("QA");
        employee.setPhone("555-0000");
        employee.setAddress("123 Test St");
        employee.setSalary(new BigDecimal("55000.00"));
        employee.setBirthDate(LocalDate.of(1993, 5, 15));

        dbSteps.insertEmployee(employee);
        employeeId = employee.getEmployeeId();
    }

    @Test(priority = 1)
    @Description("Step 1: Negative test - Register with non-existing email")
    public void test01_NegativeRegistration() {
        String randomEmail = "nonexisting." + UUID.randomUUID() + "@example.com";
        Response response = restSteps.registerUser(randomEmail, password);


        assertThat("Response should indicate error",
                response.getStatusCode(), anyOf(equalTo(400), equalTo(404), equalTo(500)));
    }

    @Test(priority = 2)
    @Description("Step 2: Valid registration with existing employee email")
    public void test02_ValidRegistration() {
        Response response = restSteps.registerUser(testEmail, password);

        assertThat("Registration should be successful",
                response.getStatusCode(), either(equalTo(200)).or(equalTo(201)));

        jwtToken = restSteps.extractToken(response);
        assertThat("JWT token should not be null", jwtToken, is(notNullValue()));
    }

    @Test(priority = 3, dependsOnMethods = "test02_ValidRegistration")
    @Description("Step 3: Access protected resource with JWT token")
    public void test03_AccessProtectedResource() {
        Response response = restSteps.accessProtectedResource(jwtToken);
        assertThat("Should access protected resource", response.getStatusCode(), equalTo(200));
    }

    @Test(priority = 4, dependsOnMethods = "test03_AccessProtectedResource")
    @Description("Step 4: Change password")
    public void test04_ChangePassword() {
        Response response = restSteps.changePassword(password, newPassword, jwtToken);
        assertThat("Password change should be successful",
                response.getStatusCode(), either(equalTo(200)).or(equalTo(204)));
    }

    @Test(priority = 5, dependsOnMethods = "test04_ChangePassword")
    @Description("Step 5: Logout")
    public void test05_Logout() {
        Response response = restSteps.logout(jwtToken);
        assertThat("Logout should be successful",
                response.getStatusCode(), either(equalTo(200)).or(equalTo(204)));
    }

    @Test(priority = 6, dependsOnMethods = "test05_Logout")
    @Description("Step 6: Re-authenticate with new password")
    public void test06_ReAuthenticate() {
        Response response = restSteps.authenticateUser(testEmail, newPassword);
        assertThat("Authentication should be successful", response.getStatusCode(), equalTo(200));

        jwtToken = restSteps.extractToken(response);
        assertThat("New JWT token should not be null", jwtToken, is(notNullValue()));
    }

    @Test(priority = 7, dependsOnMethods = "test06_ReAuthenticate")
    @Description("Step 7: Change email")
    public void test07_ChangeEmail() {
        Response response = restSteps.changeEmail(newEmail, jwtToken);
        assertThat("Email change should be successful",
                response.getStatusCode(), either(equalTo(200)).or(equalTo(204)));
    }

    @Test(priority = 8, dependsOnMethods = "test07_ChangeEmail")
    @Description("Step 8: Post-change authentication with new email")
    public void test08_PostChangeAuthentication() {
        Response response = restSteps.authenticateUser(newEmail, newPassword);
        assertThat("Authentication with new email should be successful",
                response.getStatusCode(), equalTo(200));

        jwtToken = restSteps.extractToken(response);
        assertThat("JWT token should not be null", jwtToken, is(notNullValue()));
    }

    @Test(priority = 9, dependsOnMethods = "test08_PostChangeAuthentication")
    @Description("Step 9: SOAP email update validation")
    public void test09_SOAPEmailValidation() throws Exception {
        SOAPMessage response = soapSteps.getEmployeeById(employeeId);
        assertThat("SOAP response should not be null", response, is(notNullValue()));

        String emailFromSoap = soapSteps.extractEmailFromResponse(response);
        assertThat("Email from SOAP should match new email", emailFromSoap, equalTo(newEmail));

        Employee employee = dbSteps.getEmployeeById(employeeId);
        assertThat("Employee email should be updated in employee table",
                employee.getEmail(), equalTo(newEmail));
    }

    @Test(priority = 10, dependsOnMethods = "test09_SOAPEmailValidation")
    @Description("Step 10: Database validation")
    public void test10_DatabaseValidation() {

        User user = dbSteps.getUserByEmail(newEmail);
        assertThat("User should exist with new email", user, is(notNullValue()));
        assertThat("User email should match", user.getEmail(), equalTo(newEmail));


        Employee employee = dbSteps.getEmployeeById(employeeId);
        assertThat("Employee should exist", employee, is(notNullValue()));
        assertThat("Employee email should be updated", employee.getEmail(), equalTo(newEmail));
    }
}