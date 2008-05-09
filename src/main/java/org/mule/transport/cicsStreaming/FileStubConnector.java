package org.mule.transport.cicsStreaming;

import org.mule.transport.AbstractConnector;

public class FileStubConnector extends AbstractConnector {

	/** {@inheritDoc} */
	public String getProtocol() {
		return "stubtest-streaming";
	}

	/** {@inheritDoc} */
	public void doConnect() {
	}

	/** {@inheritDoc} */
	public void doDisconnect() {
	}

	/** {@inheritDoc} */
	public void doDispose() {
	}

	/** {@inheritDoc} */
	public void doInitialise() {
	}

	/** {@inheritDoc} */
	public void doStart() {
	}

	/** {@inheritDoc} */
	public void doStop() {
	}
}
