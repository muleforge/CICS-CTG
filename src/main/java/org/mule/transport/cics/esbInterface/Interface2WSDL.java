package org.mule.transport.cics.esbInterface;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.mule.transport.cics.util.XsdReader;
import org.mule.transport.cics.util.XsdReaderFactory;
import org.mule.util.IOUtils;

public class Interface2WSDL {

	private static final String WSDLSOAP_NS = "http://schemas.xmlsoap.org/wsdl/soap/";
	private static final String WSDL_NS = "http://schemas.xmlsoap.org/wsdl/";
	private static final String XMLSCHEMA_NS = "http://www.w3.org/2001/XMLSchema";
	private XsdReaderFactory xsdFactory = XsdReaderFactory.getInstance();

	/**
     * Entry point to execute from command-line console.
     *  
     * Usage: java org.mule.transport.cics.Interface2WSDL <interfaceFile>
     */
	public static void main(String[] args) throws Exception {

		if (args.length < 2) {
			System.out.println("Usage: java org.mule.transport.cics.Interface2WSDL <interfaceFile> <serviceLocation>");
			System.out.println("<interfaceFile>   - path to the interface file");
			System.out.println("<serviceLocation> - the location of the webservice e.g http://xyz.com/abc");
			return;
		}

		String wsdl = new Interface2WSDL().getWSDL(args[0], args[1]);
		System.out.println(wsdl);
		return;
	}
	
	/**
     * Writes WSDL file for a given interface file.
     *
     * @param interfaceFile
     *            interface file
     * @throws Exception
     */
	public String getWSDL(String interfaceFile, String serviceLocation) throws Exception {

		EsbInterface esbInterface =
            EsbInterfaceDigester.getInstance().parse(interfaceFile);

		String targetNs = EsbInterface.getNamespaceOfWsdl(interfaceFile);
		return getWSDL(esbInterface, targetNs, serviceLocation);
	}

	/**
     * Writes WSDL file for a given interface file.
     *
     * @param esbInterface
     *            Java bean containing information in interface file.
     * @param targetNs
     *            the target namespace of WSDL.
     * @throws Exception
     */
	public String getWSDL(EsbInterface esbInterface, String targetNs, String serviceLocation) throws Exception {

		List operations = esbInterface.getOperations();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		factory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
		XMLStreamWriter writer = factory.createXMLStreamWriter(bos);

		writer.writeStartDocument();

		// START - <wsdl:definitions>
		writer.writeStartElement("wsdl", "definitions", WSDL_NS);
		writer.writeAttribute("targetNamespace", targetNs);

		writeNamespaces(writer, targetNs); // write xmlns:
		// declarations
		writeTypes(writer, targetNs, operations);    // write <wsdl:types>
		writeMessages(writer, targetNs, operations); // write <wsdl:messages>
		writePortType(writer, operations);           // write <wsdl:portType>
		writeBindings(writer, targetNs, operations); // write <wsdl:bindings>
		writeService(writer, serviceLocation);       // write <wsdl:service>

		writer.writeEndElement();
		// END - </wsdl:definitions>

		writer.writeEndDocument();
		writer.close();

		String wsdl = new String(bos.toByteArray());
		return wsdl;
	}

	/**
     * Writes the namespaces in the WSDL document.
     *
     * @param writer
     *            StAX object to write XML.
     * @param targetNs
     *            the target namespace of WSDL.
     * @param operations
     *            list of operation beans (Each bean contains info in interface
     *            file)
     * @throws Exception
     */
	private void writeNamespaces(XMLStreamWriter writer, String targetNs) throws Exception {
		writer.writeNamespace("impl", targetNs);
		writer.writeNamespace("xsd", XMLSCHEMA_NS);
	}

	/**
     * Writes the schema types for each operation in the WSDL document.
     * Each operation will have a schema type for request, response and/or fault message.
     *
     * @param writer
     *            StAX object to write XML.
     * @param targetNs
     *            namespace of the WSDL.
     * @param operations
     *            list of operation beans (Each bean contains info in interface file)
     * @throws Exception
     */
	private void writeTypes(XMLStreamWriter writer, String targetNs, List operations) throws Exception {
		// START - <wsdl:types>
		writer.writeStartElement("wsdl", "types", WSDL_NS);
		
		//START - <s:schema>
		writer.writeStartElement("xsd", "schema", XMLSCHEMA_NS);
		writer.writeAttribute("elementFormDefault", "qualified");
		writer.writeAttribute("targetNamespace", targetNs);
		
		List msgElementNames = new ArrayList();
		for (int i=0; i<operations.size(); i++) {
			Operation operation = (Operation) operations.get(i);
			String operationName = operation.getName();
			
			String inboundXsd = operation.getInboundXsd();
			if (inboundXsd != null && !inboundXsd.equals("")) {
			  XsdReader xsdReader = xsdFactory.getXsdReader(inboundXsd);
			  String rootElementName = xsdReader.getRootXsdElement().getName();			  			  
			  String elementName = operationName;
			  String elementType = rootElementName + "Type";
			  if(!msgElementNames.contains(elementName)){
				  writer.writeStartElement("xsd", "element", XMLSCHEMA_NS);
				  writer.writeAttribute("name", elementName);
				  writer.writeAttribute("type", "impl:"+elementType);
				  writer.writeEndElement();	
				  msgElementNames.add(elementName);
			  }
			}
			
			String outboundXsd = operation.getOutboundXsd();
			if ( outboundXsd != null && !outboundXsd.equals("")) {
			  XsdReader xsdReader = xsdFactory.getXsdReader(outboundXsd);
			  String rootElementName = xsdReader.getRootXsdElement().getName();			  
			  String elementName = rootElementName;
			  String elementType = rootElementName + "Type";
			  if(!msgElementNames.contains(elementName)){
				  writer.writeStartElement("xsd", "element", XMLSCHEMA_NS);
				  writer.writeAttribute("name", elementName);
				  writer.writeAttribute("type", "impl:"+elementType);
				  writer.writeEndElement();
				  msgElementNames.add(elementName);
			  }
			}
			
			String faultXsd = operation.getFaultXsd();
			if ( faultXsd != null && !faultXsd.equals("")) {
			  XsdReader xsdReader = xsdFactory.getXsdReader(faultXsd);
			  String rootElementName = xsdReader.getRootXsdElement().getName();			  
			  String elementName = rootElementName;
			  String elementType = elementName + "Type";
			  if(!msgElementNames.contains(elementName)){
				  writer.writeStartElement("xsd", "element", XMLSCHEMA_NS);
				  writer.writeAttribute("name", elementName);
				  writer.writeAttribute("type", "impl:"+elementType);
				  writer.writeEndElement();
				  msgElementNames.add(elementName);
			  }
			}
		}
		
		List namespaces = new ArrayList();
		// For each operation, write the inbound/outbound schema to the WSDL.
		for (int i=0; i<operations.size(); i++) {
			Operation operation = (Operation) operations.get(i);
            String [] xsdFiles = {
            		operation.getInboundXsd(),
            		operation.getOutboundXsd(),
            		operation.getFaultXsd()
            };
            
            for (int j=0; j<xsdFiles.length; j++) {
            	String xsd = xsdFiles[j];
            	if (xsd != null && !xsd.equals("")) {
      			  XsdReader xsdReader = xsdFactory.getXsdReader(xsd);      			  
      			  String namespace = xsdReader.getNamespaceOfDocument();
      			  if(!namespaces.contains(namespace)){
      				  namespaces.add(namespace);
      				  writeSchema(writer, xsd);
      			  }
               }
            }
        }
		


		writer.writeEndElement();
		// END - </wsdl:types>
		writer.writeEndElement();
		// END - </s:schema>
	}

	/**
     * Copies the schema from an XSD file into a WSDL file.
     *
     * @param writer
     *            StAX object to write XML.
     * @param xsdFile
     *            the file containing XSD schema. 
     * @throws Exception
     */
	private void writeSchema(XMLStreamWriter writer, String xsdFile) throws Exception {
		
		XsdReader xsdReader = xsdFactory.getXsdReader(xsdFile);
		String rootElementName = xsdReader.getRootXsdElement().getName();
		
		InputStream is = IOUtils.getResourceAsStream(xsdFile, this.getClass());
		XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(is);		

		// Skip events till the first <complexType> tag.		
		while (reader.hasNext()) {
			XMLEvent event = (XMLEvent) reader.next();
			if (event.isStartElement()) {
				String elementName = event.asStartElement().getName().getLocalPart();
				if (elementName.equals("complexType")) {
					writer.writeStartElement("xsd", "complexType", XMLSCHEMA_NS);
                	writer.writeAttribute("name", rootElementName+"Type");					
					break;
				}
			}
		}

		// Write events till the end of the first <complexType> tag.
		int level = 0;
		while (reader.hasNext()) {
			XMLEvent event = (XMLEvent) reader.next();

			if (event.isStartElement()) {
				level ++;
				StartElement startElement = event.asStartElement();
				String elementName = startElement.getName().getLocalPart();
				
				writer.writeStartElement("xsd", elementName, XMLSCHEMA_NS);				

				Iterator iterator = startElement.getAttributes();
				while (iterator != null && iterator.hasNext()) {
					Attribute attribute = (Attribute) iterator.next();
					String name = attribute.getName().getLocalPart();
					String value = attribute.getValue();

					if (name.equals("type")) {
						/*******************************************************
                         * In the WSDL document, we are using the prefix 'xsd:'
                         * for the namespace "http://www.w3.org/2001/XMLSchema"
                         * The XSD file may have a different prefix for above
                         * namespace. So, while writing WSDL, we replace prefix
                         * in XSD file with 'xsd:'
                         ******************************************************/
						int colonIndex = value.indexOf(':');
						if (colonIndex >= 0) {
							value = "xsd:" + value.substring(colonIndex + 1);
						}
					}

					String prefix = attribute.getName().getPrefix();
					if (prefix != null && !prefix.equals("")) {
						name = prefix + ":" + name;
					}

					writer.writeAttribute(name, value);
				}
			} else if (event.isEndElement()) {	
				writer.writeEndElement();
				
				level--;
				String elementName = event.asEndElement().getName().getLocalPart();				
				if (elementName.equals("complexType") && level < 0 ) {
					break;
				}
				
			} else if (event.isCharacters()) {
				String data = event.asCharacters().getData();
				writer.writeCharacters(data);
			}
			// else if (event.getEventType() == event.COMMENT) {
			// String comment = ((Comment) event).getText();
			// writer.writeComment(comment);
			// }
		}

		
	}

	/**
     * Writes the messages for each operation in the WSDL document.
     * Each operation can have a request, response and/or fault message.
     *
     * @param writer
     *            StAX object to write XML.
     * @param targetNs
     *            namespace of the WSDL.
     * @param operations
     *            list of operation beans (Each bean contains info in interface
     *            file)
     * @throws Exception
     */
	private void writeMessages(XMLStreamWriter writer, String targetNs, List operations) throws Exception {
		String wsdlPrefix = ""; //Prefix used for the targetNamespace of this WSDL
		wsdlPrefix = writer.getNamespaceContext().getPrefix(targetNs);
        if( wsdlPrefix != null && wsdlPrefix.length() > 0 ){
        	wsdlPrefix += ":";
        }
        
		for (int i=0; i<operations.size(); i++) {
			Operation operation = (Operation) operations.get(i);
			String operationName = operation.getName();
			
            // Write request message format in WSDL.
            String inboundXsd = operation.getInboundXsd();
			if (inboundXsd != null && !inboundXsd.equals("")) {
                String rootElementName = operationName;

                // START - <wsdl:message>
                writer.writeStartElement("wsdl", "message", WSDL_NS);
                writer.writeAttribute("name", operationName + "Request");

                // START - <wsdl:part>
                writer.writeStartElement("wsdl", "part", WSDL_NS);
                writer.writeAttribute("name", operationName + "Request");
                writer.writeAttribute("element", wsdlPrefix + rootElementName);
                writer.writeEndElement();
                // END - </wsdl:part>

                writer.writeEndElement();
                // END - </wsdl:message>
            }

            // Write response message format in WSDL.
            String outboundXsd = operation.getOutboundXsd();
			if (outboundXsd != null && !outboundXsd.equals("")) {
              XsdReader reader = XsdReaderFactory.getInstance().getXsdReader(outboundXsd);
              String rootElementName = reader.getRootXsdElement().getName(); 
              
              // START - <wsdl:message>
              writer.writeStartElement("wsdl", "message", WSDL_NS);
              writer.writeAttribute("name", operationName + "Response");

              // START - <wsdl:part>
              writer.writeStartElement("wsdl", "part", WSDL_NS);
              writer.writeAttribute("name", operationName + "Response");
              writer.writeAttribute("element", wsdlPrefix + rootElementName);
              writer.writeEndElement();
              // END - </wsdl:part>

              writer.writeEndElement();
              // END - </wsdl:message>
            }

            // Write fault message format in WSDL.
            String faultXsd = operation.getFaultXsd();
            if (faultXsd != null && !"".equals(faultXsd)) {
                XsdReader xsdReader = xsdFactory.getXsdReader(faultXsd);
                String rootElementName = xsdReader.getRootXsdElement().getName();

                // START - <wsdl:message>
                writer.writeStartElement("wsdl", "message", WSDL_NS);
                writer.writeAttribute("name", operationName + "Fault");

                // START - <wsdl:part>
                writer.writeStartElement("wsdl", "part", WSDL_NS);
                writer.writeAttribute("name", operationName + "Fault");
                writer.writeAttribute("element", wsdlPrefix + rootElementName);
                writer.writeEndElement();
                // END - </wsdl:part>

                writer.writeEndElement();
                // END - </wsdl:message>
            }
		} // for{operations}
	}

	/**
     * Writes the port type for each operation in the WSDL document. 
     * Each operation can have a request, response and/or fault message.
     *
     * @param writer
     *            StAX object to write XML.
     * @param operations
     *            list of operation beans (Each bean contains info in interface
     *            file)
     * @throws XMLStreamException
     */
	private void writePortType(XMLStreamWriter writer, List operations) throws XMLStreamException {

		// START - <wsdl:portType>
		writer.writeStartElement("wsdl", "portType", WSDL_NS);
		writer.writeAttribute("name", "MuleCicsProxy");

		for (int i=0; i<operations.size(); i++) {
			Operation operation = (Operation) operations.get(i);
			String operationName = operation.getName();

			String inboundXsd = operation.getInboundXsd();
			if (inboundXsd == null || inboundXsd.equals("")) {
				/***************************************************************
                 * If WSDL binding contains a operation with no inbound message,
                 * WSDL2Java command of Axis fails. So, skipping such messages.
                 **************************************************************/
				continue;
			}

			// START - <wsdl:operation>
			writer.writeStartElement("wsdl", "operation", WSDL_NS);
			writer.writeAttribute("name", operationName);
			writer.writeAttribute("parameterOrder", "in0");

			if (inboundXsd != null && !inboundXsd.equals("")) {
				// START - <wsdl:input>
				writer.writeStartElement("wsdl", "input", WSDL_NS);
				writer.writeAttribute("message", "impl:" + operationName + "Request");
				writer.writeAttribute("name", operationName + "Request");
				writer.writeEndElement();
				// END - </wsdl:input>
			}

			String outboundXsd = operation.getOutboundXsd();
			if (outboundXsd != null && !outboundXsd.equals("")) {
				// START - <wsdl:output>
				writer.writeStartElement("wsdl", "output", WSDL_NS);
				writer.writeAttribute("message", "impl:" + operationName + "Response");
				writer.writeAttribute("name", operationName + "Response");
				writer.writeEndElement();
				// END - </wsdl:output>
			}

            String faultXsd = operation.getFaultXsd();
			if (faultXsd != null && !faultXsd.equals("")) {
				// START - <wsdl:fault>
				writer.writeStartElement("wsdl", "fault", WSDL_NS);
				writer.writeAttribute("message", "impl:" + operationName + "Fault");
				writer.writeAttribute("name", operationName + "Fault");
				writer.writeEndElement();
				// END - </wsdl:fault>
			}

			writer.writeEndElement();
			// END - </wsdl:operation>
		}

		writer.writeEndElement();
		// END - </wsdl:portType>
	}

	/**
     * Writes the binding for each operation in the WSDL document. Each
     * operation can have two messages, one input and second output.
     *
     * @param writer
     *            StAX object to write XML.
     * @param operations
     *            list of operation beans (Each bean contains info in interface
     *            file)
     * @param targetNs
     *            the target namespace of WSDL.
     * @throws XMLStreamException
     */
	private void writeBindings(XMLStreamWriter writer, String targetNs, List operations) throws XMLStreamException {
		// START - <wsdl:binding>
		writer.writeStartElement("wsdl", "binding", WSDL_NS);
		writer.writeAttribute("name", "MuleCicsUMOSoapBinding");
		writer.writeAttribute("type", "impl:MuleCicsProxy");

		// START - <soap:binding>
		writer.writeStartElement("soap", "binding", WSDLSOAP_NS);
		writer.writeAttribute("style", "document");
		writer.writeAttribute("transport", "http://schemas.xmlsoap.org/soap/http");
		writer.writeEndElement();
		// END - </soap:binding>

		for (int i=0; i<operations.size(); i++) {
			Operation operation = (Operation) operations.get(i);
			String operationName = operation.getName();

			String inboundXsd = operation.getInboundXsd();
			if (inboundXsd == null || inboundXsd.equals("")) {
				/***************************************************************
                 * If WSDL binding contains a operation with no inbound message,
                 * WSDL2Java command of Axis fails. So, skipping such messages.
                 **************************************************************/
				continue;
			}

			// START - <wsdl:operation>
			writer.writeStartElement("wsdl", "operation", WSDL_NS);
			writer.writeAttribute("name", operationName);

			// START - <soap:operation>
			writer.writeStartElement("soap", "operation", WSDLSOAP_NS);
			writer.writeAttribute("soapAction", "");
			writer.writeEndElement();
			// END - </soap:operation>

			if (inboundXsd != null && !inboundXsd.equals("")) {
				// START - <wsdl:input>
				writer.writeStartElement("wsdl", "input", WSDL_NS);
				writer.writeAttribute("name", operationName + "Request");

				// START - <soap:body>
				writer.writeStartElement("soap", "body", WSDLSOAP_NS);
				writer.writeAttribute("encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
				writer.writeAttribute("namespace", targetNs);
				writer.writeAttribute("use", "literal");
				writer.writeEndElement();
				// END - </soap:body>

				writer.writeEndElement();
				// END - </wsdl:input>
			}

			String outboundXsd = operation.getOutboundXsd();
			if (outboundXsd != null && !outboundXsd.equals("")) {
				// START - <wsdl:output>
				writer.writeStartElement("wsdl", "output", WSDL_NS);
				writer.writeAttribute("name", operationName + "Response");

				// START - <soap:body>
				writer.writeStartElement("soap", "body", WSDLSOAP_NS);
				writer.writeAttribute("encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
				writer.writeAttribute("namespace", targetNs);
				writer.writeAttribute("use", "literal");
				writer.writeEndElement();
				// END - </soap:body>

				writer.writeEndElement();
				// END - </wsdl:output>
			}

            String faultXsd = operation.getFaultXsd();
			if (faultXsd != null && !faultXsd.equals("")) {
				// START - <wsdl:fault>
				writer.writeStartElement("wsdl", "fault", WSDL_NS);
				writer.writeAttribute("name", operationName + "Fault");

				// START - <soap:body>
				writer.writeStartElement("soap", "fault", WSDLSOAP_NS);
				writer.writeAttribute("encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
				writer.writeAttribute("namespace", targetNs);
				writer.writeAttribute("use", "literal");
				writer.writeEndElement();
				// END - </soap:body>

				writer.writeEndElement();
				// END - </wsdl:fault>
			}

			writer.writeEndElement();
			// END - </wsdl:operation>
		}

		writer.writeEndElement();
		// END - </wsdl:binding>
	}

	/**
     * Writes the service element in the WSDL document.
     *
     * @param writer
     *            StAX object to write XML.
     * @throws XMLStreamException
     */
	private void writeService(XMLStreamWriter writer, String serviceLocation) throws XMLStreamException {
		// START - <wsdl:service>
		writer.writeStartElement("wsdl", "service", WSDL_NS);
		writer.writeAttribute("name", "MuleCicsUMO");

		// START - <wsdl:port>
		writer.writeStartElement("wsdl", "port", WSDL_NS);
		writer.writeAttribute("binding", "impl:MuleCicsUMOSoapBinding");
		writer.writeAttribute("name", "MuleCicsUMO");

		// START - <soap:address>
		writer.writeStartElement("soap", "address", WSDLSOAP_NS);
		writer.writeAttribute("location", serviceLocation);
		writer.writeEndElement();
		// END - </soap:address>

		writer.writeEndElement();
		// END - </wsdl:port>

		writer.writeEndElement();
		// END - </wsdl:service>
	}
}
