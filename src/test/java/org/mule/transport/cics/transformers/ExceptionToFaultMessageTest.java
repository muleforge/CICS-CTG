
package org.mule.transport.cics.transformers;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for ExceptionToFaultMessage transformer.
 */
public class ExceptionToFaultMessageTest extends TestCase {
	/**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
	public ExceptionToFaultMessageTest(String testName) {
		super(testName);
	}

	/**
     * @return the suite of tests being tested
     */
	public static Test suite() {
		return new TestSuite(ExceptionToFaultMessageTest.class);
	}

	/**
     * Rigourous Test :-)
     */
	public void test1() throws Exception {
		Exception e = new Exception();
		byte[] faultBytes = (byte[]) new ExceptionToFaultMessage().transform(e);

        String faultMsg = new String(faultBytes, "UTF-8");
		assertTrue(faultMsg.startsWith("<fault-data"));
	}

}
