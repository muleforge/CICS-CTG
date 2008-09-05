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
			if (this.esbInterface == null) {
				this.esbInterface 
				= EsbInterfaceDigester.getInstance().parse(interfaceFile);
			}
		} catch (Exception e) {          			
			throw new InitialisationException(e, this);
		}
	}

}
