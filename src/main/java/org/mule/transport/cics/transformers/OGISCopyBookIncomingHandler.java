package org.mule.transport.cics.transformers;

import java.io.UnsupportedEncodingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.mule.api.MuleMessage;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.transport.cics.i18n.CicsMessages;
import org.mule.transport.cics.util.Constants;

/**
 * Checks the header of the response message from mainframe (CICS).
 */
public class OGISCopyBookIncomingHandler extends AbstractMessageAwareTransformer {

  private static Log logger = LogFactory.getLog(OGISCopyBookIncomingHandler.class);

  /** length of the header */
  public static int HEADER_LENGTH = 328;
  
  private String encoding = Constants.CICS_DEFAULT_ENCODING;

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
        CicsMessages messages = new CicsMessages();
        throw new RuntimeException(messages.invalidEncodingForTransformer(getClass().getName(), encoding).toString());		
    }
    this.encoding = encoding;
  }

  public Object transform(MuleMessage message, String encoding) throws TransformerException {
    try {
        if (message.getExceptionPayload() != null)
            return message;
        boolean skipProcessing = message.getBooleanProperty(Constants.SKIP_RESPONSE_TRANSFORMER, false);
        if (skipProcessing) return message;
            
        byte[] copyBookBytes = message.getPayloadAsBytes();
        return transform(copyBookBytes, getEncoding());
    } catch (Exception e) {
        throw new TransformerException(this, e);
    }
  }

  protected Object transform(byte[] copyBookBytes, String encoding) throws Exception {

    if (copyBookBytes.length < HEADER_LENGTH) {
        CicsMessages messages = new CicsMessages();
        throw new Exception(messages.insufficientResponseLength().toString());
    }

    // Read the header from the mainframe response.
    ApplHeader header = applHeaderDeserialize(copyBookBytes, encoding);

    // Log the return code in the header.
    logger.info("HDR-RTN-CD:[" + header.getLzaplhdrRtncd() + "]");
    if (header.getLzaplhdrRtncd().equals(MAINFRAME_PROC_OK)) {

        byte[] bytesWithoutHeader = new byte[copyBookBytes.length - HEADER_LENGTH];

        System.arraycopy(copyBookBytes, HEADER_LENGTH, bytesWithoutHeader, 0, bytesWithoutHeader.length);

        logger.debug("user data length = " + header.getLzaplhdrUserdataLen());
        // Pad the bytesWithoutHeader with spaces.
        int fillStart = header.getLzaplhdrUserdataLen();
        int fillEnd = bytesWithoutHeader.length;

        if (fillStart < fillEnd) {
            java.util.Arrays.fill(bytesWithoutHeader, fillStart, fillEnd, (byte) ' ');
        }

        return bytesWithoutHeader;

    } else {
        // Return fault code in error message.
        String faultCode = header.getLzaplhdrRtncd();
        CicsMessages messages = new CicsMessages();
        throw new Exception(messages.errorInResposnse(faultCode).toString());
    }
  }

  /**
   * Read the header from the mainframe response.
   *
   * @param data   the mainframe response.
   * @param encoding encoding of mainframe response.
   * @return ApplHeader object with the header data.
   */
  public ApplHeader applHeaderDeserialize(byte[] data, String encoding) {

    logger.debug("###### applHeaderDeserialize(start) ######");

    // set items to ApplHeader
    ApplHeader header = new ApplHeader();
    header.setLzaplhdrMenuid(getString(data, encoding, 128, 8));
    header.setLzaplhdrPgmid(getString(data, encoding, 136, 8));
    header.setLzaplhdrConvsign(getString(data, encoding, 144, 1));
    header.setLzaplhdrDciReserve(getString(data, encoding, 145, 23));
    header.setLzaplhdrRtncd(getString(data, encoding, 168, 2));
    header.setLzaplhdrRsncd(getString(data, encoding, 170, 4));
    header.setLzaplhdrOthcd(getString(data, encoding, 174, 8));
    header.setLzaplhdrMsgid(getString(data, encoding, 182, 7));
    header.setLzaplhdrMsgkbn(getString(data, encoding, 189, 1));
    header.setLzaplhdrMsg(getString(data, encoding, 190, 100));
    header.setLzaplhdrUserdataLen(Integer.parseInt(getString(data, encoding, 290, 8)));
    header.setLzaplhdrApplReserve(getString(data, encoding, 298, 30));

    logger.debug("###### applHeaderDeserialize( end ) ######");
    return header;
  }

  /**
   * <code>getString</code> reads the value from the byte array.
   *
   * @param bytes  <code>byte[]</code> binary data.
   * @param encoding  encoding of byte array.
   * @param start  <code>int</code> starting offset.
   * @param length <code>int</code> number of bytes from starting offset.
   * @return <code>String</code> value.
   */
  protected String getString(byte[] bytes, String encoding, int start, int length) {

    if (start > bytes.length)
        return "";

    if (start + length > bytes.length)
        length = bytes.length - start;

    try {
        return new String(bytes, start, length, encoding).trim();
    } catch (UnsupportedEncodingException e) {
        char[] buffer = new char[length];
        java.util.Arrays.fill(buffer, '?');
        return new String(buffer);
    }
  }
}
