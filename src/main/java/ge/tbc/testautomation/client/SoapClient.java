package ge.tbc.testautomation.client;

import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;

public class SoapClient {
    private static final Logger logger = LoggerFactory.getLogger(SoapClient.class);
    private static final String SOAP_ENDPOINT = "http://localhost:8087/ws";
    private static final String NAMESPACE_URI = "http://example.com/employees";

    @Step("Send SOAP Request: {operation}")
    public SOAPMessage sendSoapRequest(String operation, SOAPElement requestBody) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPBody soapBody = envelope.getBody();

        soapBody.addChildElement(requestBody);

        soapMessage.saveChanges();


        logSoapMessage("SOAP Request", soapMessage);


        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        SOAPMessage soapResponse = soapConnection.call(soapMessage, SOAP_ENDPOINT);


        logSoapMessage("SOAP Response", soapResponse);

        soapConnection.close();

        return soapResponse;
    }

    @Step("Create SOAP Element: {elementName}")
    public SOAPElement createSOAPElement(String elementName) throws SOAPException {
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        return soapFactory.createElement(new QName(NAMESPACE_URI, elementName));
    }

    private void logSoapMessage(String title, SOAPMessage message) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            message.writeTo(out);
            logger.info("{}: \n{}", title, out.toString());
        } catch (Exception e) {
            logger.error("Error logging SOAP message", e);
        }
    }
}