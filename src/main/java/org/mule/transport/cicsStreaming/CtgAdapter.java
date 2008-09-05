/*
 * Copyright (C) 2008 OGIS-RI Co., Ltd. All rights reserved.
 *
 * This software is the proprietary information of OGIS-RI Co., Ltd.
 * Use is subject to license terms.
 */

package org.mule.transport.cicsStreaming;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.transport.cics.i18n.CicsMessages;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.LocalTransaction;
import javax.resource.cci.Record;
import com.ibm.connector2.cics.ECIInteractionSpec;
import com.ibm.connector2.cics.ECIManagedConnectionFactory;
import com.ibm.connector2.cics.ECIResourceAdapterRc;

/**
 * This is an adapter class to access IBM CICS Transaction gateway. 
 * This class uses javax.resource classes and javax.resource.cci classes
 * and interfaces provided by JCA, but this class also uses IBM CTG connector
 * proprietary APIs.
 */
public class CtgAdapter {

    /** logger */
    private static Log logger = LogFactory.getLog(CtgAdapter.class);

    /** CICS Transaction ID */ private String transactionID;
    /** URL CTG             */ private String url;
    /** port number of CTG  */ private int port;
    /** entry name          */ private String entry;
    /** CICS tpn name       */ private String tpnName;

    private ECIInteractionSpec ixnSpec;

    /** connection factory */
    private ConnectionFactory connectionFactory = null;

    /**
     * Public constructor.
     * 
     * @param url            URL CTG
     * @param port           port number of CTG 
     * @param entry          entry name 
     * @param tpnName        CICS tpn name
     * @param transactionID  CICS Transaction ID
     * @throws ResourceException
     */
    public CtgAdapter(String url, int port, String entry, String tpnName,
                      String transactionID) throws ResourceException {
        this.url = url;
        this.port = port;
        this.entry = entry;
        this.tpnName = tpnName;
        this.transactionID = transactionID;

        // creates an ECI interaction spec.
        this.ixnSpec = new ECIInteractionSpec();
        this.ixnSpec.setInteractionVerb(ECIInteractionSpec.SYNC_SEND_RECEIVE);
        this.ixnSpec.setFunctionName(tpnName);
        this.ixnSpec.setReplyLength(32000);

        // initializes a factory
        ECIManagedConnectionFactory mcf = new ECIManagedConnectionFactory();
        mcf.setConnectionURL(url);
        mcf.setPortNumber("" + port);
        mcf.setServerName(entry);
        mcf.setTPNName(transactionID);
        mcf.setTranName(transactionID);

        this.connectionFactory = (ConnectionFactory) mcf.createConnectionFactory();
    }

    public String toString() {
      return "transactionID=[" + this.transactionID + 
             "], url=["        + this.url + 
             "], port=["       + this.port + 
             "], entry=["      + this.entry + 
             "], tpnName=["    + this.tpnName + "]";
    }

    /**
     * This method makes a connection to CTG.
     * 
     * @return <code>Connection</code> mainframe connection 
     * @Exception Exception if an error occurs
     */
    public Connection connect() throws Exception {
        logger.debug("###### connect(start) ######");
        Connection con = null;
        try {
            con = this.connectionFactory.getConnection();
        } catch (ResourceException e) {
            logger.error(e);
            throw new Exception(CicsMessages.cicsConnectionError().toString(), e);
        }

        logger.debug("###### connect(end) ######");
        return con;
    }

    /**
     * This <code>invoke</code> method invokes a CICS application 
     * via CTG in a streaming manner. 
     * @param <code>Connection</code> mainframe connection 
     * @param inRec a request record
     * @param outRec a response record
     */
    public void invoke(Connection con, Record inRec, Record outRec) throws Exception {
        logger.debug("###### invoke(start) ######");
        con.getLocalTransaction().begin();

        // invokes a transaction on mainframe and gets a reply message.
        execute(con, inRec, outRec);
        logger.debug("###### invoke( end ) ######");
    }

    /**
     * This method actually calls JCA and CTG APIs.
     * 
     * @param <code>Connection</code> mainframe connection 
     * @param inRec  a request record
     * @param outRec a response record
     * @return       a reply record from mainframe
     * @throws Exception if an error occured
     */
    private void execute(Connection con, Record inRec, Record outRec) throws Exception {
        try {
            // execute a mainframe transaction
            logger.debug("CICS-ACCESS start...");
            con.createInteraction().execute(this.ixnSpec, inRec, outRec);
            logger.debug("CICS-ACCESS end....");

        } catch (com.ibm.connector2.cics.CICSTxnAbendException re) {

            String msg = null; // CICS abend
            String errorCode = re.getErrorCode();
            if (errorCode == null) {
                msg = "Check CICS log.";
            } else if ("AEIO".equals(errorCode)) {
                msg = errorCode + " CICS Program not found.";
            } else {
                msg = errorCode + " Check CICS log.";
            }
            throw new Exception(CicsMessages.cicsAbendError(msg).toString(), re);

        } catch (javax.resource.spi.SecurityException re) {
            // if a security violation occurs
            String msg = null;
            String errorCode = re.getErrorCode();
            if (errorCode == null) {
                msg = re.getMessage();
            } else {
                int errCode = parseInt(errorCode);
                if (errCode == ECIResourceAdapterRc.ECI_ERR_SECURITY_ERROR) {
                    msg = errorCode + " Check credentials for userid.";
                } else {
                    msg = errorCode + " " + re.getMessage();
                }
            }
            throw new Exception(CicsMessages.cicsSecurityError(msg).toString() , re);

        } catch (javax.resource.spi.CommException re) {
            // if a communication error occurs
            String msg = null;
            String errorCode = re.getErrorCode();
            if (errorCode == null) {
                msg = re.getMessage();
            } else {
                int errCode = parseInt(errorCode);
                if (errCode == ECIResourceAdapterRc.ECI_ERR_NO_CICS) {
                    msg = errorCode + " CICS server is stopped.";
                } else {
                    msg = errorCode + " " + re.getMessage();
                }
            }

            if (re.getCause() != null) {
                if (re.getCause() instanceof java.io.IOException) {
                    msg = " IO error: check gateway daemon.";
                } else {
                    msg = " Linked exception: " + re.getCause();
                }
            }
            throw new Exception(CicsMessages.cicsCommunicationError(msg).toString(), re);

        } catch (javax.resource.spi.ResourceAllocationException re) {
            // A communication error occured between CTG and CICS.
            // If CICS on mainframe is not running, this error occurs.
            String msg = null;
            String errorCode = re.getErrorCode();
            if (errorCode == null) {
                msg = re.getMessage();
            } else {
                int errCode = parseInt(errorCode);
                if (errCode == ECIResourceAdapterRc.ECI_ERR_RESOURCE_SHORTAGE) {
                    msg = errorCode + " Check network from CTG to CICS";
                } else {
                    msg = errorCode + " " + re.getMessage();
                }
            }
            throw new Exception(CicsMessages.cicsCTGError(msg).toString(), re);
        } catch (javax.resource.spi.EISSystemException re) {
            String msg = null;
            String errorCode = re.getErrorCode();
            if (errorCode != null) 
                msg = errorCode + ": ";
            msg += re.getMessage();
            throw new Exception(CicsMessages.cicsEISError(msg).toString(), re);

        } catch (Exception re) {
            // if other exception occurs
            throw new Exception("CICS_ERR", re);
        }
    }

    /**
     * Parses string to integer (returns 0, if s is not a number)
     */
    private static int parseInt(String s) {

        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param <code>Connection</code> mainframe connection 
     * Commit the CICS transaction.
     * */
    public void commit(Connection con) throws Exception {

        if (con == null) return;
        LocalTransaction tran = con.getLocalTransaction();
        if (tran != null) {
            tran.commit(); // commits a transaction
            if (logger.isInfoEnabled()) {
              logger.info("CICS-Transaction [" + 
                          this.transactionID + "] commit.");
            }
        }
    }

    /**
     * @param <code>Connection</code> mainframe connection 
     * Rollback the CICS transaction.
     * */
    public void rollback(Connection con) throws Exception {

        if (con == null) return;
        LocalTransaction tran = con.getLocalTransaction();
        if (tran != null) {
            tran.rollback();
            logger.info("CICS-Transaction [" + this.transactionID + "] has been rolledback.");
        }
   }
}
