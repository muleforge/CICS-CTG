package org.mule.transport.cicsStreaming.transformers;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.OutputHandler;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.transport.cics.esbInterface.Operation;
import org.mule.transport.cics.i18n.CicsMessages;
import org.mule.transport.cics.util.Constants;

/**
 * Adds the header to the binary request message to be sent to the mainframe (CICS)
 */
public class OGISCopyBookOutgoingHandler extends AbstractMessageAwareTransformer {
    public static int HEADER_LENGTH = 328;

    // cache for headers sent to mainframe. (operation name is used in key)
    private Map headerMap = new HashMap(); 
    
    private String encoding = Constants.CICS_DEFAULT_ENCODING;

    /** Gets the encoding used to write header in mainframe request message */
    public String getEncoding() {
      return encoding;
    }

    /** Sets the encoding used to write header in mainframe request message */
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

        if (message.getExceptionPayload() != null) return message;

        try {
            Operation operation = (Operation) message.getProperty("operation", null);
            String maxSize = (String) getEndpoint().getProperty("maxSize");

            int size = 16000;
            if (maxSize != null && maxSize.length() > 0) {
                size = Integer.parseInt(maxSize);
            }

            // The header data is constructed from configuration file.
            // So, it can be cached, and re-used when same operation is invoked twice.
            String interfaceFile = message.getStringProperty("interfaceFile", "");
            String key = interfaceFile + "$" + operation.getName();
            byte[] header = (byte[]) headerMap.get(key);
            if (header == null) {
              header = new byte[HEADER_LENGTH];
              java.util.Arrays.fill(header, (byte) ' '); // init header

              String ipAddress = (String) getEndpoint().getProperty("ipAddress");
              String userName  = (String) getEndpoint().getProperty("userName");
              String transactionID = operation.getProperty().getTransactionID();
              String applProgramName = operation.getProperty().getApplProgramName();
              String menuID = operation.getProperty().getMenuID();


              dciHeaderSerialize(header, getEncoding(), size,ipAddress,userName, 
                                 transactionID, applProgramName, menuID);
              headerMap.put(key, header);
            }

            final byte[] headerBytes = header;

            // The previous transformer (XmlToCopyBook) returns an OutputHandler object.
            // The OutputHandler can be used to write the copybook bytes to CICS input record.
            final OutputHandler previousHandler = (OutputHandler) message.getPayload(OutputHandler.class);
            final int maximumSize = size + HEADER_LENGTH;

            // This transformer returns an OutputHandler (to defer writing of header). 
            // The CICSMessageDispatcher calls write method on OutputHandler to write to CICS input record.
            return new OutputHandler() {

                public void write(MuleEvent event, OutputStream out) throws IOException
                {
                    // A FilterOutputStream is set to ensure we do not write more than maximumSize
                    OutputStream fos = new FilterOutputStream(out) {
                       private int count = 0;

                       public void write(int b) throws IOException {
                           if (count < maximumSize) {
                               out.write(b);
                               count++;
                           }
                       }

                       public void write(byte[] bytes) throws IOException {
                           if (count + bytes.length <= maximumSize) {
                               out.write(bytes);
                               count += bytes.length;
                           }
                       }
                    };

                    // First write the bytes of header
                    fos.write(headerBytes);

                    // Then call write method on OutputHandler returned by previous transformer (i.e XmlToCopyBook)
                    previousHandler.write(event, fos);
                }
            };
        
        } catch (Throwable e) {
            throw new TransformerException(this, e);
        }
    }

    /**
     * Sets the DCI header
     *
     * @param header     the byte array in which to set the header data.
     * @param encoding   encoding of the byte array.
     * @param size       size
     * @param ipAddress  ip address
     * @param userName   user name
     * @param transactionID transaction ID
     * @param applProgramName application program name.
     * @param menu id    menu ID
     */
    private void dciHeaderSerialize(byte[] header, String encoding, int size, String ipAddress, String userName,
                                   String transactionID, String applProgramName, String menuID) {
        setBytes(header, encoding, 0, 8, size + HEADER_LENGTH); // size
        setBytes(header, encoding, 8, 14, ipAddress); // IP address
        setBytes(header, encoding, 24, 8, userName); // user name
        setBytes(header, encoding, 32, 4, transactionID); // transaction id
        setBytes(header, encoding, 36, 8, applProgramName); // appl program name
        setBytes(header, encoding, 128, 8, menuID); // menu id
        setBytes(header, encoding, 290, 8, size); // size
    }

    /**
     * <code>setBytes</code> Copy the string value into the bytes.
     *
     * @param bytes    byte array to be set.
     * @param encoding encoding of byte array.
     * @param start    starting offset into byte array where value is to be copied.
     * @param length   number of bytes to be copied.
     * @param str      value to be copied.
     */
    protected void setBytes(byte[] bytes, String encoding, int start, int length, String str) {

        if (str == null) str = "";
        byte[] srcBytes = null;
        try {
            srcBytes = str.getBytes(encoding);
        } catch (Exception e) {
            srcBytes = new byte[0];
        }

        for (int i = 0; i < length && i < bytes.length; i++) {
            if (i < srcBytes.length) {
                bytes[i + start] = srcBytes[i];
            } else {
                bytes[i + start] = (byte) ' ';
            }
        }
    }

    /**
     * <code>setBytes</code> Copy the numeric value into the bytes.
     *
     * @param bytes     Byte array to be set.
     * @param encoding  encoding of bytes.
     * @param start     starting offset into byte array where value is to be copied.
     * @param length    number of bytes to be copied.
     * @param num       number to be copied.
     */
    protected void setBytes(byte[] bytes, String encoding, int start, int length, int num) {

        char[] x = new char[length];
        java.util.Arrays.fill(x, '0');

        java.text.DecimalFormat df = new java.text.DecimalFormat(new String(x));
        String str = df.format(num);

        byte[] srcBytes = null;
        try {
            srcBytes = str.getBytes(encoding);
        } catch (Exception e) {/* ignore */ }

        for (int i = 0; i < length && i < bytes.length; i++) {
            if (i < srcBytes.length) {
                bytes[i + start] = srcBytes[i];
            } else {
                bytes[i + start] = (byte) ' ';
            }
        }
    }
}
