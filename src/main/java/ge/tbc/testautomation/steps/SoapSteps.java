package ge.tbc.testautomation.steps;

import ge.tbc.testautomation.client.SoapClient;
import io.qameta.allure.Step;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;

public class SoapSteps {
    private final SoapClient soapClient;

    public SoapSteps() {
        this.soapClient = new SoapClient();
    }

    @Step("SOAP: Get Employee By ID - {employeeId}")
    public SOAPMessage getEmployeeById(Long employeeId) throws Exception {
        SOAPElement getRequest = soapClient.createSOAPElement("getEmployeeByIdRequest");
        getRequest.addChildElement("employeeId").addTextNode(employeeId.toString());
        return soapClient.sendSoapRequest("getEmployeeById", getRequest);
    }

    @Step("SOAP: Get Employee By Email - {email}")
    public SOAPMessage getEmployeeByEmail(String email) throws Exception {
        SOAPElement getRequest = soapClient.createSOAPElement("getEmployeeByEmailRequest");
        getRequest.addChildElement("email").addTextNode(email);
        return soapClient.sendSoapRequest("getEmployeeByEmail", getRequest);
    }

    @Step("SOAP: Add Employee - {email}")
    public SOAPMessage addEmployee(String name, String department, String phone,
                                   String address, BigDecimal salary, String email,
                                   LocalDate birthDate) throws Exception {
        SOAPElement addRequest = soapClient.createSOAPElement("addEmployeeRequest");
        SOAPElement employeeInfo = addRequest.addChildElement("employeeInfo");

        employeeInfo.addChildElement("name").addTextNode(name);
        employeeInfo.addChildElement("department").addTextNode(department);
        employeeInfo.addChildElement("phone").addTextNode(phone != null ? phone : "");
        employeeInfo.addChildElement("address").addTextNode(address != null ? address : "");
        employeeInfo.addChildElement("salary").addTextNode(salary.toString());
        employeeInfo.addChildElement("email").addTextNode(email);
        employeeInfo.addChildElement("birthDate").addTextNode(birthDate.toString());

        return soapClient.sendSoapRequest("addEmployee", addRequest);
    }

    @Step("SOAP: Update Employee - ID: {employeeId}")
    public SOAPMessage updateEmployee(Long employeeId, String name, String department,
                                      String phone, String address, BigDecimal salary,
                                      String email, LocalDate birthDate) throws Exception {
        SOAPElement updateRequest = soapClient.createSOAPElement("updateEmployeeRequest");
        SOAPElement employeeInfo = updateRequest.addChildElement("employeeInfo");

        employeeInfo.addChildElement("employeeId").addTextNode(employeeId.toString());
        employeeInfo.addChildElement("name").addTextNode(name);
        employeeInfo.addChildElement("department").addTextNode(department);
        employeeInfo.addChildElement("phone").addTextNode(phone != null ? phone : "");
        employeeInfo.addChildElement("address").addTextNode(address != null ? address : "");
        employeeInfo.addChildElement("salary").addTextNode(salary.toString());
        employeeInfo.addChildElement("email").addTextNode(email);
        employeeInfo.addChildElement("birthDate").addTextNode(birthDate.toString());

        return soapClient.sendSoapRequest("updateEmployee", updateRequest);
    }

    @Step("SOAP: Delete Employee - ID: {employeeId}")
    public SOAPMessage deleteEmployeeById(Long employeeId) throws Exception {
        SOAPElement deleteRequest = soapClient.createSOAPElement("deleteEmployeeRequest");
        deleteRequest.addChildElement("employeeId").addTextNode(employeeId.toString());
        return soapClient.sendSoapRequest("deleteEmployee", deleteRequest);
    }

    @Step("SOAP: Delete Employee - Email: {email}")
    public SOAPMessage deleteEmployeeByEmail(String email) throws Exception {
        SOAPElement deleteRequest = soapClient.createSOAPElement("deleteEmployeeRequest");
        deleteRequest.addChildElement("email").addTextNode(email);
        return soapClient.sendSoapRequest("deleteEmployee", deleteRequest);
    }

    @Step("Extract email from SOAP response")
    public String extractEmailFromResponse(SOAPMessage response) throws Exception {
        return extractFieldFromResponse(response, "email");
    }

    @Step("Extract employeeId from SOAP response")
    public Long extractEmployeeIdFromResponse(SOAPMessage response) throws Exception {
        String id = extractFieldFromResponse(response, "employeeId");
        return id != null ? Long.parseLong(id) : null;
    }

    private String extractFieldFromResponse(SOAPMessage response, String fieldName) throws Exception {
        SOAPBody body = response.getSOAPBody();
        Iterator<?> iterator = body.getChildElements();
        while (iterator.hasNext()) {
            Object node = iterator.next();
            if (node instanceof SOAPElement) {
                SOAPElement element = (SOAPElement) node;
                return extractFromElement(element, fieldName);
            }
        }
        return null;
    }

    private String extractFromElement(SOAPElement element, String fieldName) {
        Iterator<?> childIterator = element.getChildElements();
        while (childIterator.hasNext()) {
            Object childNode = childIterator.next();
            if (childNode instanceof SOAPElement) {
                SOAPElement child = (SOAPElement) childNode;
                if (fieldName.equals(child.getLocalName())) {
                    return child.getTextContent();
                }
                String result = extractFromElement(child, fieldName);
                if (result != null) return result;
            }
        }
        return null;
    }
}