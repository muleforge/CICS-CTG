package org.mule.transport.cicsStreaming.transformers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.XMLEvent;

import org.mule.api.ExceptionPayload;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.message.DefaultExceptionPayload;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.transport.cics.util.Constants;

/**
 * This transformer converts the XML message into a SOAP message i.e it adds the
 * XML tags for <soap:Envelope> and <soap:Body>
 */
public class XmlToRest extends AbstractMessageAwareTransformer {
	private static XMLInputFactory inputFactory;
	private static XMLOutputFactory outputFactory;

	static {
		inputFactory = XMLInputFactory.newInstance();
		outputFactory = XMLOutputFactory.newInstance();
		outputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
	}

	public Object transform(MuleMessage message, String encoding) throws TransformerException {

		ExceptionPayload e = message.getExceptionPayload();
		if (e != null && e.getRootException() != null) {
			logger.error(e.getRootException());
			message.setExceptionPayload(new DefaultExceptionPayload(e.getRootException()));
			return message;
		}

		//Do not do any processing if returning back WADL to client.
		if (message.getBooleanProperty(Constants.SKIP_FURTHER_PROCESSING, false)) {
			return message;
		}

		try {
		    byte[] bytes = message.getPayloadAsBytes();
		    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			XMLEventReader reader = inputFactory.createXMLEventReader(bis);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			XMLEventWriter writer = outputFactory.createXMLEventWriter(bos);

			boolean writeEvent = true;
			while (reader.hasNext()) {
				XMLEvent event = (XMLEvent) reader.next();
				
				String elementName = "";
				if (event.isStartElement()) {
					elementName = event.asStartElement().getName().getLocalPart();
				} else if (event.isEndElement()) {
					elementName = event.asEndElement().getName().getLocalPart();
				}

				// Check for header, and skip events inside it.
				if ("lzaplhdr-area".equals(elementName)) {
					if (event.isStartElement()) {
						writeEvent = false;
					} else if (event.isEndElement()) {
						writeEvent = true;
					}
					continue;
				}

				if (writeEvent && !"replyData".equals(elementName)) {
					writer.add(event);
				}
			}

			writer.close();

			byte[] restReply = bos.toByteArray();
			return restReply;

		} catch (Throwable ex) {
			logger.error(ex);
			logger.error(e.getRootException());
			message.setExceptionPayload(new DefaultExceptionPayload(e.getRootException()));
			return message;
		}
	}
}
