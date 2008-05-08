/*
 * Copyright (C) 2008 OGIS-RI Co., Ltd. All rights reserved.
 *
 * This software is the proprietary information of OGIS-RI Co., Ltd.
 * Use is subject to license terms.
 */

package org.mule.transport.cicsStreaming;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.resource.cci.Record;
import javax.resource.cci.Streamable;

import org.mule.transport.cics.util.Constants;
import org.mule.api.transport.OutputHandler;

/**
 * This class implements javax.resource.cci.Record and
 * javax.resoure.cci.Streamable. The class holds a inbound
 * message when Mule accesses to CTG.
 */
class CicsRecordInbound implements java.io.Serializable, Record, Streamable {

  /** Serial Version UID  */
  private static final long serialVersionUID = 1L;

  /** This variable is a communication stream for inbound message. */
  private OutputHandler inboundHandler;

  /** gets a record name */
  private String name = CicsRecordInbound.class.getName();

  /** a record descriptor */
  private String description = "Framework record";

  /**
   * A default constructor
   */
  public CicsRecordInbound() {
  }

  /**
   * A constructor 
   *
   * @param inboundHandler
   */
  public CicsRecordInbound(OutputHandler inboundHandler) {
    this.inboundHandler = inboundHandler;
  }

  /**
   * This method write inbound message to an output stream.
   *
   * @param ostream a stream for write
   *
   */
  public void write(OutputStream ostream) throws IOException {

    this.inboundHandler.write(null, ostream);
  }

  /**
   * This method reads outbound message from input stream.
   *
   * @param istream sets message
   * @throws IOException
   */
  public void read(InputStream istream) throws IOException {

      throw new UnsupportedOperationException();
  }

  /**
   * This method creates a clone object of this class.
   *
   * @return returns a clone object.
   */
  public Object clone() {
    return new CloneNotSupportedException();
  }

  /**
   * This method returns a record name.
   *
   * @return returns a record name
   */
  public String getRecordName() {
    return this.name;
  }

  /**
     * This method returns a record descriptor.
     *
     * @return returns a descriptor
     */
  public String getRecordShortDescription() {
    return this.description;
  }


  /**
   * This method sets a record name.
   *
   * @param name a record name
   */
  public void setRecordName(final String name) {
    this.name = name;
  }

  /**
   * This method sets a description.
   *
   * @param description
   */
  public void setRecordShortDescription(String description) {
    this.description = description;
  }
}
