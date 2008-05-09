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

/** This component receives XML messages sent to mule-cics.
 * 
 * During initialization, this component parses the interface file configured
 * in mule-config.xml.
 * 
 * For each incoming message, this method detects the operation name, sets the
 * operation as message property and forwards for further processing as per the
 * configuration in mule-config.xml.
 */
public class CicsComponent implements Callable, ServiceAware {

  /** logger */
  private static Log logger = LogFactory.getLog(CicsComponent.class);

  private CicsService cicsService;

  public void setService(Service service) throws ConfigurationException {

      try { 
          this.cicsService = (CicsService) service;
      } catch (ClassCastException e) {          
          String errMsg = "Error in Mule configuration file." +
              " The <component> class CicsComponent" + 
              " can only be used inside <mule-cics:service>."; 
          throw new ConfigurationException(new Exception(errMsg, e));
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

     boolean skipProcessing
         = message.getBooleanProperty(Constants.SKIP_FURTHER_PROCESSING, false);     
     if (skipProcessing) {
         context.setStopFurtherProcessing(true);
         return message;
     }

     String operationName = message.getStringProperty("operationName", "");
     Operation operation = cicsService.getEsbInterface().getOperation(operationName);
 
     if (operation == null) {
        String errMsg = "Operation Not Supported. The operation '" + operationName + 
                        "' is not declared in the file '" + interfaceFile + "'";
        Exception e = new Exception(errMsg);
        message.setExceptionPayload(new DefaultExceptionPayload(e));
        return message;
     }

     message.setProperty("operation", operation);
     logger.info("operationName = " + operationName);
     return message;
  }
}
