package org.mule.transport.cicsStreaming.transformers;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.mule.api.MuleMessage;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.message.DefaultExceptionPayload;

import org.mule.transport.cics.esbInterface.Interface2WADL;
import org.mule.transport.cics.esbInterface.EsbInterfaceDigester;
import org.mule.transport.cics.esbInterface.EsbInterface;
import org.mule.transport.cics.esbInterface.Operation;
import org.mule.transport.cics.util.Constants;
import org.mule.transport.cics.util.XsdElement;
import org.mule.transport.cics.util.XsdReader;
import org.mule.transport.cics.util.XsdReaderFactory;

/**
 * The class converts a URL into an XML, where the value of each XML
 * element is set from the correspoding parameter passed in URL.
 *
 * Example of the URL:
 *
 * http://xxx.com/CustomerInterface/sample-command?customer-name=abc&customer-address=xyz&search-date=20060701&customer-name=joe
 *
 * XML generated for above URL:

   <sample-command xmlns="http://ogis-ri.co.jp/SAMPLE-COMMAND">
     <customer-info>
        <customer-no></customer-no>
        <customer-name>joe</customer-name>
        <customer-address>xyz</customer-address>
        <search-date>20060701</search-date>
    </customer-info>
   </sample-command>
 *
 * URL parameter name may be fully-qualified.
 * For e.g. http://xxx.com/CustomerInterface/sample-command?sample-command.customer.info.customer-name=abc
 *
 * Array data may also be sent from URL as below:
 * http://xyz.com/operation?param[0]=aa&param[1]=bb&param[2]cc
 */
public class RestToXml extends AbstractMessageAwareTransformer {

    public Object transform(MuleMessage message, String encoding) throws TransformerException {
        try {
            // Skip processing if an exception has occurred earlier.
            if (message.getExceptionPayload() != null)
                return message;
            
            
            String url = (String)message.getProperty("http.request");
            // If Method is POST then return the XML as is
            String httpMethod = (String) message.getProperty("http.method");
            if( httpMethod.equalsIgnoreCase("POST") ){
            	// Get the operation name from request.
                String operationName = getOperation(url);
                message.setStringProperty("operationName", operationName);
            	return message;
            }

            String interfaceFile = message.getStringProperty("interfaceFile", "");

            // Decode the URL as the WADL client sends encoded URL
            URLDecoder urlDecoder = new URLDecoder();
            url = urlDecoder.decode(url, encoding);

            // When the HTTP URI contains the string "?wadl", return WADL file.
            if (url.endsWith("?WADL") ||
                url.endsWith("?wadl") || url.endsWith("?Wadl")) {
                message.setBooleanProperty(Constants.SKIP_FURTHER_PROCESSING, true);
                String serviceLocation = getEndpoint().getEndpointURI().getAddress();
                return new Interface2WADL().getWADL(interfaceFile, serviceLocation);
            } else if (url.endsWith("?xsd=outbound")) {
                message.setBooleanProperty(Constants.SKIP_FURTHER_PROCESSING, true);
                String operationName = getOperation(url);
                return new Interface2WADL().getXSD(interfaceFile, operationName, Interface2WADL.XSD_TYPE_OUTBOUND);
            } else if (url.endsWith("?xsd=inbound")) {
                message.setBooleanProperty(Constants.SKIP_FURTHER_PROCESSING, true);
                String operationName = getOperation(url);
                return new Interface2WADL().getXSD(interfaceFile, operationName, Interface2WADL.XSD_TYPE_INBOUND);
            } else if (url.endsWith("?xsd=error")) {
                message.setBooleanProperty(Constants.SKIP_FURTHER_PROCESSING, true);
                String operationName = getOperation(url);
                return new Interface2WADL().getXSD(interfaceFile, operationName, Interface2WADL.XSD_TYPE_ERROR);
            }

            Map requestParams = parseUrlParameters(url);
            if (logger.isDebugEnabled()) {
                logger.debug("Parameter Map from URL->"+requestParams);
            }

            // Get the operation name from URL, and get the operation's inbound XSD.
            String operationName = getOperation(url);
            message.setStringProperty("operationName", operationName);
            EsbInterface esbInterface
                = EsbInterfaceDigester.getInstance().parse(interfaceFile);
            Operation operation = esbInterface.getOperation(operationName);
            String inboundXsd = operation.getInboundXsd();

            // Convert the request parameters to an XML that conforms to the inbound XSD.
            MapToXmlUtil mapToXmlUtil = new MapToXmlUtil();
            byte[] xml = mapToXmlUtil.transform(inboundXsd, requestParams);
            return xml;
        } catch (Throwable e) {
            logger.error(e);
            message.setExceptionPayload(new DefaultExceptionPayload(e));
            return message;
        }
    }

    private String getOperation(String url) {
        int slashIndex = url.lastIndexOf('/');
        int startIndex = (slashIndex >= 0) ? slashIndex + 1 : 0;
        int quesIndex = url.indexOf('?');
        
        if (quesIndex < 0 ){
        	return url.substring(startIndex);
        } else {
        	if (startIndex < quesIndex) {
        		return url.substring(startIndex, quesIndex);
        	}
        }
        return "";
    }

    private Map parseUrlParameters(String url) {
        HashMap requestParams = new HashMap();

        int questionIndex = url.indexOf('?');
        if (questionIndex >= 0 && url.length() >= questionIndex + 1) {
            url = url.substring(questionIndex + 1);

            StringTokenizer tokens = new StringTokenizer(url, "&");
            while (tokens.hasMoreElements()) {
                String token = (String) tokens.nextToken();
                int equalIndex = token.indexOf('=');
                if (equalIndex >= 0) {
                    String paramName = token.substring(0, equalIndex);
                    String paramValue = "";
                    if (token.length() > equalIndex + 1) {
                        paramValue = token.substring(equalIndex + 1);
                    }
                    requestParams.put(paramName, paramValue);
                }
            }
        }
        return requestParams;
    }

    // Inner class to convert Map to inbound XML
    class MapToXmlUtil {

        private Map requestParams = null;

        public byte[] transform(String xsdFile, Map requestParams) throws Exception {
            if (xsdFile == null || xsdFile.equals("")) {
                return null;
            }

            XsdReader xsdReader = XsdReaderFactory.getInstance().getXsdReader(xsdFile);
            if (xsdReader == null) return null;

            byte[] xml = transform(xsdReader, requestParams);
            return xml;
        }

        private byte[] transform(XsdReader xsdReader, Map requestParams) throws Exception {

            this.requestParams = requestParams;
            XMLOutputFactory output = XMLOutputFactory.newInstance();
            output.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            XMLStreamWriter writer = output.createXMLStreamWriter(bos, Constants.XML_DEFAULT_ENCODING);

            XsdElement rootXsdElement = xsdReader.getRootXsdElement();
            String namespace = xsdReader.getNamespaceOfDocument();

            startRootElement(writer, rootXsdElement, namespace);

            String qualifiedElementName = rootXsdElement.getName();
            writeXmlElementChildren(writer, rootXsdElement, qualifiedElementName);

            endRootElement(writer, rootXsdElement);

            writer.flush();
            writer.close();
            return bos.toByteArray();
        }

        private void writeXmlElementChildren(XMLStreamWriter writer, XsdElement xsdElement, String qualifiedElementName) throws Exception {

            Iterator childIter = xsdElement.getChildIterator();
            while (childIter.hasNext()) {
                XsdElement child = (XsdElement) childIter.next();

                long occurs = child.getOccurs();
                if (occurs > 1) {
                    for (int i=0; i<occurs; i++) {
                        writer.writeStartElement(child.getName());
                        String childName = child.getName() + "[" + i + "]";
                        String qualifiedChildName = qualifiedElementName + "." + childName;
                        writeElementValue(writer, qualifiedChildName, childName);
                        writeXmlElementChildren(writer, child, qualifiedChildName);
                        writer.writeEndElement();
                    }
                } else {
                    String qualifiedChildName = qualifiedElementName + "." + child.getName();
                    writer.writeStartElement(child.getName());
                    writeElementValue(writer, qualifiedChildName, child.getName());
                    writeXmlElementChildren(writer, child, qualifiedChildName);
                    writer.writeEndElement();
                }
            }
        }

        private void writeElementValue(XMLStreamWriter writer, String qualifiedElementName,
                                    String elementName) throws Exception {

            String elementValue = (String) this.requestParams.get(qualifiedElementName);
            if (elementValue == null) {
                elementValue = (String) this.requestParams.get(elementName);
            }

            if (elementValue == null) elementValue = "";
            writer.writeCharacters(elementValue);
        }

        private void startRootElement(XMLStreamWriter writer, XsdElement rootElement, String namespace) throws Exception {
            writer.setPrefix("", namespace);
            writer.writeStartElement(namespace, rootElement.getName());
        }

        private void endRootElement(XMLStreamWriter writer, XsdElement rootElement) throws Exception {
            writer.writeEndElement();
        }
    }
}
