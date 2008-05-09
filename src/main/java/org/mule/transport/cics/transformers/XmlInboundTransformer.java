package org.mule.transport.cics.transformers;

import java.io.ByteArrayInputStream;

import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.api.MuleMessage;
import org.mule.message.DefaultExceptionPayload;

import org.mule.transport.cics.util.XmlUtils;

/**
 * This class implements the inbound transformers for messages received by  
 * mule-cics. This removes the outer tags and returns XML inside <app-data>.
 */
public class XmlInboundTransformer extends AbstractMessageAwareTransformer {

	/*
     * This method is called when the mule-cics receives a message
     * through an inbound endpoint configured in mule-config.xml
     */
	public Object transform(MuleMessage message, String encoding) throws TransformerException {

        try {
            byte[] incoming = message.getPayloadAsBytes();
         
            String operationName = XmlUtils.getRootElementName(incoming);
    	    message.setStringProperty("operationName", operationName);
        
            ByteArrayInputStream is = new ByteArrayInputStream(incoming); 
		    return XmlUtils.getInternalXML(is, "app-data", "");
		} catch (Throwable e) {
			logger.error(e);
			message.setExceptionPayload(new DefaultExceptionPayload(e));
			return message;
		}
	}
}
