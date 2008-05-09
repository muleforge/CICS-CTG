package org.mule.transport.cicsStreaming.transformers;

import java.io.ByteArrayInputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.util.StreamReaderDelegate;

import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.api.MuleMessage;
import org.mule.message.DefaultExceptionPayload;

import org.mule.transport.cics.util.XmlUtils;

/**
 * This class implements the inbound transformers for messages received by  
 * mule-cics. This removes the outer tags and returns XML inside <app-data>.
 */
public class XmlInboundTransformer extends AbstractMessageAwareTransformer {

  private static final String APP_DATA_TAG = "<app-data>";

  /** StAX Factory for creating reader to read XML */
  private static XMLInputFactory inputFactory = XMLInputFactory.newInstance();

  /*
   * This method is called when the mule-cics receives a message
   * through an inbound endpoint configured in mule-config.xml
   */
  public Object transform(MuleMessage message, String encoding) throws TransformerException {

    try {
      byte[] incoming = message.getPayloadAsBytes();
       
      ByteArrayInputStream is = new ByteArrayInputStream(incoming); 
      final XMLStreamReader reader = inputFactory.createXMLStreamReader(is);
         
      // Skip the tags until, we reach <app-data>.
      boolean first = true;
      while (reader.hasNext()) {
          reader.next();
          if (first == true && reader.isStartElement()) {
              String operationName = reader.getLocalName();
              message.setStringProperty("operationName", operationName);
              first = false;
              continue;
          }
 
          if (isAppDataStartElement(reader)) {
              reader.next(); 
              break;
          }
      }

      // Setup a delegate reader that allows next transformer to read only the XML inside <app-data>.
       StreamReaderDelegate delegate = new StreamReaderDelegate(reader) {
           
          public int next() throws XMLStreamException {
              int event = reader.next();
              if (isAppDataEndElement(reader)) {
                  while (reader.hasNext())
                    event = reader.next();
              }

              return event;
          }

          public int nextTag() throws XMLStreamException {
              int event = reader.nextTag();
              if (isAppDataEndElement(reader)) {
                  while (reader.hasNext())
                    event = reader.nextTag();
              }

              return event;
          }
       };

       // Return the delegate, that is used by next transformer to read XML in Soap body (in streaming way).
       return delegate;
    } catch (Throwable e) {
      e.printStackTrace();
 	  logger.error(e);
 	  message.setExceptionPayload(new DefaultExceptionPayload(e));
 	  return message;
    }
  }

  private static boolean isAppDataStartElement(XMLStreamReader reader) {

    return (reader.isStartElement() &&
            APP_DATA_TAG.equalsIgnoreCase(reader.getLocalName()));
  }

  private static boolean isAppDataEndElement(XMLStreamReader reader) {

      return (reader.isEndElement() &&
            APP_DATA_TAG.equalsIgnoreCase(reader.getLocalName()));
  }
}
