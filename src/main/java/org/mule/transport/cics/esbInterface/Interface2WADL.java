package org.mule.transport.cics.esbInterface;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.mule.transport.cics.util.XsdElement;
import org.mule.transport.cics.util.XsdReader;
import org.mule.transport.cics.util.XsdReaderFactory;
import org.mule.util.IOUtils;

public class Interface2WADL {
	
	private XsdReaderFactory xsdFactory = XsdReaderFactory.getInstance();
	private HashMap nameSpaceMap = new HashMap(); 
	
	private static final String XSD_LONG = "xsd:long";
	private static final String XSD_STRING = "xsd:string";

	public static final int XSD_TYPE_INBOUND = 1;
	public static final int XSD_TYPE_OUTBOUND = 2;
	public static final int XSD_TYPE_ERROR = 3;

	/**
     * Entry point to execute from command-line console. Usage: java
     * org.mule.transport.cics.Interface2WSDL <interfaceFile>
     */
	public static void main(String[] args) throws Exception {

		if (args.length < 2) {
			System.out.println("Usage: java org.mule.transport.cics.Interface2WADL <interfaceFile> <wadlServiceLocation>");
			System.out.println("<interfaceFile>   - path to the interface file");
			System.out.println("<wadlServiceLocation> - the location of the webservice e.g http://xyz.com/seriola/abc");
			return;
		}

		String wadl = new Interface2WADL().getWADL(args[0], args[1]);
		System.out.println(wadl);
		return;
	}

	/**
     * Writes WSDL file for a given interface file.
     * 
     * @param interfaceFile
     *            interface file
     * @throws Exception
     */
	public String getWADL(String interfaceFile, String serviceLocation) throws Exception {
		EsbInterface esbInterface 
            = EsbInterfaceDigester.getInstance().parse(interfaceFile);
		return getWADL(esbInterface, serviceLocation);
	}
	
	public String getXSD(String interfaceFile, String operationName, int type) throws Exception {
		EsbInterface esbInterface 
            = EsbInterfaceDigester.getInstance().parse(interfaceFile);
		
		Operation operation = esbInterface.getOperation(operationName);
		
		String xsd = null;
		switch(type) {
			case XSD_TYPE_INBOUND:
				xsd = operation.getInboundXsd();
				break;
			case XSD_TYPE_OUTBOUND:
				xsd = operation.getOutboundXsd();
				break;
			default:
				xsd = operation.getFaultXsd();
		}
		if (xsd != null && xsd.length() > 0) {
			return IOUtils.getResourceAsString(xsd, getClass());
		}
		return "";
	}

	/**
     * Writes WSDL file for a given interface file.
     * 
     * @param esbInterface
     *            Java bean containing information in interface file.
     * @param serviceLocation
     *            The location of the wadlservice e.g http://xyz.com/seriola/abc            
     * @throws Exception
     */
	public String getWADL(EsbInterface esbInterface, String serviceLocation) throws Exception {
		List operations = esbInterface.getOperations();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		factory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
		XMLStreamWriter writer = factory.createXMLStreamWriter(bos);
		
		writer.writeStartDocument();
		//START: <application>
		writer.writeStartElement("application");
		
		writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		writer.writeAttribute("xsi:schemaLocation", "http://research.sun.com/wadl/2006/10 wadl.xsd");
		writer.writeAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		writeOperationResponseNamespaces(writer, operations);
		writer.writeAttribute("xmlns", "http://research.sun.com/wadl/2006/10");
		
		//START: <grammars>
		writer.writeStartElement("grammars");
		
		includeXsds(writer, serviceLocation, operations);
		
		writer.writeEndElement();
		//END: </grammars>
		
		//START: <resources>
		writer.writeStartElement("resources");
		writer.writeAttribute("base", serviceLocation);
		
		Iterator itr = operations.iterator();
		while(itr.hasNext()) {
			Operation operation = (Operation)itr.next();
			writeResourceTag(writer, operation);
		}
		writer.writeEndElement();
		//END: </resources>
		
		writer.writeEndElement();
		//END: </application>
		
		writer.writeEndDocument();
		writer.close();

		String wsdl = new String(bos.toByteArray());
		return wsdl;
	}
	
	private void includeXsds(XMLStreamWriter writer, String serviceLocation, List operations) throws Exception {
		HashSet set = new HashSet();
		Iterator itr = operations.iterator();
		while(itr.hasNext()) {
			Operation operation = (Operation)itr.next();
			String inboundXsd = operation.getInboundXsd();
			if ( set.add(inboundXsd)) {
				if (inboundXsd != null && inboundXsd.length() > 0) {
					String xsdPath = serviceLocation + "/" + operation.getName() + "?xsd=inbound";
					writer.writeStartElement("include");
					writer.writeAttribute("href", xsdPath);
					writer.writeEndElement();
				}
			}
			
			String outboundXsd = operation.getOutboundXsd();
			if ( set.add(outboundXsd)) {
				if (outboundXsd != null && outboundXsd.length() > 0) {
					String xsdPath = serviceLocation + "/" + operation.getName() + "?xsd=outbound";
					writer.writeStartElement("include");
					writer.writeAttribute("href", xsdPath);
					writer.writeEndElement();
				}
			}
			
			String faultXsd = operation.getFaultXsd();
			if ( set.add(faultXsd)) {
				if (faultXsd != null && faultXsd.length() > 0) {
					String xsdPath = serviceLocation + "/" + operation.getName() +"?xsd=error";
					writer.writeStartElement("include");
					writer.writeAttribute("href", xsdPath);
					writer.writeEndElement();
				}
			}
		}
		
	}
	
	private void writeOperationResponseNamespaces(XMLStreamWriter writer, List operations) throws Exception {
		Iterator itr = operations.iterator();
		int i = 1;
		while(itr.hasNext()) {
			Operation operation = (Operation)itr.next();
			String inboundXsd = operation.getInboundXsd();
			if (inboundXsd != null && !inboundXsd.equals("")) {
				XsdReader xsdReader = xsdFactory.getXsdReader(inboundXsd);
				String nameSpaceURI = xsdReader.getNamespaceOfDocument();
				String nameSpace = "ns" + (i++);
				if (nameSpaceMap.get(nameSpaceURI) == null) {
					writer.writeAttribute("xmlns:" + nameSpace, nameSpaceURI);
					nameSpaceMap.put(nameSpaceURI, nameSpace);
				}
			}
			
			String outboundXsd = operation.getOutboundXsd();
			if (outboundXsd != null && !outboundXsd.equals("")) {
				XsdReader xsdReader = xsdFactory.getXsdReader(outboundXsd);
				String nameSpaceURI = xsdReader.getNamespaceOfDocument();
				String nameSpace = "ns" + (i++);
				if (nameSpaceMap.get(nameSpaceURI) == null) {
					writer.writeAttribute("xmlns:" + nameSpace, nameSpaceURI);
					nameSpaceMap.put(nameSpaceURI, nameSpace);
				}
			}
			
			String errorXsd = operation.getFaultXsd();
			if (errorXsd != null && !errorXsd.equals("")) {
				XsdReader xsdReader = xsdFactory.getXsdReader(errorXsd);
				String nameSpaceURI = xsdReader.getNamespaceOfDocument();
				String nameSpace = "ns" + (i++);
				if (nameSpaceMap.get(nameSpaceURI) == null) {
					writer.writeAttribute("xmlns:" + nameSpace, nameSpaceURI);
					nameSpaceMap.put(nameSpaceURI, nameSpace);
				}
			}
			
		}
	}
	
	
	private void writeResourceTag(XMLStreamWriter writer, Operation operation) throws Exception {
		//START: <resource>
		writer.writeStartElement("resource");
		writer.writeAttribute("path", operation.getName());
		//START: <method> for GET
		writer.writeStartElement("method");
		writer.writeAttribute("name", "GET");
		writer.writeAttribute("id", operation.getName());
		
		String inboundXsd = operation.getInboundXsd();
		//START: <request>
		writeRequestTagForGet(writer, inboundXsd);
		//END: </request>
		
		String outboundXsd = operation.getOutboundXsd();
		String faultXsd = operation.getFaultXsd();
		//START: <response>
		writeResponseTag(writer, outboundXsd, faultXsd);
		//END: </response>
		
		writer.writeEndElement();
		//END: </method>
		
		//START: <method> for POST
		writer.writeStartElement("method");
		writer.writeAttribute("name", "POST");
		writer.writeAttribute("id", operation.getName());
		
		//START: <request>
		writeRequestTagForPost(writer, inboundXsd);
		//END: </request>
		
		//START: <response>
		writeResponseTag(writer, outboundXsd, faultXsd);
		//END: </response>
		
		writer.writeEndElement();
		//END: </method>
		
		writer.writeEndElement();
		//END: </resource>
	}
	
	private void writeRequestTagForGet(XMLStreamWriter writer, String inboundXsd) throws Exception {
		writer.writeStartElement("request");
		if (inboundXsd != null && !inboundXsd.equals("")) {
			XsdReader xsdReader = xsdFactory.getXsdReader(inboundXsd);
			XsdElement rootXsdElement = xsdReader.getRootXsdElement();
			writeParamTag(writer, rootXsdElement);
		}
		writer.writeEndElement();
	}
	
	private void writeRequestTagForPost(XMLStreamWriter writer, String inboundXsd) throws Exception {
			writer.writeStartElement("request");
			writer.writeStartElement("representation");
			writer.writeAttribute("mediaType", "application/xml");
			if (inboundXsd != null && !inboundXsd.equals("")) {
				XsdReader xsdReader = xsdFactory.getXsdReader(inboundXsd);
				XsdElement rootXsdElement = xsdReader.getRootXsdElement();
				String nameSpaceURI = xsdReader.getNamespaceOfDocument();
				String rootElementName = nameSpaceMap.get(nameSpaceURI) + ":" + rootXsdElement.getName();
				writer.writeAttribute("element", rootElementName);
			}
			writer.writeEndElement();
			writer.writeEndElement();
	}
	
	private void writeParamTag(XMLStreamWriter writer, XsdElement xsdElement) throws Exception {
		Iterator childIter = xsdElement.getChildIterator();
		while (childIter.hasNext()) {
			XsdElement child = (XsdElement) childIter.next();
			if (child.getLength() > 0) {
				writer.writeStartElement("param");
				writer.writeAttribute("name" , child.getName());
				writer.writeAttribute("style", "query");
				if ( child.getType().equals("9")) {
					writer.writeAttribute("type", XSD_LONG);
				} else {
					writer.writeAttribute("type", XSD_STRING);
				}
				writer.writeEndElement();
			}
			writeParamTag(writer, child);
		}
	}
	
	private void writeResponseTag(XMLStreamWriter writer, String outboundXsd, String faultXsd) throws Exception {
		writer.writeStartElement("response");
		writer.writeStartElement("representation");
		writer.writeAttribute("mediaType", "application/xml");
		if (outboundXsd != null && !outboundXsd.equals("")) {
			XsdReader xsdReader = xsdFactory.getXsdReader(outboundXsd);
			XsdElement rootXsdElement = xsdReader.getRootXsdElement();
			String nameSpaceURI = xsdReader.getNamespaceOfDocument();
			String rootElementName = nameSpaceMap.get(nameSpaceURI) + ":" + rootXsdElement.getName();
			writer.writeAttribute("element", rootElementName);
		}
		writer.writeEndElement(); //<representation> END
		
		if (faultXsd != null && !faultXsd.equals("")) {
			writer.writeStartElement("fault");
			writer.writeAttribute("mediaType", "application/xml");
			XsdReader xsdReader = xsdFactory.getXsdReader(faultXsd);
			XsdElement rootXsdElement = xsdReader.getRootXsdElement();
			String nameSpaceURI = xsdReader.getNamespaceOfDocument();
			String rootElementName = nameSpaceMap.get(nameSpaceURI) + ":" + rootXsdElement.getName();
			writer.writeAttribute("element", rootElementName);
			writer.writeEndElement();//<fault> END
		}
		
		writer.writeEndElement();
	}
}
