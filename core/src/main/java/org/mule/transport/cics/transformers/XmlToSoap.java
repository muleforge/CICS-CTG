package org.mule.transport.cics.transformers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.EndElement;

import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.MuleMessage;
import org.mule.api.ExceptionPayload;
import org.mule.api.transformer.TransformerException;

import org.mule.transport.cics.util.Constants;

/**
 * This transformer converts the XML message into a SOAP message i.e it adds the
 * XML tags for <soap:Envelope> and <soap:Body>
 */
public class XmlToSoap extends AbstractMessageAwareTransformer {

    private static final String SOAP_ENCODING = "UTF-8";
    private static final String SOAP_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";

	private static XMLInputFactory  inputFactory;
	private static XMLOutputFactory outputFactory;
	private static XMLEventFactory  eventFactory;

    private static StartElement envelopeStart;
    private static EndElement   envelopeEnd;
    private static StartElement bodyStart;
    private static EndElement   bodyEnd;
    private static StartElement faultStart;
    private static EndElement   faultEnd;
    private static StartElement detailStart;
    private static EndElement   detailEnd;

	static {
		inputFactory = XMLInputFactory.newInstance();
		outputFactory = XMLOutputFactory.newInstance();
		eventFactory = XMLEventFactory.newInstance();
		outputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);

		envelopeStart = eventFactory.createStartElement("soapenv", SOAP_NAMESPACE, "Envelope"); 
		envelopeEnd = eventFactory.createEndElement("soapenv", SOAP_NAMESPACE, "Envelope"); 
	    bodyStart = eventFactory.createStartElement("soapenv", SOAP_NAMESPACE, "Body"); 
		bodyEnd = eventFactory.createEndElement("soapenv", SOAP_NAMESPACE, "Body");
	    faultStart = eventFactory.createStartElement("soapenv", SOAP_NAMESPACE, "Fault"); 
		faultEnd = eventFactory.createEndElement("soapenv", SOAP_NAMESPACE, "Fault");
	    detailStart = eventFactory.createStartElement("", "", "detail"); 
		detailEnd = eventFactory.createEndElement("", "", "detail");
	}

	public Object transform(MuleMessage message, String encoding) throws TransformerException {

        try {
		  // Do not do any processing if returning back WSDL to client.
		  if (message.getBooleanProperty(Constants.SKIP_FURTHER_PROCESSING, false)) {
			  return message;
		  }

          boolean isFaultMessage = (message.getExceptionPayload() != null);

          byte[] xmlBytes = message.getPayloadAsBytes();
          byte[] soapResponse = transform(xmlBytes, isFaultMessage);
          return soapResponse;

		} catch (Throwable ex) {
			logger.error(ex);
			byte[] faultReply = generateSoapFaultReply(ex);
			return faultReply;
		}
    }
            
    protected byte[] transform(byte[] xmlBytes, boolean isFaultMessage) 
              throws Exception {

        ByteArrayInputStream bis = new ByteArrayInputStream(xmlBytes);
        XMLEventReader reader = inputFactory.createXMLEventReader(bis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLEventWriter writer = outputFactory.createXMLEventWriter(bos);

        boolean writeEvent = true;
        while (reader.hasNext()) {
            XMLEvent event = (XMLEvent) reader.next();

            if (event.isStartDocument()) {
                writer.add(event);          // START_DOCUMENT
                writer.add(envelopeStart);  // START - <soapEnv:Envelope>
                writer.add(bodyStart);      // START - <soapEnv:Body>
                if (isFaultMessage) {
                    writer.add(faultStart); // START - <soapEnv:Fault>
                    writer.add(detailStart); // START - <detail>
                }
                continue;
            }

            if (event.isEndDocument()) {
                if (isFaultMessage) {
                    writer.add(detailEnd); // END - </detail>
                    writer.add(faultEnd); // END - </soapEnv:Fault>
                }
                writer.add(bodyEnd);      // END - </soapEnv:Body>
                writer.add(envelopeEnd);  // END - </soapEnv:Envelope>
                writer.add(event);        // END_DOCUMENT
                continue;
            }

            String elementName = "";
            if (event.isStartElement()) {
                elementName = event.asStartElement().getName().getLocalPart();
            } else if (event.isEndElement()) {
                elementName = event.asEndElement().getName().getLocalPart();
            }

            // Check for header, and skip events inside it.
            if ("lzaplhdr-area".equals(elementName)) {
                if (event.isStartElement()) {
                    writeEvent = false;
                } else if (event.isEndElement()) {
                    writeEvent = true;
                }
                continue;
            }

            if (writeEvent && !"replyData".equals(elementName)) {
                writer.add(event);
            }
        }

        writer.close();

        byte[] soapResponse = bos.toByteArray();
        return soapResponse;
	}

	private byte[] generateSoapFaultReply(Throwable e) {

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			XMLStreamWriter writer = outputFactory.createXMLStreamWriter(bos);
			writer.writeStartDocument(SOAP_ENCODING, "1.0");

			// START - <soapEnv:Envelope>
			writer.writeStartElement("soapenv", "Envelope", SOAP_NAMESPACE);

			// START - <soapEnv:Body>
			writer.writeStartElement("soapenv", "Body", SOAP_NAMESPACE);

			// START - <soapEnv:Fault>
			writer.writeStartElement("soapenv", "Fault", SOAP_NAMESPACE);

			writer.writeStartElement("", "faultcode", ""); // START : <faultcode>
			writer.writeCharacters(getFaultCode(e));
			writer.writeEndElement(); // END : </faultcode>

			writer.writeStartElement("", "faultstring", ""); // START : <faultstring>
			writer.writeCharacters(xml_escape(e.getMessage()));
			writer.writeEndElement(); // END : </faultstring>

			writer.writeStartElement("", "detail", ""); // END : <detail>

			writer.writeStartElement("", "message", ""); // START: <message>
			writer.writeCharacters(getFaultDetail(e));
			writer.writeEndElement(); // END : </message>

			writer.writeStartElement("", "errorcode", ""); // START: <errorcode>

            writer.writeEndElement(); // END : </errorcode>
			writer.writeEndElement(); // END : </detail>
			writer.writeEndElement(); // END - <soapEnv:Fault>
			writer.writeEndElement(); // END - <soapEnv:Body>
			writer.writeEndElement(); // END - <soapEnv:Envelope>

			writer.close();

			byte[] soapReply = bos.toByteArray();
			return soapReply;
		} catch (Exception ex) {
			logger.error(ex);
			return null;
		}
	}

	private String xml_escape(String text) {
		if (text == null || text.equals("")) {
			return "";
		}

		text = text.replaceAll("<", "&lt;");
		text = text.replaceAll(">", "&gt;");
		return text;
	}

	private String getFaultCode(Throwable e) {
		return "MULE_CICS_FAULT_CODE";
	}

	private String getFaultDetail(Throwable e) {

        if (e == null) return "";
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		sw.flush();
		return sw.toString();
	}
}
