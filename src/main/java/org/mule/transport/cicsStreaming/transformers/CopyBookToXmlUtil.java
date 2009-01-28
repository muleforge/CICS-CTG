package org.mule.transport.cicsStreaming.transformers;

import java.io.InputStream;
import java.util.Iterator;
import javax.xml.stream.XMLStreamWriter;

import org.mule.transport.cics.i18n.CicsMessages;
import org.mule.transport.cics.util.XsdElement;
import org.mule.transport.cics.util.XsdReader;
import org.mule.transport.cics.util.XsdReaderFactory;

/**
 * Converts mainframe binary data to XML as per the given Xml schema (XSD)
 * It is assumed that the XSD file will have a length and type attribute
 * for each element. The length and type attributes are used to set
 * the XML element's value from the mainframe bytes.
 */
class CopyBookToXmlUtil {

    /** mainframe binary data */
    private InputStream copybookStream;
    private String copybookEncoding;

    /** COBOL data types */
    private static final String TYPE_G = "G"; // double-byte Japanese data.
    private static final String TYPE_9 = "9"; // numeric-data.
    private static final String TYPE_X = "X"; // character-data.

    /**
     * Transforms the mainframe binary data to XML.
     * The XML is written to the StaX writer object.
     *
     * @param msgBytes    mainframe binary data.
     * @param xsdFile     XSD file containing schema for output XML.
     * @param writer      writer to write XML.
     */
    protected void transform(InputStream copybookStream, String copybookEncoding, String xsdFile, XMLStreamWriter writer) throws Exception {

        this.copybookStream = copybookStream; // mainframe data.
        this.copybookEncoding = copybookEncoding; 

        if (xsdFile == null || xsdFile.equals("")) {
            return; // No transformation is done.
        }

        XsdReader xsdReader = XsdReaderFactory.getInstance().getXsdReader(xsdFile);
        if (xsdReader == null) return;


        XsdElement rootXsdElement = xsdReader.getRootXsdElement();
        String namespace = xsdReader.getNamespaceOfDocument();

        writer.setPrefix("", namespace);
        writer.writeStartElement(namespace, rootXsdElement.getName());
        writeXmlChildren(writer, rootXsdElement);
        writer.writeEndElement();

        writer.flush();
    }

    /**
     * Writes all children recursively for an XML element.
     * @param writer     writer to write XML.
     * @param xsdElement the XSD element.
     */
    private void writeXmlChildren(XMLStreamWriter writer, XsdElement xsdElement) throws Exception {

        // Iterate over the children of the XSD element.
        Iterator iterator = xsdElement.getChildIterator();
        while (iterator.hasNext()) {
            XsdElement child = (XsdElement) iterator.next();

            long occurs = child.getOccurs();
            if (occurs <= 1) occurs = 1;
            for (int i = 0; i < occurs; i++) {
                writer.writeStartElement(child.getName());
                writeElementValue(writer, child);
                writeXmlChildren(writer, child);
                writer.writeEndElement();
            }
        }
    }

    /**
     * Writes the value of an XML element.
     *
     * @param writer   writer to write XML
     * @param element  XSD element
     */
    private void writeElementValue(XMLStreamWriter writer, XsdElement xsdElement) throws Exception {
        int length = xsdElement.getLength();
        String dataType = xsdElement.getType(); // Coboltype i.e 'X', 'N' or 'G'
        if (length <= 0 || dataType == null || dataType.equals("")) {
            return;
        }

        // Each character is 2 bytes for dataType = 'G'
        if (TYPE_G.equals(dataType)) {
            length = length * 2; 
        }

        byte[] bytes = new byte[length];
        int i = 0;
        while (i < length) {
          int c = copybookStream.read();
          if (c == -1) {
              CicsMessages messages = new CicsMessages();
            throw new Exception(messages.copyBookToXmlError(xsdElement.getName()).toString());
          }

          bytes[i++] = (byte) c;
        }

        try {
            writer.writeCharacters(new String(bytes, copybookEncoding));
        } catch (Exception e) {
            CicsMessages messages = new CicsMessages();
            throw new Exception(messages.errorReadingXsdEleFromResponse(xsdElement.getName()).toString(), e);
        }
    }
}
