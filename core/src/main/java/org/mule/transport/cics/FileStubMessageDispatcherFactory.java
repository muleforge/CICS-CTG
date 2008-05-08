package org.mule.transport.cics;

import org.mule.transport.AbstractMessageDispatcherFactory;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.transport.MessageDispatcher;

/**
 * Creates a FileStubMessageDispatcher to send dummy file as response.
 */
public class FileStubMessageDispatcherFactory extends AbstractMessageDispatcherFactory {

	public MessageDispatcher create(OutboundEndpoint endpoint) {

	    return new FileStubMessageDispatcher(endpoint);

	}
}
