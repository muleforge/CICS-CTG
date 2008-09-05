package org.mule.transport.cicsStreaming;

import org.mule.transport.AbstractConnector;

/**
 * <code>CicsConnector</code> provides a way of invoking CICS mainframe programs.
 * This endpoint recognises the following properties - <p/>
 * <ul>
 * <li>entry - The entry name of CICS program.</li>
 * <li>tpnName - The tpnName of the CICS program.</li>
 * <li>maxSize - The maximum size of the request message (excluding header) to CICS program.</li>
 * <li>ipAddress - The ip address of the CICS host machine.</li>
 * </ul>
 */
public class CicsConnector extends AbstractConnector {

	/** {@inheritDoc} */
	public String getProtocol() {
		return "cics-streaming";
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
