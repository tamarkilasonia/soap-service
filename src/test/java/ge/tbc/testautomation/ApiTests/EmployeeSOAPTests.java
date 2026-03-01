package ge.tbc.testautomation.ApiTests;

import ge.tbc.testautomation.model.Employee;
import ge.tbc.testautomation.steps.DatabaseSteps;
import ge.tbc.testautomation.steps.SoapSteps;
import io.qameta.allure.Description;
import org.testng.annotations.*;

import javax.xml.soap.SOAPMessage;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EmployeeSOAPTests {
    private SoapSteps soapSteps;
    private DatabaseSteps dbSteps;
    private Long testEmployeeId;

    @BeforeClass
    public void setup() {
        soapSteps = new SoapSteps();
        dbSteps = new DatabaseSteps();
    }

    @Test(priority = 1)
    @Description("Task 1.1: Insert new employee in database using MyBatis")
    public void testInsertEmployeeInDB() {
        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("IT");
        employee.setPhone("555-1234");
        employee.setAddress("123 Main St");
        employee.setSalary(new BigDecimal("50000.00"));
        employee.setBirthDate(LocalDate.of(1990, 1, 15));

        dbSteps.insertEmployee(employee);

        testEmployeeId = employee.getEmployeeId();

        assertThat("Employee ID should be generated", testEmployeeId, is(notNullValue()));
        assertThat("Employee ID should be greater than 0", testEmployeeId, greaterThan(0L));
    }

    @Test(priority = 2, dependsOnMethods = "testInsertEmployeeInDB")
    @Description("Task 1.2: Get employee by ID via SOAP and validate with database")
    public void testGetEmployeeByIdSOAP() throws Exception {
        SOAPMessage response = soapSteps.getEmployeeById(testEmployeeId);

        Employee dbEmployee = dbSteps.getEmployeeById(testEmployeeId);

        assertThat("SOAP response should not be null", response, is(notNullValue()));
        assertThat("Database employee should exist", dbEmployee, is(notNullValue()));
        assertThat("Employee email should match", dbEmployee.getEmail(), equalTo("john.doe@example.com"));
        assertThat("Employee name should match", dbEmployee.getName(), equalTo("John Doe"));
    }

    @Test(priority = 3, dependsOnMethods = "testGetEmployeeByIdSOAP")
    @Description("Task 1.3: Update employee via SOAP service and validate in database")
    public void testUpdateEmployeeViaSOAP() throws Exception {
        // ✅ ყველა 8 არგუმენტი!
        SOAPMessage response = soapSteps.updateEmployee(
                testEmployeeId,
                "Jane Smith",
                "HR",
                "555-5678",
                "456 Oak Ave",
                new BigDecimal("60000.00"),
                "jane.smith@example.com",
                LocalDate.of(1992, 3, 20)
        );

        assertThat("SOAP response should not be null", response, is(notNullValue()));

        Employee updatedEmployee = dbSteps.getEmployeeById(testEmployeeId);
        assertThat("Name should be updated", updatedEmployee.getName(), equalTo("Jane Smith"));
        assertThat("Email should be updated", updatedEmployee.getEmail(), equalTo("jane.smith@example.com"));
        assertThat("Department should be updated", updatedEmployee.getDepartment(), equalTo("HR"));
        assertThat("Phone should be updated", updatedEmployee.getPhone(), equalTo("555-5678"));
        assertThat("Salary should be updated", updatedEmployee.getSalary(), equalTo(new BigDecimal("60000.00")));
    }

    @Test(priority = 4, dependsOnMethods = "testUpdateEmployeeViaSOAP")
    @Description("Task 1.4: Update employee via database and validate via SOAP")
    public void testUpdateEmployeeViaDatabase() throws Exception {
        Employee employee = dbSteps.getEmployeeById(testEmployeeId);
        employee.setName("Updated Name");
        employee.setEmail("updated.name@example.com");

        int result = dbSteps.updateEmployee(employee);

        assertThat("Update should affect 1 row", result, equalTo(1));

        SOAPMessage response = soapSteps.getEmployeeById(testEmployeeId);
        assertThat("SOAP response should not be null", response, is(notNullValue()));

        Employee verifiedEmployee = dbSteps.getEmployeeById(testEmployeeId);
        assertThat("Name should match", verifiedEmployee.getName(), equalTo("Updated Name"));
        assertThat("Email should match", verifiedEmployee.getEmail(), equalTo("updated.name@example.com"));
    }

    @Test(priority = 5, dependsOnMethods = "testUpdateEmployeeViaDatabase")
    @Description("Task 1.5 (Optional): Delete employee via SOAP and verify in database")
    public void testDeleteEmployeeViaSOAP() throws Exception {

        SOAPMessage response = soapSteps.deleteEmployeeById(testEmployeeId);

        assertThat("SOAP response should not be null", response, is(notNullValue()));

        int count = dbSteps.countEmployeeById(testEmployeeId);
        assertThat("Employee should be deleted", count, equalTo(0));
    }
}