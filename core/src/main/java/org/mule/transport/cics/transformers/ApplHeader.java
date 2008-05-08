package org.mule.transport.cics.transformers;

import java.io.Serializable;

/**
 * <code>ApplHeader</code> is used to store the header of the response message.
 */
public class ApplHeader implements Serializable {

	/** serial-id */
	private static final long serialVersionUID = 1L;

	/** menu ID */
	private String lzaplhdrMenuid;

	/** program ID */
	private String lzaplhdrPgmid;

	/** conv sign */
	private String lzaplhdrConvsign;

	/** dci reserve */
	private String lzaplhdrDciReserve;

	/** return code */
	private String lzaplhdrRtncd;

	/** rsn code */
	private String lzaplhdrRsncd;

	/** oth cd */
	private String lzaplhdrOthcd;

	/** msg id */
	private String lzaplhdrMsgid;

	/** msg kbn */
	private String lzaplhdrMsgkbn;

	/** msg */
	private String lzaplhdrMsg;

	/** user data len */
	private int lzaplhdrUserdataLen;

	/** applReserve */
	private String lzaplhdrApplReserve;

	/**
     * Gets the menu id.
     * 
     * @return menu id.
     */
	public String getLzaplhdrMenuid() {
		return this.lzaplhdrMenuid;
	}

	/**
     * Sets the menu id.
     * 
     * @param menuId   menu id.
     */
	public void setLzaplhdrMenuid(String menuId) {
		this.lzaplhdrMenuid = menuId;
	}

	/**
     * Gets the program id.
     * 
     * @return program id.
     */
	public String getLzaplhdrPgmid() {
		return this.lzaplhdrPgmid;
	}

	/**
     * Sets the program id.
     * 
     * @param programId  program id.
     */
	public void setLzaplhdrPgmid(String programId) {
		this.lzaplhdrPgmid = programId;
	}

	/**
     * Gets conv sig.
     * 
     * @return conv sign.
     */
	public String getLzaplhdrConvsign() {
		return this.lzaplhdrConvsign;
	}

	/**
     * Sets the conv sign.
     * 
     * @param convSign  conv sign.
     */
	public void setLzaplhdrConvsign(String convSign) {
		this.lzaplhdrConvsign = convSign;
	}

	/**
     * Gets the dci reserve.
     * 
     * @return dci reserve.
     */
	public String getLzaplhdrDciReserve() {
		return this.lzaplhdrDciReserve;
	}

	/**
     * Sets the dci reserve.
     * 
     * @param dciReserve dci reserve.
     */
	public void setLzaplhdrDciReserve(String dciReserve) {
		this.lzaplhdrDciReserve = dciReserve;
	}

	/**
     * Gets the Return code.
     * 
     * @return return code.
     */
	public String getLzaplhdrRtncd() {
		return this.lzaplhdrRtncd;
	}

	/**
     * Sets the return code.
     * 
     * @param returnCode  return code.
     */
	public void setLzaplhdrRtncd(String returnCode) {
		this.lzaplhdrRtncd = returnCode;
	}

	/**
     * Gets the rsnCode
     * 
     * @return rsnCode
     */
	public String getLzaplhdrRsncd() {
		return this.lzaplhdrRsncd;
	}

	/**
     * Sets the rsnCode
     * 
     * @param rsnCode
     */
	public void setLzaplhdrRsncd(String rsnCode) {
		this.lzaplhdrRsncd = rsnCode;
	}

	/**
     * Gets the othCd.
     * 
     * @return othCd.
     */
	public String getLzaplhdrOthcd() {
		return this.lzaplhdrOthcd;
	}

	/**
     * Sets the othCd.
     * 
     * @param othCd  othCd
     */
	public void setLzaplhdrOthcd(String othCd) {
		this.lzaplhdrOthcd = othCd;
	}

	/**
     * Gets the msg id
     * 
     * @return msg id.
     */
	public String getLzaplhdrMsgid() {
		return this.lzaplhdrMsgid;
	}

	/**
     * Sets th msg id.
     * 
     * @param msgId  msg id.
     */
	public void setLzaplhdrMsgid(String msgId) {
		this.lzaplhdrMsgid = msgId;
	}

	/**
     * Gets the msg kbn.
     * 
     * @return msg kbn.
     */
	public String getLzaplhdrMsgkbn() {
		return this.lzaplhdrMsgkbn;
	}

	/**
     * Sets the msg kbn.
     * 
     * @param msgKbn msgKbn.
     */
	public void setLzaplhdrMsgkbn(String msgKbn) {
		this.lzaplhdrMsgkbn = msgKbn;
	}

	/**
     * Gets the hdr msg.
     * 
     * @return hdr msg.
     */
	public String getLzaplhdrMsg() {
		return this.lzaplhdrMsg;
	}

	/**
     * Sets hdr msg.
     * 
     * @param hdrMsg  hdr msg.
     */
	public void setLzaplhdrMsg(String hdrMsg) {
		this.lzaplhdrMsg = hdrMsg;
	}

	/**
     * Gets the user data length.
     * 
     * @return user data length.
     */
	public int getLzaplhdrUserdataLen() {
		return this.lzaplhdrUserdataLen;
	}

	/**
     * Sets the user data length.
     * 
     * @param userDataLen  user data length.
     */
	public void setLzaplhdrUserdataLen(int userDataLen) {
		this.lzaplhdrUserdataLen = userDataLen;
	}

	/**
     * Gets applReserve.
     * 
     * @return applReserve.
     */
	public String getLzaplhdrApplReserve() {
		return this.lzaplhdrApplReserve;
	}

	/**
     * Sets applReserve.
     * 
     * @param applReserve applReserve
     */
	public void setLzaplhdrApplReserve(String applReserve) {
		this.lzaplhdrApplReserve = applReserve;
	}
}
