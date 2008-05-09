package org.mule.transport.cicsStreaming;

import java.io.InputStream;

import org.mule.transport.AbstractMessageDispatcher;
import org.mule.api.MuleMessage;
import org.mule.api.MuleEvent;
import org.mule.message.DefaultExceptionPayload;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.util.IOUtils;

import org.mule.transport.cics.util.Constants;

/**
 * <code>FileStubMessageDispatcher</code> dispatches Mule events for stub
 * testing.
 */
public class FileStubMessageDispatcher extends AbstractMessageDispatcher {

	public FileStubMessageDispatcher(OutboundEndpoint endpoint) {
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

	/** {@inheritDoc} */
	protected MuleMessage doSend(MuleEvent event) throws Exception {
	    String filename = "";
		MuleMessage message = event.getMessage();
		try {
			if (message.getExceptionPayload() != null) return message;

			filename = (String) this.endpoint.getProperty("file");
            String[] parameters = getParametersInFilename(filename);
            for (int i=0; i<parameters.length; i++) {
              String paramValue = message.getStringProperty(parameters[i], "");
              filename = filename.replace("${" + parameters[i] + "}", paramValue);
            }

            event.transformMessage();
            logger.debug("Returning dummy response from file: " + filename);
            InputStream is = IOUtils.getResourceAsStream(filename, this.getClass());
            message.setPayload(is);

		} catch (Throwable e) {
            e.printStackTrace();
			logger.error(e);
			message.setExceptionPayload(new DefaultExceptionPayload(e));
		} 

        return message;
	}

    private String[] getParametersInFilename(String filename) {

      java.util.List parameters = new java.util.ArrayList();
      int paramStart = filename.indexOf("${");
      while (paramStart >= 0) {
        int paramEnd = filename.indexOf('}', paramStart);
        String paramName = filename.substring(paramStart + 2, paramEnd);
        parameters.add(paramName);
        paramStart = filename.indexOf("${", paramEnd);
      }

      return (String []) parameters.toArray(new String[0]);
    }

	/** {@inheritDoc} */
	protected void doDispose() {
	}
}
