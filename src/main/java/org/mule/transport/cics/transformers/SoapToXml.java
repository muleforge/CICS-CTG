package org.mule.transport.cics.transformers;

import java.io.InputStream;

import org.mule.api.MuleMessage;
import org.mule.message.DefaultExceptionPayload;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.transport.cics.esbInterface.EsbInterface;
import org.mule.transport.cics.esbInterface.Interface2WSDL;
import org.mule.transport.cics.util.XmlUtils;
import org.mule.transport.cics.util.Constants;

/**
 * This transformer extracts the XML in a Soap message. i.e it returns the
 * message inside <soap:Body> tag.
 */
public class SoapToXml extends AbstractMessageAwareTransformer {

    private static final String NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
    private static final String BODY_TAG = "Body";

    public Object transform(MuleMessage message, String encoding) throws TransformerException {
      logger.info("In SoapToXml.transform()"); 
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
        	logger.info("In SoapToXml.transform() url->"+url);
            message.setBooleanProperty(Constants.SKIP_FURTHER_PROCESSING, true);
            String interfaceFile = message.getStringProperty("interfaceFile", "");
            String serviceLocation = getEndpoint().getEndpointURI().getAddress();
            return new Interface2WSDL().getWSDL(interfaceFile, serviceLocation);
        }

       String interfaceFile = message.getStringProperty("interfaceFile", "");
       String soapOutboundNamespace = EsbInterface.getNamespaceOfWsdl(interfaceFile);
       message.setStringProperty("OUTBOUND_NAMESPACE", soapOutboundNamespace);
       InputStream soapIs = (InputStream) message.getPayload(InputStream.class);
       byte[] bodyContents = XmlUtils.getInternalXML(soapIs, BODY_TAG, NAMESPACE);

       String operationName = XmlUtils.getRootElementName(bodyContents);
       message.setStringProperty("operationName", operationName);

       return bodyContents;

     } catch (Exception e) {
        logger.error(e);
        message.setExceptionPayload(new DefaultExceptionPayload(e));
        return message;
    }
  }
}
