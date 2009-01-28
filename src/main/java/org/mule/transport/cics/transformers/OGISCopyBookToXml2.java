package org.mule.transport.cics.transformers;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamWriter;

import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

import org.mule.transport.cics.util.Constants;
import org.mule.transport.cics.esbInterface.Operation;
import org.mule.transport.cics.i18n.CicsMessages;

/**
 * Transformer to convert mainframe response to XML.
 * Response XML also contains the header data.
 */
public class OGISCopyBookToXml2 extends AbstractMessageAwareTransformer {

    public static int HEADER_LENGTH = 328;
    public static int DCI_HEADER_LENGTH = 128;

	/** StAX Factory for creating writer to write XML */
    private static XMLOutputFactory outputFactory = null;
    
    private String encoding = Constants.CICS_DEFAULT_ENCODING;

	/** StAX Factory for creating XML events used to write XML */
	private static XMLEventFactory eventFactory;
	
    static {
        outputFactory = XMLOutputFactory.newInstance();
        outputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
		eventFactory = XMLEventFactory.newInstance();
    }
    
    /** Returns the encoding used to read mainframe response */
    public String getEncoding() {
      return encoding;
    }

    /** Sets the encoding used to read mainframe response */
    public void setEncoding(String encoding) {
      try {
          " ".getBytes(encoding);
      } catch(UnsupportedEncodingException e) {
          CicsMessages messages = new CicsMessages();
          throw new RuntimeException(messages.invalidEncodingForTransformer(getClass().getName(),encoding).toString());		
      }
      this.encoding = encoding;
    }

	public Object transform(MuleMessage message, String encoding) throws TransformerException {
        if (message.getExceptionPayload() != null)
            return message;

        boolean skipProcessing = message.getBooleanProperty(Constants.SKIP_RESPONSE_TRANSFORMER, false);     
        if (skipProcessing) return message;

        try {
            byte[] copyBookBytes = message.getPayloadAsBytes();
            if (copyBookBytes.length < HEADER_LENGTH) {
                CicsMessages messages = new CicsMessages();
                throw new Exception(messages.insufficientResponseLength().toString());
            }

            byte[] bytesHeader = new byte[HEADER_LENGTH - DCI_HEADER_LENGTH];
            byte[] bytesWithoutHeader = new byte[copyBookBytes.length - HEADER_LENGTH];

            System.arraycopy(copyBookBytes, DCI_HEADER_LENGTH, 
                       bytesHeader, 0, HEADER_LENGTH - DCI_HEADER_LENGTH);
            System.arraycopy(copyBookBytes, HEADER_LENGTH, 
                       bytesWithoutHeader, 0, bytesWithoutHeader.length);

            Operation operation = (Operation) message.getProperty("operation", null);

            String outboundXsd = operation.getOutboundXsd();
            logger.info("outboundXsd = " + outboundXsd);
            if (outboundXsd == null || outboundXsd.equals("")) {
                return message; // No transformation is required.
            }

            // Create the Stax writer to write XML.
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(bos, Constants.XML_DEFAULT_ENCODING);
            writer.writeStartDocument();
            writer.writeStartElement("", "replyData"); // START - <replyData>

            // Write headerXML as per header bytes. 
            String headerXsd = "META-INF/header.xsd";
            InputStream headerStream = new BufferedInputStream(
                                   new ByteArrayInputStream(bytesHeader));
            new CopyBookToXmlUtil().transform(headerStream, getEncoding(), headerXsd, writer);

            // Transform the copybook bytes into XML.
            InputStream copybookStream = new BufferedInputStream(
                                  new ByteArrayInputStream(bytesWithoutHeader));
            new CopyBookToXmlUtil().transform(copybookStream, getEncoding(), outboundXsd, writer);

            writer.writeEndElement(); // END - </replyData>
			writer.writeEndDocument();

            writer.close();
            String xml = new String(bos.toByteArray(), Constants.XML_DEFAULT_ENCODING);
            return xml;
        } catch (Throwable e) {
            throw new TransformerException(this, e);
        }
    }
}
