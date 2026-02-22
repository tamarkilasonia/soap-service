package ge.tbc.testautomation.data;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Marshall {

    public static <T> String marshallSoapRequest(T object) {
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();

            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(object, new DOMResult(soapPart.getEnvelope().getBody()));

            Transformer ts = TransformerFactory.newInstance().newTransformer();
            Properties properties = new Properties();
            properties.setProperty("indent", "yes");
            properties.setProperty("omit-xml-declaration", "yes");
            ts.setOutputProperties(properties);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ts.transform(soapMessage.getSOAPPart().getContent(), new StreamResult(output));

            return output.toString(StandardCharsets.UTF_8);
        } catch (JAXBException | SOAPException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T unmarshallSoapResponse(String xmlResponse, Class<T> responseClass) {
        try {
            // Make tag name lowercase first letter
            String className = responseClass.getSimpleName();
            String tagNameLower = Character.toLowerCase(className.charAt(0)) + className.substring(1);

            String responseBody = extractResponseBody(xmlResponse, tagNameLower, className);

            JAXBContext jaxbContext = JAXBContext.newInstance(responseClass);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new StringReader(responseBody));
        } catch (JAXBException e) {
            throw new RuntimeException("Unmarshalling failed: " + e.getMessage(), e);
        }
    }

    private static String extractResponseBody(String soapResponse, String tagLower, String tagUpper) {
        int start = soapResponse.indexOf("<" + tagLower);
        if (start == -1) start = soapResponse.indexOf("<ns2:" + tagLower);
        if (start == -1) start = soapResponse.indexOf("<" + tagUpper);
        if (start == -1) throw new RuntimeException("Response tag not found: " + tagLower);

        int end = soapResponse.indexOf("</" + tagLower + ">", start);
        if (end == -1) end = soapResponse.indexOf("</ns2:" + tagLower + ">", start);
        if (end == -1) end = soapResponse.indexOf("</" + tagUpper + ">", start);
        if (end == -1) throw new RuntimeException("Closing tag not found");

        end = soapResponse.indexOf(">", end) + 1;

        return soapResponse.substring(start, end)
                .replaceAll("<ns2:", "<")
                .replaceAll("</ns2:", "</")
                .replaceAll("xmlns:ns2=\"[^\"]*\"", "")
                .replaceAll("<" + tagLower, "<" + tagUpper)
                .replaceAll("</" + tagLower, "</" + tagUpper)
                .trim();
    }
}