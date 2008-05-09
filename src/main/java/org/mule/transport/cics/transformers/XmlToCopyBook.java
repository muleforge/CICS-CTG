package org.mule.transport.cics.transformers;

import java.io.ByteArrayInputStream;
import java.util.Stack;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.mule.transport.cics.esbInterface.Operation;
import org.mule.transport.cics.util.Constants;
import org.mule.transport.cics.util.XsdElement;
import org.mule.transport.cics.util.XsdReader;
import org.mule.transport.cics.util.XsdReaderFactory;

import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.MuleMessage;
import org.mule.message.DefaultExceptionPayload;
import org.mule.api.transformer.TransformerException;

/**
 * Transforms Xml message into mainframe binary data.
 */
public class XmlToCopyBook extends AbstractMessageAwareTransformer {

    private static Log logger = LogFactory.getLog(XmlToCopyBook.class);

    /**
     * Transforms Xml message into mainframe binary data.
     *
     * @param message Mule message containing xml data.
     *
     * @return binary array in mainframe format.
     * @throws TransformerException
     */
    public Object transform(MuleMessage message, String encoding) throws TransformerException {
        try {
            if (message.getExceptionPayload() != null) return message;

            Operation operation = (Operation) message.getProperty("operation", null);
            String inboundXsd = operation.getInboundXsd();
            if (inboundXsd == null || inboundXsd.equals("")) {
                return message; // No transformation is required.
            }

            byte[] xmlBytes = message.getPayloadAsBytes();
            return transform(xmlBytes, inboundXsd);
        } catch (Throwable e) {
            logger.error(e);
            message.setExceptionPayload(new DefaultExceptionPayload(e));
            return message;
        }
    }

    /**
     * Converts xml into mainframe binary data.
     *
     * @param bytes XML bytes
     * @param xsdFile the xsd file
     * @return byte array in mainframe format.
     * @throws Exception
     */
    protected String transform(byte[] xmlBytes, String xsdFile) throws Exception {

        XmlToCopyBookUtil handler = new XmlToCopyBookUtil(xsdFile);

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader =
             factory.createXMLStreamReader(new ByteArrayInputStream(xmlBytes));

        for (int event = reader.next();
                 event != XMLStreamConstants.END_DOCUMENT;
                 event = reader.next()) {
            switch (event) {
              case XMLStreamConstants.START_ELEMENT:
                handler.startElement(reader.getLocalName());
                break;
              case XMLStreamConstants.END_ELEMENT:
                handler.endElement(reader.getLocalName());
                break;
              case XMLStreamConstants.CHARACTERS:
                handler.characters(reader.getText());
                break;
            }
        }

        reader.close();
        return handler.copyBookBuffer.toString();
    }

    /**
     * Class to convert Xml to mainframe data.
     */
    public static class XmlToCopyBookUtil {

        /** buffer to hold the mainframe data */
        private StringBuffer copyBookBuffer = new StringBuffer();

        /** Stack to store value of each xml element in input message */
        private Stack xmlValueStack = new Stack();

        /** COBOL data types */
        private static final String TYPE_G = "G"; // double-byte Japanese data.
        private static final String TYPE_9 = "9"; // numeric-data.
        private static final String TYPE_X = "X"; // character-data.

        private XsdReader xsdReader;
        private String xsdFile;

        public XmlToCopyBookUtil(String xsdFile) throws Exception {
            this.xsdFile = xsdFile;
            this.xsdReader = XsdReaderFactory.getInstance().getXsdReader(xsdFile);
        }

        /**
         * Called for a start element.
         */
        public void startElement(String qName) {
            xmlValueStack.push("");
        }

        /**
         * Called for a end element.
         */
        public void endElement(String qName) {

            String contents = (String) xmlValueStack.pop();

            XsdElement xsdElement = xsdReader.getXsdElement(qName);
            if (xsdElement == null) {
                logger.debug("Skipping " + qName + ". XSD element not found in file " + xsdFile);
                return;
            }

            int length = xsdElement.getLength();
            String dataType = xsdElement.getType();
            if (length < 0 || dataType == null) {
                return;
            }

            if (!(dataType.equals(TYPE_X) || dataType.equals(TYPE_9) ||
                dataType.equals(TYPE_G))) {
                return;
            }

            // Edit the string to make it the correct length.
            String str = editString(contents, dataType, length);
            copyBookBuffer.append(str);
        }

        /**
         * Called for character data in XML.
         */
        public void characters(String text) {

            String value = (String) xmlValueStack.pop();
            if (!"".equals(value)) {
                xmlValueStack.push(value + text);
            } else {
                xmlValueStack.push(text);
            }
        }

        /**
         * Edit the string to make it the correct length.
         *
         * @param target    the string to be edited.
         * @param dataType  the COBOL datatype.
         * @param length    the length of COBOL field.
         * @return the edited string.
         */
        private String editString(String target, String dataType, int length) {

            // Check if length is greater than required.
            if (target.length() > length) {
                target = target.substring(0, length);
            }

            // Edit end of line character.
            String targetCRLF = changeEndOfLineCharacter(target);

            // dataTypeがTYPE_G の場合のみ、CRLFに変換した文字数だけ
            // length を増やし、以降を処理します
            if (TYPE_G.equals(dataType)) {
                length += targetCRLF.length() - target.length();
            }

            if (targetCRLF.length() == length) {
                return targetCRLF;
            }

            if (targetCRLF.length() > length) {
                return targetCRLF.substring(0, length);
            }

            // Pad the string as the length is less than required.
            char padChar = ' ';
            if (TYPE_G.equals(dataType)) {
                padChar = '　'; // pad with double byte space for G datatype.
            } else if (TYPE_9.equals(dataType)) {
                padChar = '0';  // pad with zero-es for numeric datatype.
            }

            char[] padding = new char[length - targetCRLF.length()];
            java.util.Arrays.fill(padding, padChar);
            if (padChar == '0') {
                return target + new String(padding); // pad on the right-side.
            } else {
                return new String(padding) + target; // pad on the left-side.
            }
        }

        /**
         * Changes the end of line character.
         * @param target the text to be edited.
         *
         * @return the edited text.
         */
        private static String changeEndOfLineCharacter(String target) {

            String CRLF_STR = "\r\n";
            if (target == null || target.length() == 0) {
                return target;
            }
            int targetLength = target.length();

            boolean changeRequired = false;
            for (int i = 0; i < targetLength; i++) {
                char c = target.charAt(i);
                switch (c) {
                case '\r':
                    if (i + 1 < targetLength && target.charAt(i + 1) != '\n')
                        changeRequired = true;

                case '\n':
                    if (i - 1 >= 0 && target.charAt(i - 1) != '\r')
                        ;
                    changeRequired = true;
                }
            }

            if (!changeRequired)
                return target;

            StringBuffer buffer = new StringBuffer(targetLength);
            boolean prevCR = false;
            for (int i = 0; i < targetLength; i++) {
                char c = target.charAt(i);
                switch (c) {
                case '\r':
                    if (prevCR) {
                        // if the previous character is CR, then it is registered as CRLF.
                        buffer.append(CRLF_STR);
                        prevCR = false;
                    }
                    if (i == (targetLength - 1)) {
                        // if it is the last character, then it is registered as CRLF.
                        buffer.append(CRLF_STR);
                    } else {
                        // set the flag to true.
                        prevCR = true;
                    }
                    break;
                case '\n':
                    // if a return code is found, then it is registered as CRLF.
                    buffer.append(CRLF_STR);
                    prevCR = false;
                    break;
                default:
                    if (prevCR) {
                        // if the previous charater is CR, then it is registered as CRLF.
                        buffer.append(CRLF_STR);
                        prevCR = false;
                    }
                    // if the charater is not the return code, then that character is registered.
                    buffer.append(c);
                    break;
                }
            }
            return buffer.toString();
        }
    }
}
