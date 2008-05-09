
package org.mule.transport.cics.esbInterface;

/**
 * This is a bean class for the <property> element of interface file
 */
public class Property {

	private String transactionID;

	private String applProgramName;

	private String menuID;

	/**
     * a setter method
     * 
     * @param transactionID
     */
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	/**
     * a getter method
     * 
     * @return returns a value
     */
	public String getTransactionID() {
		return this.transactionID;
	}

	/**
     * a setter method
     * 
     * @param applProgramName set a value
     */
	public void setApplProgramName(String applProgramName) {
		this.applProgramName = applProgramName;
	}

	/**
     * a getter method
     * 
     * @return a <code>String</code> value
     */
	public String getApplProgramName() {
		return this.applProgramName;
	}

	/**
     * a getter method
     * 
     * @return returns a value
     */
	public String getMenuID() {
		return this.menuID;
	}

	/**
     * a setter method
     * 
     * @param argMenuID set a value
     */
	public void setMenuID(String argMenuID) {
		this.menuID = argMenuID;
	}
}
