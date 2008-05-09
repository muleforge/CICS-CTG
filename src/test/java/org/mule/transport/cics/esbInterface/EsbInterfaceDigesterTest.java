
package org.mule.transport.cics.esbInterface;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for EsbInterfaceDigester
 */
public class EsbInterfaceDigesterTest extends TestCase {
	/**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
	public EsbInterfaceDigesterTest(String testName) {
		super(testName);
	}

	/**
     * @return the suite of tests being tested
     */
	public static Test suite() {
		return new TestSuite(EsbInterfaceDigesterTest.class);
	}

	/**
     * Rigourous Test :-)
     */
	public void test1() throws Exception {
		EsbInterface esbInterface 
            = EsbInterfaceDigester.getInstance().parse("interface/CustomerInterface.xml");
		assertNotNull(esbInterface);

		Operation operation = esbInterface.getOperation("sample-command");
		assertNotNull(operation);
		String inboundXsd = operation.getInboundXsd();
		String outboundXsd = operation.getOutboundXsd();
		assertEquals("interface/sample_command.xsd", inboundXsd);
		assertEquals("interface/sample_response.xsd", outboundXsd);
		Property property = operation.getProperty();
		assertNotNull(property);
		assertEquals("SAMPLE", property.getTransactionID());
		assertEquals("SAMPLE", property.getApplProgramName());

		operation = esbInterface.getOperation("sample-null-command");
		assertNotNull(operation);
		inboundXsd = operation.getInboundXsd();
		outboundXsd = operation.getOutboundXsd();
		assertTrue((inboundXsd == null) || inboundXsd.equals(""));
		assertTrue((outboundXsd == null) || outboundXsd.equals(""));
		property = operation.getProperty();
		assertNotNull(property);
		assertEquals("SAMPLE", property.getTransactionID());
		assertEquals("SAMPLE", property.getApplProgramName());

		operation = esbInterface.getOperation("sample-request-only-command");
		assertNotNull(operation);
		inboundXsd = operation.getInboundXsd();
		outboundXsd = operation.getOutboundXsd();
		assertEquals("interface/sample_command.xsd", inboundXsd);
		assertTrue((outboundXsd == null) || outboundXsd.equals(""));
		property = operation.getProperty();
		assertNotNull(property);
		assertEquals("SAMPLE", property.getTransactionID());
		assertEquals("SAMPLE", property.getApplProgramName());

		operation = esbInterface.getOperation("sample-reply-only-command");
		assertNotNull(operation);
		inboundXsd = operation.getInboundXsd();
		outboundXsd = operation.getOutboundXsd();
		assertTrue((inboundXsd == null) || inboundXsd.equals(""));
		assertEquals("interface/sample_response.xsd", outboundXsd);
		property = operation.getProperty();
		assertNotNull(property);
		assertEquals("SAMPLE", property.getTransactionID());
		assertEquals("SAMPLE", property.getApplProgramName());

		operation = esbInterface.getOperation("sample-list-command");
		assertNotNull(operation);
		inboundXsd = operation.getInboundXsd();
		outboundXsd = operation.getOutboundXsd();
		assertEquals("interface/sample_list_command.xsd", inboundXsd);
		assertEquals("interface/sample_response.xsd", outboundXsd);
		property = operation.getProperty();
		assertNotNull(property);
		assertEquals("SAMPLE", property.getTransactionID());
		assertEquals("SAMPLE", property.getApplProgramName());

	}

}
