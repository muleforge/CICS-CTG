package org.mule.transport.cics.esbInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is bean class for the root element of the <esb-interface>.
 */
public class EsbInterface {
	
	private List operationList = new ArrayList();
	

	/**
     * This method adds an instance of the Operation class.
     * 
     * @param operation an instance of the Operation class
     */
	public void addOperation(Operation operation) {
		operationList.add(operation);
	}

	public Operation getOperation(String operationName) {

		for (int i = 0; i < operationList.size(); i++) {
			Operation operation = (Operation) operationList.get(i);
			if (operationName.equals(operation.getName()))
				return operation;
		}

		return null;
	}

	public List getOperations() {
		return operationList;
	}
	
	public static String getNamespaceOfWsdl(String interfaceFile){
		return "http://" + getFilenameWithoutExtension(interfaceFile) + "/";
	}
	
    private static String getFilenameWithoutExtension(String filename) {

		int dotIndex = filename.lastIndexOf('.');
        int slashIndex = -1;

        if (!filename.endsWith(java.io.File.pathSeparator)) {
		    slashIndex = filename.lastIndexOf(java.io.File.pathSeparator);
        }

        if (slashIndex == -1 && !filename.endsWith("/")) {
   		    slashIndex = filename.lastIndexOf('/');
        }

        int startIndex = (slashIndex >=0) ? slashIndex + 1 : 0;
        int endIndex = (dotIndex >=0) ? dotIndex : filename.length();
      
        return filename.substring(startIndex, endIndex);
    }
}
