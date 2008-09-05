
package org.mule.transport.cics.util;

import java.io.ByteArrayInputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for XmlUtils
 */
public class XmlUtilsTest extends TestCase {
	/**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
	public XmlUtilsTest(String testName) {
		super(testName);
	}

	/**
     * @return the suite of tests being tested
     */
	public static Test suite() {
		return new TestSuite(XmlUtilsTest.class);
	}

	/**
     * Rigourous Test :-)
     */
	public void testGetRootElementName() throws Exception {
		String xml = "<?xml version='1.0' ?><test></test>";
       
		String operationName = XmlUtils.getRootElementName(xml.getBytes());

		assertEquals("test", operationName);
	}

	public void testGetInternalXML() throws Exception {

		String xml = "<?xml version='1.0' ?><test><internal></internal></test>";
		String tag = "test";
		String namespace = "";
        ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes()); 
		byte[] internalXML = XmlUtils.getInternalXML(bis, tag, namespace);

		String root = XmlUtils.getRootElementName(internalXML);

		assertEquals("internal", root);
	}
}
