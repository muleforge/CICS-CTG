package org.mule.transport.cics.esbInterface;

/**
 * This is a bean class for the <operation> element of interface file
 */
public class Operation {

	private String inboundXsd;
	private String outboundXsd;
	private String faultXsd;
	private Property property;
	private String name;

	/**
     * Sets the inbound xsd.
     * @param xsd a value
     */
	public void setInboundXsd(String xsd) {
		this.inboundXsd = xsd;
	}

	/**
     * Returns the inbound xsd
     * @return a Inbound xsd
     */
	public String getInboundXsd() {
		return this.inboundXsd;
	}

	/**
     * Sets the outbound xsd
     * @param xsd value
     */
	public void setOutboundXsd(String xsd) {
		this.outboundXsd = xsd;
	}

	/**
     * Returns the fault xsd.
     * @return a Fault xsd
     */
	public String getFaultXsd() {
	    return this.faultXsd;
	}
	
	/**
     * Sets the fault xsd.
     * @param xsd value
     */
	public void setFaultXsd(String xsd) {
		this.faultXsd = xsd;
	}

	/**
     * getter method
     * 
     * @return returns value
     */
	public String getOutboundXsd() {
		return this.outboundXsd;
	}

	/**
     * setter method
     * 
     * @param property value
     */
	public void setProperty(Property property) {
		this.property = property;
	}

	/**
     * a getter method
     * 
     * @return returns value
     */
	public Property getProperty() {
		return this.property;
	}

	/**
     * a setter method
     * 
     * @param name value
     */
	public void setName(String name) {
		this.name = name;
	}

	/**
     * a getter method
     * 
     * @return returns value
     */
	public String getName() {
		return this.name;
	}
}
