package org.mule.transport.cics;

import org.mule.api.lifecycle.InitialisationException;
import org.mule.model.seda.SedaService;
import org.mule.transport.cics.esbInterface.EsbInterface;
import org.mule.transport.cics.esbInterface.EsbInterfaceDigester;

public class CicsService extends SedaService {

	private String interfaceFile = "";

	/** Java object containing information parsed from interface file. */
	private EsbInterface esbInterface = null;

	public String getInterfaceFile() {
		return interfaceFile;
	}

	public void setInterfaceFile(String interfaceFile) {
		this.interfaceFile = interfaceFile;
	}

	public EsbInterface getEsbInterface() {
		return esbInterface;
	}

	protected synchronized void doInitialise() throws InitialisationException {
		super.doInitialise();
		try {
			logger.info("In doInitialise of CicsService");
			if (this.esbInterface == null) {
				this.esbInterface 
				= EsbInterfaceDigester.getInstance().parse(interfaceFile);
			}
		} catch (Exception e) {          			
			throw new InitialisationException(e, this);
		}
		// Verify the interface file. 
		// The operation name in the interface file should be same as the root
		// element of the inbound XSD for that operation.
		/*
	      try {
	          XsdReaderFactory xsdFactory = XsdReaderFactory.getInstance();
	          List operations = esbInterface.getOperations();
	          for (int i = 0; i < operations.size(); i++) {
	              Operation operation = (Operation) operations.get(i);
	              String inboundXsd = operation.getInboundXsd();
	              if (inboundXsd != null && !inboundXsd.equals("")) {
	                  XsdReader xsdReader = xsdFactory.getXsdReader(inboundXsd);
	                  String rootElementName = xsdReader.getRootXsdElement().getName();
	                  if (!rootElementName.equals(operation.getName())) {
	                    String msg = "ERROR: Operation name in interface file is " +
	                           "not same as root element in inbound XSD.\n" +
	                           "interfaceFile={" + interfaceFile + "}, " +
	                           "operationName={" + operation.getName() + "}, " +
	                           "inboundXsd={" + inboundXsd + "}, " +
	                           "rootElementName={" + rootElementName + "}";
	                    throw new Exception(msg);
	                  }
	              }
	          }
	      } catch (Exception e) {          
	          throw new ConfigurationException(e);
	      }*/
		logger.info("doInitialise of CicsService returned successfully.");
	}

}
