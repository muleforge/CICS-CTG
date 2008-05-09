package org.mule.transport.cics.util;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaField;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlObject;

/**
 * This class is for reading XML schema.
 */
public class XsdReader {

	private Map xsdElementMap = new HashMap();

	private SchemaTypeSystem sts;

	/** It holds the root element of xml schema.*/
	private XsdElement rootXsdElement;

	/**
     * This method returns the root element.
     */
	public XsdElement getRootXsdElement() {
		return rootXsdElement;
	}

	/**
     * This method returns the matched element by name.
     * 
     * @param name
     *            element name of xml schema
     * @return XsdElement returns a element (XsdElement)
     */
	public XsdElement getXsdElement(String name) {
		// remove namespace
		int colonIndex = name.indexOf(":");
		if (colonIndex >= 0 && colonIndex + 1 < name.length()) {
			name = name.substring(colonIndex + 1);
		}
		return (XsdElement) xsdElementMap.get(name);
	}

	/**
     * This method returns a namespace of xml schema.
     * 
     * @return returns a namespace
     */
	public String getNamespaceOfDocument() {

		SchemaType[] docTypes = this.sts.documentTypes();
		if (docTypes != null && docTypes.length > 0) {
			QName name = docTypes[0].getDocumentElementName();
			if (name != null) {
				return name.getNamespaceURI();
			}
		}

		return "";
	}

	/**
     * This method reads XML schema.
     * 
     * @param xsdInputStream an input stream of XML schema
     *            
     */
	protected void process(InputStream xsdInputStream) throws Exception {

		// compiles xml schema
		XmlObject[] schemas =
		{ XmlObject.Factory.parse(xsdInputStream) };
		this.sts = XmlBeans.compileXsd(schemas, XmlBeans.getBuiltinTypeSystem(), null);

		List allTypes = Arrays.asList(sts.documentTypes());
		allTypes.addAll(Arrays.asList(sts.globalTypes()));

		for (int i = 0; i < allTypes.size(); i++) {
			SchemaType sType = (SchemaType) allTypes.get(i);
			processSchemaType(sType, null);
		}
	}

	/**
     * This method processes schema types of xml schema recursively.
     * 
     * @param sType a schema type
     * @param parentElement a parent element
     *            
     */
	private void processSchemaType(SchemaType sType, XsdElement parentElement) {

		int maxOccurs = getMaxOccurs(sType);
		if (maxOccurs > 1) {
			processArraySchemaType(sType, parentElement);
			return;
		}

		String name = getNameOfSchemaType(sType);
		XsdElement newElement = addXsdElement(name, sType, parentElement);
		SchemaType[] anonTypes = sType.getAnonymousTypes();
		for (int i = 0; i < anonTypes.length; i++) {
			// processes schema types recursively
			processSchemaType(anonTypes[i], newElement);
		}
	}

	/**
     * This method add an XsdElement
     * 
     * @param name a name of element
     * @param sType a schema type
     * @param parentElement a parent element which the new element is added
     * @return returns the added element
     */
	private XsdElement addXsdElement(String name, SchemaType sType, XsdElement parentElement) {
		if (name.equals(""))
			return null;

		String type = getDefaultValueOfAttribute(sType, "type");
		String length = getDefaultValueOfAttribute(sType, "length");

		XsdElement newElement = new XsdElement(name, type, length);
		xsdElementMap.put(name, newElement);

		if (parentElement != null) {
			parentElement.addChild(newElement); // Adding the new element to the parent element.
		} else {
			this.rootXsdElement = newElement; // The new element becomes a root element.
		}
		return newElement;
	}

	/**
     * This method processes array type.
     * 
     * @param sType a schma type
     * @parentElement a parent element
     */
	private void processArraySchemaType(SchemaType sType, XsdElement parentElement) {

		int arraySize = getMaxOccurs(sType);// obtains an occurence
		String name = getNameOfSchemaType(sType); // obtains the name of schema type

		XsdElement newElement = addXsdElement(name, sType, parentElement);
		newElement.setOccurs(arraySize);

		SchemaType[] anonTypes = sType.getAnonymousTypes();
		for (int i = 0; i < anonTypes.length; i++) {
			processSchemaType(anonTypes[i], newElement);
		}
	}

	/**
     * This method obtains the element name of the schema type.
     * 
     * @param sType a schema type
     * @return returns the element name
     */
	private String getNameOfSchemaType(SchemaType sType) {

		SchemaField schemaField = sType.getContainerField();
		if (schemaField != null) {
			QName name = schemaField.getName();
			if (name != null)
				return name.getLocalPart();
		}
		return "";
	}

	/**
     * This method obtains the integer value of maxOccurs.
     * 
     * @param sType a schema type
     * @return returns the value of maxOccurs(if it is 1, the schema type is not array.)
     */
	private int getMaxOccurs(SchemaType sType) {

		SchemaField schemaField = sType.getContainerField();
		if (schemaField != null) {
			BigInteger maxOccurs = schemaField.getMaxOccurs();
			if (maxOccurs != null)
				return maxOccurs.intValue();
		}
		return 1;
	}

	/**
	 * 
     * This method returns the default name of attribute.
     * 
     * @param sType a schema type
     * @param sAttributeName the name of the attribute
     * @return returns the default value of the attribute
     */
	private String getDefaultValueOfAttribute(SchemaType sType, String sAttributeName) {

		// obtains a list of attribute
		SchemaProperty[] attrs = sType.getAttributeProperties();
		for (int i = 0; i < attrs.length; i++) {
			QName attrName = attrs[i].getName();
			// returns the matched default name.
			if (attrName != null && attrName.getLocalPart().equals(sAttributeName)) {
				return attrs[i].getDefaultText();
			}
		}

		// else return space
		return "";
	}

	/**
     * This method is for debugging.
     */
	public String toString() {

		return rootXsdElement.toString("");
	}
}
