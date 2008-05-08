package org.mule.transport.cics.transformers;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mule.util.IOUtils;

/**
 * Unit test for XmlToSoap transformer.
 */
public class XmlToSoapTest extends TestCase {
	/**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
	public XmlToSoapTest(String testName) {
		super(testName);
	}

	/**
     * @return the suite of tests being tested
     */
	public static Test suite() {
		return new TestSuite(XmlToSoapTest.class);
	}

	/**
     * Rigourous Test :-)
     */
	public void test1() throws Exception {
		String xml = IOUtils.getResourceAsString("XmlToSoap-in.xml", this.getClass());
		byte[] xmlBytes = xml.getBytes("UTF-8");
		boolean isFaultMessage = false;

        byte[] soapMsg = (byte[]) new XmlToSoap().transform(xmlBytes, isFaultMessage);
        
		String expectedSoapMsg = IOUtils.getResourceAsString("XmlToSoap-out.xml", this.getClass());
		assertEquals(expectedSoapMsg, new String(soapMsg, "UTF-8"));
	}

}
