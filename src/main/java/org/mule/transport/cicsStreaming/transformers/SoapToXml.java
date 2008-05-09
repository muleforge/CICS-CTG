package org.mule.transport.cicsStreaming.transformers;

import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.util.StreamReaderDelegate;

import org.mule.api.MuleMessage;
import org.mule.api.MuleEvent;
import org.mule.message.DefaultExceptionPayload;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.transformer.TransformerException;

import org.mule.transport.cics.esbInterface.Interface2WSDL;
import org.mule.transport.cics.util.XmlUtils;
import org.mule.transport.cics.util.Constants;

/**
 * This transformer extracts the XML in a Soap message. i.e it returns the
 * message inside <soap:Body> tag.
 */
public class SoapToXml extends AbstractMessageAwareTransformer {

    private static final String SOAP_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
    private static final String SOAP_BODY_TAG  = "Body";

    /** StAX Factory for creating reader to read XML */
    private static XMLInputFactory inputFactory = XMLInputFactory.newInstance();

    public Object transform(MuleMessage message, String encoding) throws TransformerException {
                  
      // Skip processing if an exception has occurred earlier.
      if (message.getExceptionPayload() != null)
          return message;

      try {

        // When the HTTP URI ends with the string "?wsdl", return WSDL file.
        String url = (String) message.getProperty("http.request");
        if (url != null && url.matches("(?i).*\\?wsdl$")) {
            /* Above regex matches url ending with '?wsdl' in case-insensitive manner.
             * (?i) - flag for case-insensitive matching. 
             * .*   - any sequence of characters, 
             * \\?  - ? escaped to prevent special meaning.
             */

            message.setBooleanProperty(Constants.SKIP_FURTHER_PROCESSING, true);
            String interfaceFile = message.getStringProperty("interfaceFile", "");
            String serviceLocation = getEndpoint().getEndpointURI().getAddress();
            return new Interface2WSDL().getWSDL(interfaceFile, serviceLocation);
        }

       // Get the input stream to the received SOAP message.
       InputStream soapIs = (InputStream) message.getPayload(InputStream.class);
       final XMLStreamReader reader = inputFactory.createXMLStreamReader(soapIs);

       // Skip the tags until, we reach the SOAP body.
       while (reader.hasNext()) {
           int eventType = reader.nextTag();
           if (isSoapBodyStartElement(reader)) {
               
                reader.nextTag(); 
                if (reader.isStartElement()) {
                    String operationName = reader.getLocalName();
                    message.setStringProperty("operationName", operationName);
                    break;
                }
           }
       }

       // Setup a delegate reader that allows next transformer to read only the XML inside SOAP body.
       StreamReaderDelegate delegate = new StreamReaderDelegate(reader) {
           
          public int next() throws XMLStreamException {
              int event = reader.next();
              if (isSoapBodyEndElement(reader)) {
                  while (reader.hasNext())
                    event = reader.next();
              }

              return event;
          }

          public int nextTag() throws XMLStreamException {
              int event = reader.nextTag();
              if (isSoapBodyEndElement(reader)) {
                  while (reader.hasNext())
                    event = reader.nextTag();
              }

              return event;
          }
       };

       // Return the delegate, that is used by next transformer to read XML in Soap body (in streaming way).
       return delegate;

     } catch (Exception e) {
        logger.error(e);
        message.setExceptionPayload(new DefaultExceptionPayload(e));
        return message;
     }
  }

  private static boolean isSoapBodyStartElement(XMLStreamReader reader) {

    if (reader.isStartElement()) {
        return  SOAP_BODY_TAG.equalsIgnoreCase(reader.getLocalName())
                && SOAP_NAMESPACE.equalsIgnoreCase(reader.getNamespaceURI());
    }

    return false;
  }

  private static boolean isSoapBodyEndElement(XMLStreamReader reader) {

    if (reader.isEndElement()) {
        return  SOAP_BODY_TAG.equalsIgnoreCase(reader.getLocalName())
                && SOAP_NAMESPACE.equalsIgnoreCase(reader.getNamespaceURI());
    }

    return false;
  }
}
