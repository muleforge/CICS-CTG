package org.mule.transport.cics.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.mule.transport.cics.i18n.CicsMessages;
import org.mule.util.IOUtils;

/**
 * This class is a factory class to read a XML schema.
 */
public class XsdReaderFactory {

	private static XsdReaderFactory factory;
	private Map xsdReaderMap = new HashMap();

	public static XsdReaderFactory getInstance() {

		if (factory == null)
			factory = new XsdReaderFactory();
		return factory;
	}

	/**
     * This method reads a XML Schema and returns XsdReader instance. XsdReader
     * instance is used for getting elements of XML according to XML schema
     * definition.
     * 
     * @param xsdFile
     *            the name of XML schema file.
     * @return XsdReader returns an instance of XsdReader class.
     */
	public XsdReader getXsdReader(String xsdFile) throws Exception {

      try {
		XsdReader reader = (XsdReader) this.xsdReaderMap.get(xsdFile);
		if (reader == null) {
			reader = new XsdReader();
			InputStream is = IOUtils.getResourceAsStream(xsdFile, this.getClass());
            if (is == null)
				throw new IOException(CicsMessages.errorReadingXsdFile(xsdFile).toString());

			reader.process(is);
			this.xsdReaderMap.put(xsdFile, reader);
		}

		return reader;

      } catch (Exception e) {
    	  throw new IOException(CicsMessages.errorLoadingXsdFile(xsdFile, e.getMessage()).toString());
      }
	}
}
