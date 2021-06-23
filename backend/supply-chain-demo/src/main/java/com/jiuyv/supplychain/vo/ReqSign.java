package com.jiuyv.supplychain.vo;

import java.io.Serializable;

/**
 * <p>Title: ReqSign</p>
 * <p>Description: </p>
 * @author he_jiebing@jiuyv.com
   @date   2021年4月23日 下午5:41:21
 */

public class ReqSign implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2978641475648819844L;
	/**
	 * 签名人id
	 */
	private Integer participaterId;
	/**
	 * 链条id
	 */
	private Integer chainId;
	
	private Integer userId;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getParticipaterId() {
		return participaterId;
	}

	public void setParticipaterId(Integer participaterId) {
		this.participaterId = participaterId;
	}

	public Integer getChainId() {
		return chainId;
	}

	public void setChainId(Integer chainId) {
		this.chainId = chainId;
	}
	
	

}
