package org.mule.transport.cics.transformers;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.api.MuleMessage;
import org.mule.transport.cics.util.Constants;

/*
 * Used to convert a byte array to XML with UTF-8 encoding.
 */
public class ByteArrayToXml extends AbstractMessageAwareTransformer {

  public Object transform(MuleMessage message, String encoding) throws TransformerException {

      try {
          if (message.getExceptionPayload() != null) 
              return message;
          boolean skipProcessing = message.getBooleanProperty(Constants.SKIP_RESPONSE_TRANSFORMER, false);
          if (skipProcessing) return message;

          byte[] bytes = message.getPayloadAsBytes();
          ByteArrayInputStream is = new ByteArrayInputStream(bytes);

          // Write the XML into UTF-8 encoding.
          Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
          Transformer transformer = TransformerFactory.newInstance().newTransformer();
          transformer.setOutputProperty("encoding", Constants.XML_DEFAULT_ENCODING);

          StringWriter stringWriter = new StringWriter();
          StreamResult streamResult = new StreamResult(stringWriter);
          DOMSource domSource = new DOMSource(xmlDocument);
          transformer.transform(domSource, streamResult);

          String response = stringWriter.toString();
          return response;

      } catch (Throwable e) {
          throw new TransformerException(this, e);
      }
  }
}
