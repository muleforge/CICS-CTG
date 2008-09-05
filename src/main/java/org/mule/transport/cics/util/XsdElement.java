package org.mule.transport.cics.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mule.transport.cics.i18n.CicsMessages;

/**
 * This class contains the information for an element in an Xsd file.<BR>
 */
public class XsdElement {

	private String name;

	private String type;

	private int length;

	private long occurs;

	private List childElements;

	/**
     * This is a constructer method and only used by XsdReader class.
     * 
     * @param name
     *            the name of element
     * @param type
     *            the name of type
     * @param len
     *            element length in byte
     */
	XsdElement(String name, String type, String len) {
		this.name = name;
		this.type = type;
		this.length = 0;

		// get length
		if (len != null && !len.equals("")) {
			try {
				this.length = Integer.parseInt(len);
			} catch (NumberFormatException e) {
				throw new NumberFormatException(CicsMessages.invalidLengthOfXsdElement(len,name).toString());
			}
		}
		this.occurs = 1;
		this.childElements = new ArrayList();
	}

	/**
     * This method retrieves the name of the element
     * 
     * @return <code>String</code> the name of element
     */
	public String getName() {
		return this.name;
	}

	/**
     * This method retrieves the type of the element
     * 
     * @return <code>String</code>the tpye of element
     */
	public String getType() {
		return this.type;
	}

	/**
     * This method retrieves the length of the element
     * 
     * @return <code>int</code> the lenght of the element
     */
	public int getLength() {
		return this.length;
	}

	/**
     * This method retrieve the size of the array(occurence)
     * 
     * @return <code>long</code> the size of the array(occurence)
     */
	public long getOccurs() {
		return this.occurs;
	}

	/**
     * This method sets the size of the array. This method is only used by XsdReader class.
     */
	void setOccurs(long occurs) {
		this.occurs = occurs;
	}

	/**
     * This method adds the child element. This method is only used by XsdReader class.
     * 
     * @param childElement
     *            <code>XsdElement</code> the child element
     */
	public void addChild(XsdElement childElement) {
		this.childElements.add(childElement);
	}

	/**
     * This method retrieves an iterator for getting child elements.
     * 
     * @return <code>Iterator</code> an iterator for child elements
     */
	public Iterator getChildIterator() {
		return this.childElements.iterator();
	}

	/**
     * This method is for debugging
     * 
     * @param padding
     *            <code>String</code> padding 
     * @return <code>String</code> returns a string
     */
	public String toString(String padding) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(padding + name);
		buffer.append(" : PIC " + type);
		buffer.append("(" + length + ")");

		if (occurs > 1)
			buffer.append(" OCCURS " + occurs);
		buffer.append("\n");

		for (int i = 0; i < childElements.size(); i++) {
			XsdElement childElement = (XsdElement) childElements.get(i);
			buffer.append(childElement.toString(padding + "  "));
		}
		return buffer.toString();
	}
}
