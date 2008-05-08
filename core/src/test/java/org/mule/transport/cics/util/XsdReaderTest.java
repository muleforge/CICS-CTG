
package org.mule.transport.cics.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for XsdReader, XsdElement and XsdReaderFactory
 */
public class XsdReaderTest extends TestCase {
	/**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
	public XsdReaderTest(String testName) {
		super(testName);
	}

	/**
     * @return the suite of tests being tested
     */
	public static Test suite() {
		return new TestSuite(XsdReaderTest.class);
	}

	/**
     * Rigourous Test :-)
     */
	public void test1() throws Exception {
		String headerXsd = "META-INF/header.xsd";
		XsdReader xsdReader = XsdReaderFactory.getInstance().getXsdReader(headerXsd);
		XsdElement rootXsdElement = xsdReader.getRootXsdElement();
		String namespace = xsdReader.getNamespaceOfDocument();
		assertEquals("http://ogis-ri.co.jp/LZAPLHDR-AREA", namespace);

		String name = rootXsdElement.getName();
		assertEquals("lzaplhdr-area", name);
	}
}
