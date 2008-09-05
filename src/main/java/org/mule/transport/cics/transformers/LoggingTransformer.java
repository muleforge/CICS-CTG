package org.mule.transport.cics.transformers;

import java.io.FileOutputStream;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.MuleMessage;

/**
 * Logs the message into a file.
 * The filename can contain parameters e.g logfile-${operationName}.xml
 * This transformers will then get the "operationName" property
 * from the Mule message, and then prepare the filename dynamically
 * before writing to it.
 */
public class LoggingTransformer extends AbstractMessageAwareTransformer {

  private String file;
  private long id = 0;

  public void setFile(String file) {
    this.file = file;
  }

  public String getFile() {
    return this.file;
  }

  public Object transform(MuleMessage message, String outputEncoding) {
	FileOutputStream fos = null;
    try {
      if (message.getExceptionPayload() != null)
        return message;

      byte[] bytes = message.getPayloadAsBytes();
      String filename = this.file;

      String[] parameters = getParametersInFilename(filename);
      for (int i=0; i<parameters.length; i++) {
          String paramValue = message.getStringProperty(parameters[i], "");
          filename = filename.replace("${" + parameters[i] + "}", paramValue);
      }

      synchronized(this) { id++; }
      filename = filename + "-" + id;

      fos = new FileOutputStream(filename);
      fos.write(bytes);

    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
    	if(fos != null ){
    		try{
    			fos.close();
    		}catch (Exception e) {/*Do nothing*/}
    	}
        return message;
    }
  }

  private String[] getParametersInFilename(String filename) {

    java.util.List parameters = new java.util.ArrayList();
    int paramStart = filename.indexOf("${");
    while (paramStart >= 0) {
        int paramEnd = filename.indexOf('}', paramStart);
        String paramName = filename.substring(paramStart + 2, paramEnd);
        parameters.add(paramName);
        paramStart = filename.indexOf("${", paramEnd);
    }

    return (String []) parameters.toArray(new String[0]);
  }
}
