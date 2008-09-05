package org.mule.transport.cicsStreaming.transformers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.OutputHandler;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.transport.cics.util.Constants;

/**
 * This transformer converts the XML message into a SOAP message i.e it adds the
 * XML tags for <soap:Envelope> and <soap:Body>
 */
public class XmlToSoap extends AbstractMessageAwareTransformer {

    private static final String SOAP_ENCODING = "UTF-8";
    private static final String SOAP_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";


    private final static String SOAP_START =
      "<?xml version='1.0' encoding='UTF-8'?><soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'><soapenv:Body>";
      

    private final static String SOAP_END =
        "</soapEnv:Body></soapenv:Envelope>";

    private static byte[] SOAP_START_BYTES = null;
    private static byte[] SOAP_END_BYTES = null;
    private static XMLOutputFactory outputFactory;
   
    static {
        outputFactory = XMLOutputFactory.newInstance();
        outputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);

        try {
          SOAP_START_BYTES = SOAP_START.getBytes(SOAP_ENCODING);   
          SOAP_END_BYTES   = SOAP_END.getBytes(SOAP_ENCODING);   
        } catch (UnsupportedEncodingException e) {
          SOAP_START_BYTES = SOAP_START.getBytes();   
          SOAP_END_BYTES   = SOAP_END.getBytes();   
        }
    }


    public Object transform(final MuleMessage message, String encoding) throws TransformerException {

        // Do not do any processing if returning back WSDL to client.
        if (message.getBooleanProperty(Constants.SKIP_FURTHER_PROCESSING, false)) {
            return message;
        }

        final OutputHandler previousOutputHandler
               = (OutputHandler) message.getPayload(OutputHandler.class);

        return new OutputHandler() {

          public void write(MuleEvent event, OutputStream out) throws IOException
          {
            try {
                if (message.getExceptionPayload() != null) {
                    writeFaultMessage(out, message.getPayloadAsBytes());
                } else {
                    writeSoapResponse(event, previousOutputHandler, out);
                }
            } catch (Exception e) {
                byte[] faultReply = generateSoapFaultMessage(e);
                e.printStackTrace();
			    logger.error(new String(faultReply));
                out.write(faultReply);
            }
          }
        };
    }
 
    protected void writeSoapResponse(MuleEvent event, OutputHandler previousOutputHandler, OutputStream out) throws Exception {

        out.write(SOAP_START_BYTES);

        // The OutputHandler in previous transformer writes XML in soap body.
        previousOutputHandler.write(event, out);

        out.write(SOAP_END_BYTES);
        out.flush();
    }

    protected void writeFaultMessage(OutputStream out, byte[] fault) 
              throws Exception {

        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(out);
		writer.writeStartDocument(SOAP_ENCODING, "1.0");
        // START - <soapEnv:Envelope>
        writer.writeStartElement("soapenv", "Envelope", SOAP_NAMESPACE);
        // START - <soapEnv:Body>
        writer.writeStartElement("soapenv", "Body", SOAP_NAMESPACE);
        // START - <soapEnv:Fault>
        writer.writeStartElement("soapenv", "Fault", SOAP_NAMESPACE);
        // START - <detail>
        writer.writeStartElement("", "detail", "");

        out.write(fault);

        writer.writeEndElement(); // END - </detail>
        writer.writeEndElement(); // END - </soapEnv:Fault>

		writer.writeEndElement(); // END - <soapEnv:Body>
		writer.writeEndElement(); // END - <soapEnv:Envelope>
        writer.writeEndDocument();// END_DOCUMENT
        writer.close();
    }

    private byte[] generateSoapFaultMessage(Throwable e) {

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

	        writer.writeStartElement("", "detail", ""); // START - <detail>

            writer.writeStartElement("", "message", ""); // START: <message>
            writer.writeCharacters(xml_escape(getFaultDetail(e)));
            writer.writeEndElement(); // END : </message>

            writer.writeStartElement("", "errorcode", ""); // START: <errorcode>
            writer.writeEndElement(); // END : </errorcode>

            writer.writeEndElement(); // END - </detail>
            writer.writeEndElement(); // END - </soapEnv:Fault>
            writer.writeEndElement(); // END - </soapEnv:Body>
            writer.writeEndElement(); // END - </soapEnv:Envelope>
            writer.writeEndDocument();// END_DOCUMENT

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
