package org.mule.transport.cics.esbInterface;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.mule.util.IOUtils;

/**
 * This class parses interface file and creates bean instance.
 */
public class EsbInterfaceDigester {

    private static Log logger = LogFactory.getLog(EsbInterfaceDigester.class);
    private Map esbInterfaceMap = new HashMap();
    private static EsbInterfaceDigester esbDigester;

    private EsbInterfaceDigester() {}

    public static EsbInterfaceDigester getInstance() {
        if (esbDigester == null)
            esbDigester = new EsbInterfaceDigester();

        return esbDigester;
    }

    /**
     * This method parses the interface file
     * @param filename the filename to be parsed.
     * @return EsbInterface returns a bean instance.
     * @exception EsbConfigException if an error occurs.
     */
    public synchronized EsbInterface parse(String filename) throws Exception {

        EsbInterface esbInterface = 
            (EsbInterface) this.esbInterfaceMap.get(filename);
        if (esbInterface == null) {
          logger.info("Parsing interfaceFile: " + filename);
          InputStream is = IOUtils.getResourceAsStream(filename, this.getClass());
          if (is == null)
              throw new IOException("Error loading interface file : '" + filename + "'");
        
          esbInterface = validate(is);
          logger.info("Successfully parsed interfaceFile: " + filename);
          this.esbInterfaceMap.put(filename, esbInterface);
        }

        return esbInterface;
    }
    
    private EsbInterface validate(InputStream xmlIs) throws Exception {

        EsbInterface esbInterface = new EsbInterface();
        EsbInterfaceDocument document = EsbInterfaceDocument.Factory.parse(xmlIs);
        OperationDocument.Operation opArray[] = document.getEsbInterface().getOperationArray();
        if (opArray == null) return esbInterface;

        for (int i=0; i<opArray.length; i++) {
            Operation esbOperation = new Operation();
            esbOperation.setName(opArray[i].getName());
            
            if (opArray[i].getInbound() != null) {
                esbOperation.setInboundXsd(opArray[i].getInbound().getXsd());
            }
            
            if (opArray[i].getOutbound() != null) {
                esbOperation.setOutboundXsd(opArray[i].getOutbound().getXsd());
            }
            
            if (opArray[i].getFault() != null) {
                esbOperation.setFaultXsd(opArray[i].getFault().getXsd());
            }
            
            if (opArray[i].getProperty() != null) {
                Property property = new Property();
                property.setApplProgramName(opArray[i].getProperty().getApplProgramName());
                property.setTransactionID(opArray[i].getProperty().getTransactionID());
                esbOperation.setProperty(property);
            }else{
            	throw new RuntimeException("Error while parsing interface file: Expecting 'property' tag in 'operation' tag for operation name '"+opArray[i].getName()+"'");
            }

            esbInterface.addOperation(esbOperation);
        }

        return esbInterface;
    }
}
