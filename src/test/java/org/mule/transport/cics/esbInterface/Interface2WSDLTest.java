package org.mule.transport.cics.esbInterface;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mule.util.IOUtils;

/**
 * Unit test for simple App.
 */
public class Interface2WSDLTest extends TestCase {
	/**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
	public Interface2WSDLTest(String testName) {
		super(testName);
	}

	/**
     * @return the suite of tests being tested
     */
	public static Test suite() {
		return new TestSuite(Interface2WSDLTest.class);
	}

	/**
     * Rigourous Test :-)
     */
	public void test1() throws Exception {
		String interfaceFile = "interface/CustomerInterface.xml";
		String serviceLocation = "http://localhost:8888/seriola/CustomerInterface";
		String wsdl = new Interface2WSDL().getWSDL(interfaceFile, serviceLocation);
		String expected = IOUtils.getResourceAsString("CustomerInterface.wsdl", this.getClass());
	 //	assertEquals(expected, wsdl);
	}
}
