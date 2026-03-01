package ge.tbc.testautomation.data;

import java.util.List;
import java.util.Map;

public class Constants {


    public static final String LOCAL_HOST_URL = "http://localhost:8087/ws";
    public static final String CONTINENT_URL = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso/ListOfContinentsByName";


    public static final String CONTENT_TYPE = "Content-Type";
    public static final String TEXT_XML = "text/xml; charset=utf-8";


    public static final String NAME = "Tamari";
    public static final String GMAIL = "tamari@gmail.com";
    public static final String TBILISI = "Tbilisi";
    public static final String IT = "IT";
    public static final String PHONE_FOR_TAMARI = "577 123 321";
    public static final String BIRTHDATE_FOR_TAMARI = "2006-02-04";


    public static final String NAME_UPDATED = "Tamari ";
    public static final String GMAIL_UPDATED = "tamari.@gmail.com";
    public static final String SABURTALO = "Saburtalo";
    public static final String DEPT_UPDATED = "IT";
    public static final String PHONE_UPDATED = "577 999 888";
    public static final String BIRTHDATE_UPDATED = "2006-02-04";


    public static final String EMPLOYEE_ADD_MESSAGE = "Envelope.Body.addEmployeeResponse.serviceStatus.message";
    public static final String EMPLOYEE_DELETE_MESSAGE = "Envelope.Body.deleteEmployeeResponse.serviceStatus.message";
    public static final String EMPLOYEE_ID = "Envelope.Body.getEmployeeByIdResponse.employeeInfo.employeeId";
    public static final String EMPLOYEE_NAME = "Envelope.Body.getEmployeeByIdResponse.employeeInfo.name";
    public static final String EMPLOYEE_EMAIL = "Envelope.Body.getEmployeeByIdResponse.employeeInfo.email";
    public static final String EMPLOYEE_DEPT = "Envelope.Body.getEmployeeByIdResponse.employeeInfo.department";
    public static final String EMPLOYEE_SALARY = "Envelope.Body.getEmployeeByIdResponse.employeeInfo.salary";
    public static final String EMPLOYEE_ADDRESS = "Envelope.Body.getEmployeeByIdResponse.employeeInfo.address";
    public static final String EMPLOYEE_PHONE = "Envelope.Body.getEmployeeByIdResponse.employeeInfo.phone";
    public static final String EMPLOYEE_BIRTHDATE = "Envelope.Body.getEmployeeByIdResponse.employeeInfo.birthDate";
    public static final String FAIL_STRING = "Envelope.Body.Fault.faultstring";


    public static final String EMPLOYEE_ADD_SUCCESSFULLY = "Content Added Successfully";
    public static final String EMPLOYEE_UPDATE_SUCCESSFULLY = "Content Updated Successfully";
    public static final String EMPLOYEE_DELETE_SUCCESSFULLY = "Content Deleted Successfully";
    public static final String EMPLOYEE_NOT_FOUND = "Employee not found";
    public static final String SOURCE_NOT_NULL = "Source must not be null";


    public static final String SEVEN = "7";
    public static final String FIVE_THOUSAND = "5000.00";
    public static final String SEVEN_THOUSAND = "7000.00";


    public static final List<String> EXPECTED_CONTINENT_NAMES = List.of(
            "Africa",
            "Antarctica",
            "Asia",
            "Europe",
            "Ocenania",
            "The Americas"
    );

    public static final List<String> EXPECTED_CONTINENT_CODES = List.of(
            "AF", "AN", "AS", "EU", "OC", "AM"
    );

    public static final Map<String, String> CONTINENT_CODE_NAME_MAP = Map.of(
            "AF", "Africa",
            "AN", "Antarctica",
            "AS", "Asia",
            "EU", "Europe",
            "OC", "Ocenania",
            "AM", "The Americas"
    );


    public static final String SNAME_NO_NUMBERS_PATTERN = "^[^0-9]*$";
    public static final String SCODE_TWO_UPPERCASE_PATTERN = "^[A-Z]{2}$";
}