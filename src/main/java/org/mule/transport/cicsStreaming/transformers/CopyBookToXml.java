package org.mule.transport.cicsStreaming.transformers;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.mule.api.MuleMessage;
import org.mule.api.MuleEvent;
import org.mule.api.transport.OutputHandler;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;

import org.mule.transport.cics.esbInterface.Operation;
import org.mule.transport.cics.util.Constants;

/** 
 * Transformer to transform mainframe binary data to XML.
 */
public class CopyBookToXml extends AbstractMessageAwareTransformer {

    private String encoding = Constants.CICS_DEFAULT_ENCODING;

    /** StAX Factory for creating XML writer. */
    private static XMLOutputFactory outputFactory = null;

    static {
        outputFactory = XMLOutputFactory.newInstance();
        outputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
    }

    /** Gets the encoding used to read the mainframe response */
    public String getEncoding() {
	  return encoding;
    }

    /** Sets the encoding used to read the mainframe response */
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

        boolean skipProcessing = message.getBooleanProperty(Constants.SKIP_RESPONSE_TRANSFORMER, false);
        if (skipProcessing) return message;

        try {
            Operation operation = (Operation) message.getProperty("operation", null);
            final String outboundXsd = operation.getOutboundXsd();

            logger.debug("outboundXsd = " + outboundXsd);
            if (outboundXsd == null || outboundXsd.equals("")) {
                return message; // No transformation is required.
            }

            InputStream is = (InputStream) message.getPayload();
            final InputStream copybookStream = new BufferedInputStream(is);

            return new OutputHandler() {

                public void write(MuleEvent event, OutputStream out) throws IOException
                {
                    try {
                        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(out, Constants.XML_DEFAULT_ENCODING);
                        new CopyBookToXmlUtil().transform(copybookStream, getEncoding(), outboundXsd, writer);
                        writer.close();
                        copybookStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e instanceof IOException) 
                            throw (IOException) e;
                        else
                            throw new IOException(e.getMessage());
                    }
                }
            };

        } catch (Throwable e) {
            throw new TransformerException(this, e);
        }
    }
}
