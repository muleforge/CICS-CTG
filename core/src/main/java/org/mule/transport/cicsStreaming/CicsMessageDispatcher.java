package org.mule.transport.cicsStreaming;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import javax.resource.cci.Connection;

import org.mule.DefaultMuleMessage;
import org.mule.message.DefaultExceptionPayload;
import org.mule.transport.AbstractMessageDispatcher;
import org.mule.api.transport.OutputHandler;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.OutboundEndpoint;

import org.mule.transport.cics.esbInterface.Operation;
import org.mule.transport.cics.esbInterface.Property;

/**
 * <code>CicsMessageDispatcher</code> dispatches Mule events to mainframe
 * using cics/ctg transport.
 */
public class CicsMessageDispatcher extends AbstractMessageDispatcher {

    /** Map to store CtgAdapter instances (operationName is used as key). */
    private Map ctgAdapterMap = new HashMap();

    public CicsMessageDispatcher(OutboundEndpoint endpoint) {
        super(endpoint);
    }

    /** {@inheritDoc} */
    protected void doConnect() throws Exception {
    }

    /** {@inheritDoc} */
    protected void doDisconnect() throws Exception {
    }

    /** {@inheritDoc} */
    protected void doDispatch(MuleEvent event) throws Exception {
    }

    /** {@inheritDoc} */
    protected MuleMessage doReceive(long timeout) throws Exception {
        return null;
    }

    protected MuleMessage doSend(MuleEvent event) throws Exception {

        final MuleMessage message = event.getMessage();
        if (message.getExceptionPayload() != null) return message;
        try {

            final CtgAdapter ctgAdapter = getCtgAdapter(message);
            if (logger.isDebugEnabled()) {
                logger.debug ("Invoking CTGAdapter: " + ctgAdapter.toString());
            }

            event.transformMessage();

            // Inbound transformers (XmlToCopyBook, etc) return an OutputHandler object.
            // The write method on OutputHandler object is used to write input data to CICS in a streaming manner.
            OutputHandler inboundHandler
                = (OutputHandler) message.getPayload(OutputHandler.class);

            // create the input, output record for CICS transaction.
            CicsRecordInbound  inRec  = new CicsRecordInbound(inboundHandler);
            CicsRecordOutbound outRec = new CicsRecordOutbound();

            // When the CICS transaction is executed, 
            //  - inRec.read()   gets called, with an OutputStream to which request is written.
            //  - outRec.write() gets called, with an InputStream from which response can be read.
            final Connection connection = ctgAdapter.connect();
            ctgAdapter.invoke(connection, inRec, outRec);

            // Get the CICS response stream from the outRec. 
            final InputStream responseStream = outRec.getOutboundStream();

            // Set filter stream, to  commit/rollback CICS transaction, after reading
            // of CICS response is complete.
            FilterInputStream delegateStream = new FilterInputStream(responseStream) {
                public void close() throws IOException {

                  boolean rollback = (message.getExceptionPayload() == null);
                  if (!rollback) {
                      try {
                        ctgAdapter.commit(connection);
                      } catch (Exception e) {
                        rollback = true;
                      }
                  }

                  if (rollback) {
                      try {
                        ctgAdapter.rollback(connection);
                      } catch (Exception e) { /* ignore */ }
                  }

                  try {
                      responseStream.close();
                      connection.close();
                  } catch (Exception e) { /* ignore */ }
                }
            };

            // The response transformers will read the data from CICS response stream.
            message.setPayload(delegateStream);

        } catch (Throwable e) {
            logger.error(e);
            message.setExceptionPayload(new DefaultExceptionPayload(e));
        } 

        return message;
    }

    /**
     * This method gets the instance of CtgAdapter for this message
     * CtgAdapters are cached in a Map, with operationName as key.
     */
    private CtgAdapter getCtgAdapter(MuleMessage message) throws Exception {

        Operation operation = (Operation) message.getProperty("operation", null);
        if (operation == null) {
            throw new Exception("CicsMessageDispatcher - NULL_OPERATION");
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
