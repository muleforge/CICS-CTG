/*
 * Copyright (C) 2008 OGIS-RI Co., Ltd. All rights reserved.
 *
 * This software is the proprietary information of OGIS-RI Co., Ltd.
 * Use is subject to license terms.
 */

package org.mule.transport.cicsStreaming;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.resource.cci.Record;
import javax.resource.cci.Streamable;

/**
 * This class implements javax.resource.cci.Record and
 * javax.resoure.cci.Streamable. The class holds the outbound 
 * message when Mule accesses to CTG.
 */
class CicsRecordOutbound implements java.io.Serializable, Record, Streamable {

  /** Serial Version UID  */
  private static final long serialVersionUID = 1L;

  /** This variable is a communication stream for outbound message. */
  private InputStream outboundStream;

  /** gets a record name */
  private String name = CicsRecordOutbound.class.getName();

  /** a record descriptor */
  private String description = "Framework record";

  /**
   * A default constructor
   */
  public CicsRecordOutbound() {
  }

  public InputStream getOutboundStream() {
      return this.outboundStream;
  }

  /**
   * This method write inbound message to an output stream.
   *
   * @param ostream a stream for write
   *
   */
  public void write(OutputStream ostream) throws IOException {
      throw new UnsupportedOperationException();
  }

  /**
   * This method reads outbound message from input stream.
   *
   * @param istream sets message
   * @throws IOException
   */
  public void read(InputStream istream) throws IOException {
      this.outboundStream = istream;
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
