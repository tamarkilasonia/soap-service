package ge.tbc.testautomation.ApiTests;

import ge.tbc.testautomation.model.Employee;
import ge.tbc.testautomation.model.User;
import ge.tbc.testautomation.steps.DatabaseSteps;
import ge.tbc.testautomation.steps.RestSteps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DataPreparationTests {
    private RestSteps restSteps;
    private DatabaseSteps dbSteps;

    private String uniqueEmail;
    private String password = "TestPassword123!";
    private String jwtToken;
    private Long employeeId;

    @BeforeClass
    public void setup() {
        restSteps = new RestSteps();
        dbSteps = new DatabaseSteps();

        uniqueEmail = "prep." + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    @Test(priority = 1)
    @Description("Step 1: Insert test data - new employee with unique email")
    public void test01_InsertTestData() {
        Employee employee = new Employee();
        employee.setName("DataPrep TestUser");
        employee.setEmail(uniqueEmail);
        employee.setDepartment("Testing");
        employee.setPhone("555-9999");
        employee.setAddress("789 Test Blvd");
        employee.setSalary(new BigDecimal("45000.00"));
        employee.setBirthDate(LocalDate.of(1995, 6, 10));

        dbSteps.insertEmployee(employee);
        employeeId = employee.getEmployeeId();

        assertThat("Employee ID should be generated", employeeId, is(notNullValue()));
        assertThat("Employee ID should be greater than 0", employeeId, greaterThan(0L));

        Employee dbEmployee = dbSteps.getEmployeeById(employeeId);
        assertThat("Employee should exist in database", dbEmployee, is(notNullValue()));
        assertThat("Email should match", dbEmployee.getEmail(), equalTo(uniqueEmail));
        assertThat("Name should match", dbEmployee.getName(), equalTo("DataPrep TestUser"));
    }

    @Test(priority = 2, dependsOnMethods = "test01_InsertTestData")
    @Description("Step 2: Register user with inserted email")
    public void test02_RegisterUser() {
        Response response = restSteps.registerUser(uniqueEmail, password);

        assertThat("Registration should be successful",
                response.getStatusCode(), either(equalTo(200)).or(equalTo(201)));

        jwtToken = restSteps.extractToken(response);
        assertThat("JWT token should not be null", jwtToken, is(notNullValue()));
        assertThat("JWT token should not be empty", jwtToken, not(emptyString()));
    }

    @Test(priority = 3, dependsOnMethods = "test02_RegisterUser")
    @Description("Step 3: Access protected endpoint and validate access")
    public void test03_AccessProtectedEndpoint() {
        Response response = restSteps.accessProtectedResource(jwtToken);
        assertThat("Should successfully access protected resource",
                response.getStatusCode(), equalTo(200));
    }

    @Test(priority = 4, dependsOnMethods = "test03_AccessProtectedEndpoint")
    @Description("Step 4: Database verification - user registered with correct email")
    public void test04_DatabaseVerification() {
        User user = dbSteps.getUserByEmail(uniqueEmail);

        assertThat("User should exist in database", user, is(notNullValue()));
        assertThat("User email should match", user.getEmail(), equalTo(uniqueEmail));
        assertThat("User should have a role", user.getRole(), is(notNullValue()));
    }
}