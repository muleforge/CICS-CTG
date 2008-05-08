package org.mule.transport.cics.transformers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.custommonkey.xmlunit.XMLAssert;

import org.mule.util.IOUtils;
import org.mule.transport.cics.util.Constants;

/**
 * Unit test for simple App.
 */
public class CopyBookToXmlUtilTest extends TestCase {
    private static XMLOutputFactory outputFactory;
    static {
        outputFactory = XMLOutputFactory.newInstance();
        outputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", Boolean.TRUE);
    }
	/**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
	public CopyBookToXmlUtilTest(String testName) {
		super(testName);
	}

	/**
     * @return the suite of tests being tested
     */
	public static Test suite() {
		return new TestSuite(CopyBookToXmlUtilTest.class);
	}

	/**
     * Rigourous Test :-)
     */
	public void test1() throws Exception {
		String xsdFile = "META-INF/header.xsd";
		InputStream is = IOUtils.getResourceAsStream("header.bytes", this.getClass());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(bos, "UTF-8");

        String cicsEncoding = Constants.CICS_DEFAULT_ENCODING;
        //new CopyBookToXmlUtil().transform(is, cicsEncoding, xsdFile, writer);
        new CopyBookToXmlUtil().transform(is, "UTF-8", xsdFile, writer);

        writer.close();
        String xml = new String(bos.toByteArray(), "UTF-8");
		String expectedXml = IOUtils.getResourceAsString("header.xml", this.getClass());
		XMLAssert.assertXMLEqual(expectedXml, xml);
	}

	/**
     * Rigourous Test :-)
     */
	public void test2() throws Exception {
		String xsdFile = "interface/sample_response.xsd";
		InputStream is = IOUtils.getResourceAsStream("sample-response-without-header.bytes", this.getClass());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(bos, "UTF-8");
        String cicsEncoding = Constants.CICS_DEFAULT_ENCODING;
		//new CopyBookToXmlUtil().transform(is, cicsEncoding, xsdFile, writer);
        new CopyBookToXmlUtil().transform(is, "UTF-8", xsdFile, writer);
        writer.close();
        String xml = new String(bos.toByteArray(), "UTF-8");

		String expectedXml = IOUtils.getResourceAsString("sample-response-without-header.xml", this.getClass());
		XMLAssert.assertXMLEqual(expectedXml, xml);
	}
}
