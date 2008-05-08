package org.mule.transport.cicsStreaming;

import org.mule.transport.AbstractMessageDispatcherFactory;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.transport.MessageDispatcher;

/**
 * Creates a CicsMessageDispatcher to invoke mainframe (Cics) programs.
 */
public class CicsMessageDispatcherFactory extends AbstractMessageDispatcherFactory {

    private java.util.Map dispatcherMap = new java.util.HashMap();

	public MessageDispatcher create(OutboundEndpoint endpoint) {

        MessageDispatcher dispatcher = (MessageDispatcher) dispatcherMap.get(endpoint);
		if (dispatcher == null) {
			dispatcher = new CicsMessageDispatcher(endpoint);
			dispatcherMap.put(endpoint, dispatcher);
		}

		return dispatcher;
	}
}
