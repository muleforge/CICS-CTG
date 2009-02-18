
package org.mule.transport.cics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for CicsRecord
 */
public class CicsRecordTest extends TestCase {
	/**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
	public CicsRecordTest(String testName) {
		super(testName);
	}

	/**
     * @return the suite of tests being tested
     */
	public static Test suite() {
		return new TestSuite(CicsRecordTest.class);
	}

	/**
     * Rigourous Test :-)
     */
	public void test1() throws Exception {
		CicsRecord record = new CicsRecord();
		byte[] inBytes = new byte[10];

		for (int i = 0; i < inBytes.length; i++) {
			inBytes[i] = (byte) ' ';
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(inBytes);
		record.read(bis);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		record.write(bos);

		byte[] outBytes = bos.toByteArray();
		assertTrue(java.util.Arrays.equals(inBytes, outBytes));
	}

	/**
     * Rigourous Test :-)
     */
	public void test2() throws Exception {
		byte[] inBytes = new byte[10];

		for (int i = 0; i < inBytes.length; i++) {
			inBytes[i] = (byte) ' ';
		}

		CicsRecord record = new CicsRecord(inBytes);

		byte[] outBytes = record.getBytes();
		assertEquals(inBytes, outBytes);
	}
}
