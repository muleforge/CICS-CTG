package org.mule.transport.cics.transformers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.api.MuleMessage;
import org.mule.DefaultMuleMessage;
import org.mule.message.DefaultExceptionPayload;

import org.mule.transport.cics.util.Constants;

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
    try {
      if (message.getExceptionPayload() != null)
        return message;

      boolean skipProcessing = message.getBooleanProperty(Constants.SKIP_RESPONSE_TRANSFORMER, false);
      if (skipProcessing) return message;

      byte[] bytes = message.getPayloadAsBytes();
      String filename = this.file;

      String[] parameters = getParametersInFilename(filename);
      for (int i=0; i<parameters.length; i++) {
          String paramValue = message.getStringProperty(parameters[i], "");
          filename = filename.replace("${" + parameters[i] + "}", paramValue);
      }

      synchronized(this) { id++; }
      filename = filename + "-" + id;

      FileOutputStream fos = new FileOutputStream(filename);
      fos.write(bytes);

    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
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
