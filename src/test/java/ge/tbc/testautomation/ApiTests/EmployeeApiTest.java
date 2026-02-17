package ge.tbc.testautomation.ApiTests;

import com.example.springboot.soap.interfaces.*;
import ge.tbc.testautomation.data.Constants;
import ge.tbc.testautomation.data.Marshall;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EmployeeApiTest {

    private ObjectFactory objectFactory;
    private Long employeeId;

    @BeforeClass
    public void setup() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = Constants.LOCAL_HOST_URL;
        objectFactory = new ObjectFactory();
    }

    @BeforeMethod
    public void setUp() {
        employeeId = System.currentTimeMillis() % 100000;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            DeleteEmployeeRequest deleteRequest = objectFactory.createDeleteEmployeeRequest();
            deleteRequest.setEmployeeId(employeeId);
            given().header(Constants.CONTENT_TYPE, Constants.TEXT_XML)
                    .body(Marshall.marshallSoapRequest(deleteRequest))
                    .post();
        } catch (Exception e) {}
    }

    @Test
    public void addEmployeeTest() throws DatatypeConfigurationException {
        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(Constants.BIRTHDATE_FOR_TAMARI);

        AddEmployeeRequest addEmployeeReq = objectFactory.createAddEmployeeRequest();
        EmployeeInfo employeeInfo = objectFactory.createEmployeeInfo();
        employeeInfo.setEmployeeId(employeeId);
        employeeInfo.setName(Constants.NAME);
        employeeInfo.setEmail(Constants.GMAIL);
        employeeInfo.setAddress(Constants.TBILISI);
        employeeInfo.setDepartment(Constants.IT);
        employeeInfo.setPhone(Constants.PHONE_FOR_TAMARI);
        employeeInfo.setSalary(new BigDecimal(5000));
        employeeInfo.setBirthDate(xmlDate);
        addEmployeeReq.setEmployeeInfo(employeeInfo);

        given().header(Constants.CONTENT_TYPE, Constants.TEXT_XML)
                .body(Marshall.marshallSoapRequest(addEmployeeReq))
                .post()
                .then()
                .statusCode(200)
                .body(Constants.EMPLOYEE_ADD_MESSAGE, equalTo(Constants.EMPLOYEE_ADD_SUCCESSFULLY));
    }

    @Test(dependsOnMethods = "addEmployeeTest")
    public void getEmployeeTest() throws DatatypeConfigurationException {
        addEmployee(employeeId, Constants.NAME, Constants.GMAIL, Constants.BIRTHDATE_FOR_TAMARI);

        GetEmployeeByIdRequest getRequest = objectFactory.createGetEmployeeByIdRequest();
        getRequest.setEmployeeId(employeeId);

        Response response = given().header(Constants.CONTENT_TYPE, Constants.TEXT_XML)
                .body(Marshall.marshallSoapRequest(getRequest))
                .post()
                .then()
                .statusCode(200)
                .extract().response();

        GetEmployeeByIdResponse employeeResponse = Marshall.unmarshallSoapResponse(
                response.asString(), GetEmployeeByIdResponse.class);
        EmployeeInfo employee = employeeResponse.getEmployeeInfo();

        assertThat(employee.getEmployeeId(), equalTo(employeeId));
        assertThat(employee.getName(), equalTo(Constants.NAME));
        assertThat(employee.getEmail(), equalTo(Constants.GMAIL));
        assertThat(employee.getDepartment(), equalTo(Constants.IT));
        assertThat(employee.getSalary(), equalTo(new BigDecimal(5000)));
        assertThat(employee.getAddress(), equalTo(Constants.TBILISI));
        assertThat(employee.getPhone(), equalTo(Constants.PHONE_FOR_TAMARI));
        assertThat(employee.getBirthDate().toString(), equalTo(Constants.BIRTHDATE_FOR_TAMARI));
    }

    @Test(dependsOnMethods = "addEmployeeTest")
    public void updateEmployeeTest() throws DatatypeConfigurationException {
        addEmployee(employeeId, Constants.NAME, Constants.GMAIL, Constants.BIRTHDATE_FOR_TAMARI);

        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(Constants.BIRTHDATE_UPDATED);

        UpdateEmployeeRequest updateRequest = objectFactory.createUpdateEmployeeRequest();
        EmployeeInfo employeeInfo = objectFactory.createEmployeeInfo();
        employeeInfo.setEmployeeId(employeeId);
        employeeInfo.setName(Constants.NAME_UPDATED);
        employeeInfo.setEmail(Constants.GMAIL_UPDATED);
        employeeInfo.setAddress(Constants.SABURTALO);
        employeeInfo.setDepartment(Constants.DEPT_UPDATED);
        employeeInfo.setPhone(Constants.PHONE_UPDATED);
        employeeInfo.setSalary(new BigDecimal(7000));
        employeeInfo.setBirthDate(xmlDate);
        updateRequest.setEmployeeInfo(employeeInfo);

        Response updateResponse = given().header(Constants.CONTENT_TYPE, Constants.TEXT_XML)
                .body(Marshall.marshallSoapRequest(updateRequest))
                .post()
                .then()
                .statusCode(200)
                .extract().response();

        UpdateEmployeeResponse updateResult = Marshall.unmarshallSoapResponse(
                updateResponse.asString(), UpdateEmployeeResponse.class);
        assertThat(updateResult.getServiceStatus().getMessage(),
                equalTo(Constants.EMPLOYEE_UPDATE_SUCCESSFULLY));

        GetEmployeeByIdRequest getRequest = objectFactory.createGetEmployeeByIdRequest();
        getRequest.setEmployeeId(employeeId);

        Response getResponse = given().header(Constants.CONTENT_TYPE, Constants.TEXT_XML)
                .body(Marshall.marshallSoapRequest(getRequest))
                .post()
                .then()
                .statusCode(200)
                .extract().response();

        GetEmployeeByIdResponse employeeResponse = Marshall.unmarshallSoapResponse(
                getResponse.asString(), GetEmployeeByIdResponse.class);
        EmployeeInfo updatedEmployee = employeeResponse.getEmployeeInfo();

        assertThat(updatedEmployee.getEmployeeId(), equalTo(employeeId));
        assertThat(updatedEmployee.getName(), equalTo(Constants.NAME_UPDATED));
        assertThat(updatedEmployee.getEmail(), equalTo(Constants.GMAIL_UPDATED));
        assertThat(updatedEmployee.getDepartment(), equalTo(Constants.DEPT_UPDATED));
        assertThat(updatedEmployee.getSalary(), equalTo(new BigDecimal(7000)));
        assertThat(updatedEmployee.getAddress(), equalTo(Constants.SABURTALO));
        assertThat(updatedEmployee.getPhone(), equalTo(Constants.PHONE_UPDATED));
    }

    @Test(dependsOnMethods = "addEmployeeTest")
    public void deleteEmployeeTest() throws DatatypeConfigurationException {
        addEmployee(employeeId, Constants.NAME, Constants.GMAIL, Constants.BIRTHDATE_FOR_TAMARI);

        DeleteEmployeeRequest deleteRequest = objectFactory.createDeleteEmployeeRequest();
        deleteRequest.setEmployeeId(employeeId);

        given().header(Constants.CONTENT_TYPE, Constants.TEXT_XML)
                .body(Marshall.marshallSoapRequest(deleteRequest))
                .post()
                .then()
                .statusCode(200)
                .body(Constants.EMPLOYEE_DELETE_MESSAGE, equalTo(Constants.EMPLOYEE_DELETE_SUCCESSFULLY));

        GetEmployeeByIdRequest getRequest = objectFactory.createGetEmployeeByIdRequest();
        getRequest.setEmployeeId(employeeId);

        given().header(Constants.CONTENT_TYPE, Constants.TEXT_XML)
                .body(Marshall.marshallSoapRequest(getRequest))
                .post()
                .then()
                .statusCode(500)
                .body(Constants.FAIL_STRING, not(emptyString()));
    }

    private void addEmployee(Long id, String name, String email, String birthDateStr)
            throws DatatypeConfigurationException {
        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(birthDateStr);

        AddEmployeeRequest addEmployeeReq = objectFactory.createAddEmployeeRequest();
        EmployeeInfo employeeInfo = objectFactory.createEmployeeInfo();
        employeeInfo.setEmployeeId(id);
        employeeInfo.setName(name);
        employeeInfo.setEmail(email);
        employeeInfo.setAddress(Constants.TBILISI);
        employeeInfo.setDepartment(Constants.IT);
        employeeInfo.setPhone(Constants.PHONE_FOR_TAMARI);
        employeeInfo.setSalary(new BigDecimal(5000));
        employeeInfo.setBirthDate(xmlDate);
        addEmployeeReq.setEmployeeInfo(employeeInfo);

        given().header(Constants.CONTENT_TYPE, Constants.TEXT_XML)
                .body(Marshall.marshallSoapRequest(addEmployeeReq))
                .post()
                .then()
                .statusCode(200);
    }
}