package org.mule.transport.cics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.resource.cci.Connection;
import org.mule.message.DefaultExceptionPayload;
import org.mule.transport.AbstractMessageDispatcher;
import org.mule.transport.cics.esbInterface.Operation;
import org.mule.transport.cics.esbInterface.Property;
import org.mule.transport.cics.i18n.CicsMessages;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.ImmutableEndpoint;
import org.mule.api.endpoint.OutboundEndpoint;

import org.mule.transport.cics.transformers.LoggingTransformer;
import org.mule.transport.cics.util.Constants;

/**
 * <code>CicsMessageDispatcher</code> dispatches Mule events to mainframe
 * using cics/ctg transport.
 */
public class CicsMessageDispatcher extends AbstractMessageDispatcher {

    /** Map to store CtgAdapter instances (operationName is used as key). */
    private Map ctgAdapterMap = new HashMap();

    /**
     * Creates a new <code>CicsMessageDispatcher</code> instance.
     * @param endpoint an <code>OutboundEndpoint</code> value
     */
    public CicsMessageDispatcher(OutboundEndpoint endpoint) {
        super(endpoint);
    }

    /**
     * {@inheritDoc}
     */
    protected void doConnect() throws Exception {
    }

    /**
     * {@inheritDoc}
     */
    protected void doDisconnect() throws Exception {
    }

    /** {@inheritDoc} */
    protected void doDispatch(MuleEvent event) throws Exception {
    }

    /** {@inheritDoc} */
    protected MuleMessage doReceive(long timeout) throws Exception {
        return null;
    }

    /** {@inheritDoc} */
    protected MuleMessage doSend(MuleEvent event) throws Exception {

        MuleMessage message = event.getMessage();
        if (message.getExceptionPayload() != null) return message;

        CtgAdapter ctgAdapter = null;
        Connection connection = null;
        try {
            event.transformMessage();
            byte[] request = message.getPayloadAsBytes();

            ctgAdapter = getCtgAdapter(message);
            if (logger.isDebugEnabled()) {
                logger.debug ("Invoking CTGAdapter: " + ctgAdapter.toString());
            }

            connection = ctgAdapter.connect();
            byte[] reply = ctgAdapter.invoke(connection, request);

            message.setPayload(reply);
            List transformers = getTransformerList(endpoint);
            message.applyTransformers(transformers);
            ctgAdapter.commit(connection);

        } catch (Throwable e) {
            try {
                if (ctgAdapter != null) {
                    ctgAdapter.rollback(connection);
                }
            } catch (Exception ex) {
                logger.error(ex);
            }
            logger.error(e);
            message.setExceptionPayload(new DefaultExceptionPayload(e));
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) { /* ignore */ }
            message.setBooleanProperty(Constants.SKIP_RESPONSE_TRANSFORMER, true);
        }

        return message;
    }
    
    private List getTransformerList(ImmutableEndpoint endpoint){
		List transformers = new ArrayList();
		for(int i=0;i<this.endpoint.getResponseTransformers().size();i++){
			Object transformer = this.endpoint.getResponseTransformers().get(i);
			if (!(transformer instanceof LoggingTransformer)) {
				transformers.add(transformer);
			}
		}
		return transformers;
	}

    /**
     * This method gets the instance of CtgAdapter for this message
     * CtgAdapters are cached in a Map, with operationName as key.
     */
    private CtgAdapter getCtgAdapter(MuleMessage message) throws Exception {

        Operation operation = (Operation) message.getProperty("operation", null);
        if (operation == null) {
              CicsMessages messages = new CicsMessages();
              throw new Exception(messages.nullOperation().toString());
        }

        String operationName = operation.getName();
        CtgAdapter ctgAdapter = (CtgAdapter) ctgAdapterMap.get(operationName);

        if (ctgAdapter == null) {
            String transactionId = "";
            Property property = operation.getProperty();
            if (property != null) {
                transactionId = property.getTransactionID();
            }
            
            String url = "tcp://" + this.endpoint.getEndpointURI().getHost();
            int port = this.endpoint.getEndpointURI().getPort();
            String entry = (String) this.endpoint.getProperty("entry");
            String tpnName = (String) this.endpoint.getProperty("tpnName");
            
           ctgAdapter = new CtgAdapter(url, port, entry, tpnName, transactionId);
           ctgAdapterMap.put(operationName, ctgAdapter);
        }

        return ctgAdapter;
    }

    /** {@inheritDoc} */
    protected void doDispose() {
        ctgAdapterMap.clear();
    }
}
