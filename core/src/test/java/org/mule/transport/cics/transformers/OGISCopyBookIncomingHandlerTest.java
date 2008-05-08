package org.mule.transport.cics.transformers;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.mule.transport.cics.util.Constants;

import org.mule.util.IOUtils;

/**
 * Unit test for simple OGISCopyBookIncomingHandler.
 */
public class OGISCopyBookIncomingHandlerTest extends TestCase {

	/**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
	public OGISCopyBookIncomingHandlerTest(String testName) {
		super(testName);
	}

	/**
     * @return the suite of tests being tested
     */
	public static Test suite() {
		return new TestSuite(OGISCopyBookIncomingHandlerTest.class);
	}

	/**
     * Rigourous Test :-)
     */
	public void test1() throws Exception {
		String msg = IOUtils.getResourceAsString("OGISCopyBookIncomingHandler-in.bytes", this.getClass());
        byte[] copyBookBytes = msg.getBytes();

		byte[] result = (byte[]) new OGISCopyBookIncomingHandler().transform(copyBookBytes, Constants.CICS_DEFAULT_ENCODING);

		String expected = IOUtils.getResourceAsString("OGISCopyBookIncomingHandler-out.bytes", this.getClass());
		assertEquals(expected, new String(result));
	}
}
