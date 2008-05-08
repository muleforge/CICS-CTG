package org.mule.transport.cics.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Describe class <code>XmlUtils</code> here.
 *
 * @author <a href="mailto:makoto@zebra"></a>
 * @version 1.0
 */
public class XmlUtils {

	/** StAX Factory for creating reader to read XML */
	private static XMLInputFactory inputFactory;

	/** StAX Factory for creating writer to write XML */
	private static XMLOutputFactory outputFactory;

	/** StAX Factory for creating XML events used to write XML */
	private static XMLEventFactory eventFactory;

	static {
		inputFactory = XMLInputFactory.newInstance();
		outputFactory = XMLOutputFactory.newInstance();
		eventFactory = XMLEventFactory.newInstance();
	}

	/**
	 * Returns the name of the root element of an XML message.
     * 
	 * @param xml     XML message
	 * @return the name of the root element
	 * @exception Exception if an error occurs
	 */
	public static String getRootElementName(byte[] xml) throws Exception {

		ByteArrayInputStream is = new ByteArrayInputStream(xml);
		XMLEventReader reader = inputFactory.createXMLEventReader(is);

		while (reader.hasNext()) {
			XMLEvent event = (XMLEvent) reader.next();

			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				return startElement.getName().getLocalPart();
			}
		}

        reader.close();
		return "";
	}

	/**
	 * Describe <code>getInternalXML</code> method here.
	 *
	 * @param is an <code>InputStream</code> value
	 * @param tagName a <code>String</code> value
	 * @param namespace a <code>String</code> value
	 * @return a <code>byte[]</code> value
	 * @exception Exception if an error occurs
	 */
	public static byte[] getInternalXML(InputStream is, String tagName, String namespace) throws Exception {

		if (namespace == null) namespace = "";

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XMLEventWriter writer = outputFactory.createXMLEventWriter(bos);

		// Loop over XML input stream and process events
		boolean bStartWritingEvents = false;

		XMLEventReader reader = inputFactory.createXMLEventReader(is);

		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			if (checkNameAndNamespace(event, tagName, namespace) == true) {
				if (event.isStartElement() && !bStartWritingEvents) {
					writer.add(eventFactory.createStartDocument());
					bStartWritingEvents = true;
					continue;
				} else if (event.isEndElement()) {
					writer.add(eventFactory.createEndDocument());
					bStartWritingEvents = false;
					break;
				}
			}

			if (bStartWritingEvents) {
				writer.add(event);
			}
		}

		writer.close();
        reader.close();

		return bos.toByteArray();
	}

	private static boolean checkNameAndNamespace(XMLEvent event, String tagName, String namespace) {
		if (event.isStartElement()) {
			StartElement element = (StartElement) event;
            QName name = element.getName();
			return tagName.equalsIgnoreCase(name.getLocalPart())
                   && namespace.equalsIgnoreCase(name.getNamespaceURI());
		} else if (event.isEndElement()) {
			EndElement element = (EndElement) event;
            QName name = element.getName();
			return tagName.equalsIgnoreCase(name.getLocalPart())
                   && namespace.equalsIgnoreCase(name.getNamespaceURI());
		}

		return false;
	}
}
