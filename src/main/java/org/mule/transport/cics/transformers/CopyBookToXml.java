package org.mule.transport.cics.transformers;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.mule.api.MuleMessage;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.transport.cics.esbInterface.Operation;
import org.mule.transport.cics.util.Constants;
import org.mule.transport.cics.util.XsdReader;
import org.mule.transport.cics.util.XsdReaderFactory;

/** 
 * Transformer to transform mainframe binary data to XML.
 */
public class CopyBookToXml extends AbstractMessageAwareTransformer {

	private String encoding = Constants.CICS_DEFAULT_ENCODING;
	
    /** StAX Factory for creating writer to write XML */
    private static XMLOutputFactory outputFactory = null;

    static {
        outputFactory = XMLOutputFactory.newInstance();
        outputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
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
          throw new RuntimeException("Error in Mule config file. Invalid value for encoding of transformer 'CopyBookToXml'. The encoding '"+ encoding+"' is not supported.");		
      }
      this.encoding = encoding;
    }
    
	public Object transform(MuleMessage message, String encoding) throws TransformerException {

        if (message.getExceptionPayload() != null) return message;

        
        try {            
            boolean skipProcessing = message.getBooleanProperty(Constants.SKIP_RESPONSE_TRANSFORMER, false);
            if (skipProcessing) return message;

            Operation operation = (Operation) message.getProperty("operation", null);
            String outboundXsd = operation.getOutboundXsd();
            
            String outboundNamespace = message.getStringProperty("OUTBOUND_NAMESPACE", "");
            if( "".equals(outboundNamespace) ){
            	XsdReader reader = XsdReaderFactory.getInstance().getXsdReader(outboundXsd);
            	outboundNamespace = reader.getNamespaceOfDocument();
            }
            
            logger.debug("outboundXsd = " + outboundXsd);
            if (outboundXsd == null || outboundXsd.equals("")) {
                return message; // No transformation is required.
            }

            // Create the Stax writer to write XML.
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(bos, Constants.XML_DEFAULT_ENCODING);
            writer.writeStartDocument();

            // Transform the copy book bytes into XML.
            byte[] copyBookBytes = message.getPayloadAsBytes();
            InputStream copybookStream = new BufferedInputStream
                                  (new ByteArrayInputStream(copyBookBytes));
            
            new CopyBookToXmlUtil().transform(copybookStream, getEncoding(), outboundXsd, outboundNamespace, writer);

            writer.writeEndDocument();
            writer.close();
            copybookStream.close();
            return new String(bos.toByteArray(), Constants.XML_DEFAULT_ENCODING);
        } catch (Throwable e) {
            throw new TransformerException(this, e);
        }
    }
}
