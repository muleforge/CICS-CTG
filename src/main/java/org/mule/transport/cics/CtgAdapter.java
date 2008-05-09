/*
 * Copyright (C) 2008 OGIS-RI Co., Ltd. All rights reserved.
 *
 * This software is the proprietary information of OGIS-RI Co., Ltd.
 * Use is subject to license terms.
 */

package org.mule.transport.cics;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.LocalTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

        // creates an ECI request
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
     * @return a <code>Connection</code> value
     * @Exception Exception if an error occurs
     */
    public Connection connect() throws Exception {
        logger.debug("###### connect(start) ######");
        Connection con = null;
        try {
            con = this.connectionFactory.getConnection();
        } catch (ResourceException e) {
            logger.error(e);
            throw new Exception("CICS_ERR: Error connecting to CICS", e);
        }

        logger.debug("###### connect(end) ######");
        return con;
    }

    /**
     * This <code>invoke</code> method invokes a CICS application via CTG.
     * 
     * @param <code>Connection</code> mainframe connection 
     * @param requestBytes a request message.
     * @return returns a reply message
     */
    public byte[] invoke(Connection con, byte[] requestBytes) throws Exception {
        logger.debug("###### invoke(start) ######");

        con.getLocalTransaction().begin();

        // creates an outbound message.
        CICSRecord inRec = new CICSRecord(requestBytes);

        // invokes a transaction on mainframe and gets a reply message.
        CICSRecord outRec = execute(con, inRec);

        // gets a result as a byte array.
        byte[] resultBytes = outRec.getBytes();
        
        logger.debug("###### invoke( end ) ######");
        return resultBytes;
    }

    /**
     * This method actually calls JCA and CTG APIs.
     * 
     * @param <code>Connection</code> mainframe connection 
     * @param inRec a request record
     * @return a reply record from mainframe
     * @throws Exception
     *             if an error occured
     */
    private CICSRecord execute(Connection con, CICSRecord inRec) throws Exception {
        try {
            CICSRecord outRec = new CICSRecord(); // reply record

            // execute a mainframe transaction
            logger.debug("CICS-ACCESS start...inRec:[" + inRec + "]");
            con.createInteraction().execute(this.ixnSpec, inRec, outRec);
            logger.debug("CICS-ACCESS end....outRec:[" + outRec + "]");

            return outRec;

        } catch (com.ibm.connector2.cics.CICSTxnAbendException re) {

            String msg = "Error - CICS Abend: "; // CICS abend
            if (re.getErrorCode() != null) {
                msg += ": " + re.getErrorCode();
            }
            if (re.getErrorCode().equals("AEIO")) {
                msg += " Msg: CICS Program not found";
            } else {
                msg += " Msg: Check CICS log";
            }
            throw new Exception("CICS_ABEND_ERR: " + msg, re);

        } catch (javax.resource.spi.SecurityException re) {

            // if a security violation occurs
            String msg = "Error - Security";
            if (re.getErrorCode() != null)
                msg += ": " + re.getErrorCode();
            int errCode = Integer.parseInt(re.getErrorCode());
            if (errCode == ECIResourceAdapterRc.ECI_ERR_SECURITY_ERROR) {
                msg += " Msg: Check credentials for userid ";
            } else {
                msg += " Msg: " + re.getMessage();
            }

            throw new Exception("CICS_SECURITY_ERR:" + msg, re);

        } catch (javax.resource.spi.CommException re) {

            // if a communication error occurs
            String msg = "Error - CICS Communication";
            if (re.getErrorCode() != null)
                msg += ": " + re.getErrorCode();
            if (re.getCause() != null) {
                if (re.getCause() instanceof java.io.IOException) {
                    msg += " Msg: IO error: check gateway daemon";
                } else {
                    msg += "Linked exception = " + re.getCause();
                }
            }

            if (re.getErrorCode() != null) {
                int errCode = Integer.parseInt(re.getErrorCode());
                if (errCode == ECIResourceAdapterRc.ECI_ERR_NO_CICS) {
                    msg += " Msg: CICS server is stopped";
                } else {
                    msg += " Msg: " + re.getMessage();
                }
            }
            throw new Exception("CICS_COMMUNICATION_ERR:" + msg, re);

        } catch (javax.resource.spi.ResourceAllocationException re) {

            // A communication error occured between CTG and CICS.
            // If CICS on mainframe is not running, this error occurs.
            String msg = "Error - Resource allocation";
            if (re.getErrorCode() != null)
                msg += ": " + re.getErrorCode();
            int errCode = Integer.parseInt(re.getErrorCode());
            if (errCode == ECIResourceAdapterRc.ECI_ERR_RESOURCE_SHORTAGE) {
                msg += " Msg: Check network from CTG to CICS";
            } else {
                msg += " Msg: " + re.getMessage();
            }
            throw new Exception("CICS_CTG_ERR:" + msg, re);

        } catch (javax.resource.spi.EISSystemException re) {

            String msg = "Error - CICS System error";
            if (re.getErrorCode() != null)
                msg += ": " + re.getErrorCode();
            msg += "Msg: " + re.getMessage();
            throw new Exception("CICS_EIS_ERR", re);
        } catch (Exception re) {
            // if other exception occurs
            throw new Exception("CICS_ERR", re);
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
