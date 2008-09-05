
package org.mule.transport.cics.transformers;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mule.transport.cics.util.Constants;
import org.mule.util.IOUtils;

/**
 * Unit test for simple App.
 */
public class XmlToCopyBookTest extends TestCase {
	/**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
	public XmlToCopyBookTest(String testName) {
		super(testName);
	}

	/**
     * @return the suite of tests being tested
     */
	public static Test suite() {
		return new TestSuite(XmlToCopyBookTest.class);
	}

	/**
     * Rigourous Test :-)
     */
	public void test1() throws Exception {
		String xml = IOUtils.getResourceAsString("XmlToCopyBook-in.xml", this.getClass());
        byte[] xmlBytes = xml.getBytes(Constants.XML_DEFAULT_ENCODING);
		String xsdFile = "interface/sample_command.xsd";
		String copyBook = new XmlToCopyBook().transform(xmlBytes, xsdFile);

		String expected = IOUtils.getResourceAsString("XmlToCopyBook-out.bytes", this.getClass());
		//assertEquals(expected, copyBook);
	}

}
