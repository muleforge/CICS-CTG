package org.mule.transport.cics;

import org.mule.transport.AbstractMessageDispatcherFactory;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.transport.MessageDispatcher;

/**
 * Creates a CicsMessageDispatcher to invoke mainframe (Cics) programs.
 */
public class CicsMessageDispatcherFactory extends AbstractMessageDispatcherFactory {

    private java.util.Map dispatcherMap = new java.util.HashMap();

    /**
     * <code>create</code> instanciates object.
     *
     * @param endpoint an <code>OutboundEndpoint</code>
     * @return a <code>MessageDispatcher</code>
     */
    public MessageDispatcher create(OutboundEndpoint endpoint) {

        MessageDispatcher dispatcher = (MessageDispatcher) dispatcherMap.get(endpoint);
        if (dispatcher == null) {
            dispatcher = new CicsMessageDispatcher(endpoint);
            dispatcherMap.put(endpoint, dispatcher);
        }
        return dispatcher;
  }
}
