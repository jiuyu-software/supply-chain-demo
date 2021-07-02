package com.jiuyv.supplychain.bo;

import java.io.Serializable;

/**
 * <p>Title: ReservedMoney</p>
 * <p>Description: </p>
 * @author he_jiebing@jiuyv.com
   @date   2021年4月25日 下午3:25:41
 */

public class ReservedMoneyBO implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9169066551835613134L;
	
	private String participaterId;
	
	private String userAddress;
	
	private Long amount;
	
	private Long balance;
	
	private String orgName;
	
	private String txHash;
	
	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getParticipaterId() {
		return participaterId;
	}

	public void setParticipaterId(String participaterId) {
		this.participaterId = participaterId;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}
	
	

}
