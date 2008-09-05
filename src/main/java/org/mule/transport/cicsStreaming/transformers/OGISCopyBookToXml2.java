package org.mule.transport.cicsStreaming.transformers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.OutputHandler;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.transport.cics.esbInterface.Operation;
import org.mule.transport.cics.i18n.CicsMessages;
import org.mule.transport.cics.util.Constants;

/**
 * Transformer to convert mainframe response to XML.
 * Response XML also contains the header data.
 */
public class OGISCopyBookToXml2 extends AbstractMessageAwareTransformer {

    public static int HEADER_LENGTH     = 328;
    public static int DCI_HEADER_LENGTH = 128;

    private String encoding = Constants.CICS_DEFAULT_ENCODING;

	/** StAX Factory for creating writer to write XML */
    private static XMLOutputFactory outputFactory = null;

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
          throw new RuntimeException(CicsMessages.invalidEncodingForTransformer(getClass().getName(),encoding).toString());		
      }
      this.encoding = encoding;
    }

    public Object transform(MuleMessage message, String encoding) throws TransformerException {

        if (message.getExceptionPayload() != null) return message;

        boolean skipProcessing = message.getBooleanProperty(Constants.SKIP_RESPONSE_TRANSFORMER, false);     
        if (skipProcessing) return message;

        Operation operation = (Operation) message.getProperty("operation", null);

        final String outboundXsd = operation.getOutboundXsd();
        logger.info("outboundXsd = " + outboundXsd);
        if (outboundXsd == null || outboundXsd.equals("")) {
            return message; // No transformation is required.
        }

        InputStream is = (InputStream) message.getPayload();
        final InputStream copybookStream = new BufferedInputStream(is);

        return new OutputHandler() {

          public void write(MuleEvent event, OutputStream out) throws IOException
          {
            try {
                // Create the Stax writer to write XML.
                XMLStreamWriter writer = outputFactory.createXMLStreamWriter(out, Constants.XML_DEFAULT_ENCODING);
                writer.writeStartDocument();
                writer.writeStartElement("", "replyData"); // START - <replyData>
                    
                // Write headerXML as per header bytes.
                String headerXsd = "META-INF/header.xsd";
                for (int i=0; i<DCI_HEADER_LENGTH; i++) {
                    int b = copybookStream.read();  // skip the DCI HEADER
                }

                new CopyBookToXmlUtil().transform(copybookStream, getEncoding(), headerXsd, writer);

                // Write the main XML body
                new CopyBookToXmlUtil().transform(copybookStream, getEncoding(), outboundXsd, writer);
                writer.writeEndElement(); // END - </replyData>
                writer.writeEndDocument();
                writer.close();
                copybookStream.close();
            } catch (Exception e) {
                
                logger.error(e);
                e.printStackTrace(); 
                // @TODO: Detect if exception is due to insufficient repsonse.   
                // String errMsg = "Insufficient length of mainframe response. " +
                if (e instanceof IOException) 
                  throw (IOException) e;
                else
                  throw new IOException(e.getMessage());
            }
         }
       };
    }
}
