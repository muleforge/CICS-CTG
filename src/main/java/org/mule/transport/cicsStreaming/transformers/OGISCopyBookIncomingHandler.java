package org.mule.transport.cicsStreaming.transformers;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.transport.cics.i18n.CicsMessages;
import org.mule.transport.cics.util.Constants;

/**
 * Checks the header of the response message from mainframe (CICS).
 */
public class OGISCopyBookIncomingHandler extends AbstractMessageAwareTransformer {

  private static Log logger = LogFactory.getLog(OGISCopyBookIncomingHandler.class);

  private String encoding = Constants.CICS_DEFAULT_ENCODING;

  /** length of the header */
  public static int HEADER_LENGTH = 328;

  /** The value to indicate that the mainframe procedure executed OK */
  private static final String MAINFRAME_PROC_OK = "00";
 
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
    try {
        if (message.getExceptionPayload() != null) return message;

        boolean skipProcessing = message.getBooleanProperty(Constants.SKIP_RESPONSE_TRANSFORMER, false);
        if (skipProcessing) return message;
 
        InputStream copybookStream = (InputStream) message.getPayload(InputStream.class);
        return transform(copybookStream, getEncoding());
    } catch (Exception e) {
        throw new TransformerException(this, e);
    }
  }

  protected Object transform(InputStream copybookStream, String encoding) throws IOException {

    // Read the header from the mainframe response.
    ApplHeader header = applHeaderDeserialize(copybookStream, encoding);

    // Log the return code in the header.
    logger.info("HDR-RTN-CD:[" + header.getLzaplhdrRtncd() + "]");
    if (header.getLzaplhdrRtncd().equals(MAINFRAME_PROC_OK)) {

        final int dataLength = header.getLzaplhdrUserdataLen();
        logger.debug("user data length = " + dataLength);

        // Return input stream (This transformer reads only header, 
        // the remaining response is read by CopybookToXml)
        // The FilterInputStream takes care to return BLANK character after user data length.
        return new FilterInputStream(copybookStream) {

           int count = 0;
           public int read() throws IOException {

               int c = super.read();
               if (c == -1) return c;
 
               count++;
               return (count <= dataLength) ? c : ' ';
           }
        };
    } else {
        // Return fault code in error message.
        String faultCode = header.getLzaplhdrRtncd();
        throw new IOException(CicsMessages.errorInResposnse(faultCode).toString());
    }
  }

  /**
   * Read the header from the mainframe response.
   *
   * @param data   the mainframe response.
   * @param encoding encoding of mainframe response.
   * @return ApplHeader object with the header data.
   */
  public ApplHeader applHeaderDeserialize(InputStream is, String encoding) 
         throws IOException {

    logger.debug("###### applHeaderDeserialize(start) ######");

    // set items to ApplHeader
    ApplHeader header = new ApplHeader();
    is.skip(128);
    header.setLzaplhdrMenuid(getString(is, encoding, 8));
    header.setLzaplhdrPgmid(getString(is, encoding, 8));
    header.setLzaplhdrConvsign(getString(is, encoding, 1));
    header.setLzaplhdrDciReserve(getString(is, encoding, 23));
    header.setLzaplhdrRtncd(getString(is, encoding, 2));
    header.setLzaplhdrRsncd(getString(is, encoding, 4));
    header.setLzaplhdrOthcd(getString(is, encoding, 8));
    header.setLzaplhdrMsgid(getString(is, encoding, 7));
    header.setLzaplhdrMsgkbn(getString(is, encoding, 1));
    header.setLzaplhdrMsg(getString(is, encoding, 100));
    header.setLzaplhdrUserdataLen(Integer.parseInt(getString(is, encoding, 8)));
    header.setLzaplhdrApplReserve(getString(is, encoding, 30));

    logger.debug("###### applHeaderDeserialize( end ) ######");
    return header;
  }

  /**
   * <code>getString</code> reads the value from the byte array.
   *
   * @param is  <code>InputStream</code> binary data
   * @param encoding  encoding of byte array.
   * @param length <code>int</code> number of bytes to read.
   * @return <code>String</code> value.
   */
  protected String getString(InputStream is, String encoding, int length) throws IOException {

    byte[] bytes = new byte[length];
    int i = 0;
    while (i < length) {
        int c = is.read();
        if (c == -1) {
            throw new IOException(CicsMessages.insufficientResponseLength().toString()); 
        }

        bytes[i++] = (byte) c;
    }

    try {
        return new String(bytes, encoding).trim();
    } catch (UnsupportedEncodingException e) {
        char[] buffer = new char[length];
        java.util.Arrays.fill(buffer, '?');
        return new String(buffer);
    }
  }
}
