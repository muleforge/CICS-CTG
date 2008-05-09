package org.mule.transport.cics.util;

/**
 * This class contains constants
 */
public class Constants {

	/** default encoding for XML */
	public static final String XML_DEFAULT_ENCODING = "UTF-8";

	/** default encoding for CICS copybook message */
	public static final String CICS_DEFAULT_ENCODING = "CP943C";

    /** name of message property used to stop further processing on the message
     * (E.g when client request for WSDL
     */
    public static final String SKIP_FURTHER_PROCESSING = "skip-further-processing";

    /** 
     * If there is an error in response transformer, we want to rollback
     * the transaction. So, we explicitly call the response transformer
     * and check if there is any error.
     * This property is used to prevent Mule from calling the response
     * transformer the second time.
     */
    public static final String SKIP_RESPONSE_TRANSFORMER = "skip-cics-response-transformer";
    
}
