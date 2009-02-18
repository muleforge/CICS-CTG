/*
 * Copyright (C) 2008 OGIS-RI Co., Ltd. All rights reserved.
 *
 * This software is the proprietary information of OGIS-RI Co., Ltd.
 * Use is subject to license terms.
 */

package org.mule.transport.cics;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.resource.cci.Record;
import javax.resource.cci.Streamable;

import org.mule.transport.cics.util.Constants;

/**
 * This class implements javax.resource.cci.Record and
 * javax.resoure.cci.Streamable. The class holds a inbound
 * and outbound message when the Mule accesses to
 * CTG(IBM CICS Transaction gateway).
 */
class CicsRecord implements java.io.Serializable, Record, Streamable {

  /** Serial Version UID  */
  private static final long serialVersionUID = 1L;

  /** This variable is a communication buffer for inbound and outbound. */
  private byte[] bytes = null;

  /** gets a record name */
  private String name = CicsRecord.class.getName();

  /** a record descriptor */
  private String description = "Framework record";

  /**
   * A default constructor
   */
  public CicsRecord() {
  }

  /**
   * A constructor with a message as bytearray.
   *
   * @param byteArray
   */
  public CicsRecord(final byte[] byteArray) {
    this.bytes = byteArray;
  }

  /**
   * A constructor with a mesage size.
   *
   * @param size
   */
  public CicsRecord(int size) {
    this.bytes = new byte[size];
  }

  /**
   * This method returns an byte array of the message.
   *
   * @return byte[]
   */
  public byte[] getBytes() {
    return this.bytes;
  }

  /**
   * This method creates a clone object.
   *
   * @return returns a clone object.
   */
  public Object clone() {
    byte[] byteArray = new byte[this.bytes.length];
    System.arraycopy(this.bytes, 0, byteArray, 0, byteArray.length);
    return new CicsRecord(byteArray);
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
   * This method reads a inbound or outbound message.
   *
   * @param istream sets message
   * @throws IOException
   */
  public void read(InputStream istream) throws IOException {
    int size = istream.available();
    if (this.bytes == null || this.bytes.length != size) {
      this.bytes = new byte[size];
    }

    istream.read(this.bytes);
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

  /**
   * This method write data to an output stream.
   *
   * @param ostream a stream for write
   *
   */
  public void write(OutputStream ostream) throws IOException {
    ostream.write(this.bytes);
  }

  /**
   * toString method for debugging
   *
   */
  public String toString() {
    try {
      return new String(this.bytes, Constants.CICS_DEFAULT_ENCODING);
    } catch (UnsupportedEncodingException e) {
      return "";
    }
  }
}
