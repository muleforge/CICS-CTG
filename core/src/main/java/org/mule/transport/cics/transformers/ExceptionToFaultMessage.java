package org.mule.transport.cics.transformers;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.mule.transport.cics.util.Constants;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.MuleMessage;
import org.mule.api.ExceptionPayload;
import org.mule.api.transformer.TransformerException;

/**
 * This class detects if an exception has occured during message processing
 * and converts the exception to a fault message containing stack trace.
 * The fault message can be send back as the response to the client.
 *  
 * This transformer should be applied as the response transformer
 * after all message processing is complete.
 */
public class ExceptionToFaultMessage extends AbstractMessageAwareTransformer {

    private static final String FAULT_NAMESPACE = "http://ogis-ri.co.jp/mule-cics-fault";

    /**
     * Converts into fault message if an exception has occured.
     * 
     * @param message Mule message.
     * @return fault message (if exception has occured), original message (otherwise)
     * @throws TransformerException
     */
    public Object transform(MuleMessage message, String encoding) throws TransformerException {
      ExceptionPayload e = message.getExceptionPayload();
      if (e == null) return message; 
        
      byte[] faultBytes = transform(e.getRootException());
      try {
          String faultMsg = new String(faultBytes, Constants.XML_DEFAULT_ENCODING);
          logger.error(faultMsg);
          return faultMsg;
      } catch (Exception ex) { /* ignore */ }

      return faultBytes;
    }

    protected byte[] transform(Throwable e) {

      XMLStreamWriter writer = null;

      try {
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          XMLOutputFactory output = XMLOutputFactory.newInstance();
          output.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
          writer = output.createXMLStreamWriter(bos, Constants.XML_DEFAULT_ENCODING);

          writer.writeStartElement("fault-data");
          writer.writeAttribute("xmlns", FAULT_NAMESPACE );
          writer.writeStartElement("fault-class" );
          writer.writeCharacters(getFaultClass(e));
          writer.writeEndElement();
          writer.writeStartElement("fault-code");
          writer.writeCharacters(getFaultCode(e));
          writer.writeEndElement();
          writer.writeStartElement("fault-message" );
          String faultMsg = xml_escape(e.getMessage());
          writer.writeCharacters(faultMsg);
          writer.writeEndElement();
          writer.writeStartElement("fault-detail");
          String detail = xml_escape(getFaultDetail(e));
          writer.writeCharacters(detail);
          writer.writeEndElement();
          writer.writeEndElement();
          writer.flush();

          byte[] faultBytes = bos.toByteArray();
          
          return faultBytes;
      } catch (Exception ex) {
          ex.printStackTrace();
      } finally {
          try {
              writer.close();
          } catch (Exception ex) {
              ex.printStackTrace();
          }
      }

      return null;
    }

    private String xml_escape(String text) {
      if (text == null || text.equals("")) {
          return "";
      }

      text = text.replaceAll("<", "&lt;");
      text = text.replaceAll(">", "&gt;");
      return text;
    }

    private String getFaultClass(Throwable e) {
      return "MULE_CICS_FAULT_CLASS";
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
