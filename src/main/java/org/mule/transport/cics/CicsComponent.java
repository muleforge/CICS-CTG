package org.mule.transport.cics; 

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.config.ConfigurationException;
import org.mule.api.lifecycle.Callable;
import org.mule.api.service.Service;
import org.mule.api.service.ServiceAware;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.message.DefaultExceptionPayload;
import org.mule.transport.cics.util.Constants;
import org.mule.transport.cics.esbInterface.Operation;
import org.mule.transport.cics.i18n.CicsMessages;

/** This component receives XML messages sent to mule-cics.
 */
public class CicsComponent implements Callable, ServiceAware {

  /** logger */
  private static Log logger = LogFactory.getLog(CicsComponent.class);

  private CicsService cicsService;

  public void setService(Service service) throws ConfigurationException {
      try { 
          this.cicsService = (CicsService) service;
      } catch (ClassCastException e) {          
          throw new ConfigurationException(new Exception(CicsMessages.errorInConfigurationFile().toString(), e));
      }
  }
  
  public Service getService() {
      return cicsService;
  }

  /*
   * This method is called when the CicsComponent receives a message through
   * an inbound endpoint configured in mule-config.xml
   */
  public Object onCall(MuleEventContext context) throws Exception {

     String interfaceFile = cicsService.getInterfaceFile();
     MuleMessage message = context.getMessage();
     message.setStringProperty("interfaceFile", interfaceFile);
     context.transformMessage();

     // Check if an error occured during inbound transformation.
     if (message.getExceptionPayload() != null) {
         return message;
     }

     boolean skipProcessing
         = message.getBooleanProperty(Constants.SKIP_FURTHER_PROCESSING, false);     
     if (skipProcessing) {
         context.setStopFurtherProcessing(true);
         return message;
     }

     String operationName = message.getStringProperty("operationName", "");
     Operation operation = cicsService.getEsbInterface().getOperation(operationName);
 
     if (operation == null) {
        String errMsg = CicsMessages.operationNotSupported(operationName, interfaceFile).toString();
        Exception e = new Exception(errMsg);
        message.setExceptionPayload(new DefaultExceptionPayload(e));
        return message;
     }

     message.setProperty("operation", operation);
     logger.info("operationName = " + operationName);
     return message;
  }
}
