package com.jiuyv.supplychain.vo;

import java.io.Serializable;

/**
 * <p>Title: ReqChainItem</p>
 * <p>Description: </p>
 * @author he_jiebing@jiuyv.com
   @date   2021年4月23日 下午3:51:03
 */

public class ReqChainItem implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5913486136685829601L;
	/**
	 * 供应商id
	 */
	private String itemId;
	/**
	 * 上一级供应商id
	 */
	private String lastItemId;
	/**
	 * 供应商链路级别
	 */
	private Integer levelOnChain;
	/**
	 * 角色
	 */
	private String role;
	/**
	 * 分成比例
	 */
	private Integer portion;
	/**
	 * 参与方id
	 */
	private String participaterId;
	

	public String getLastItemId() {
		return lastItemId;
	}

	public void setLastItemId(String lastItemId) {
		this.lastItemId = lastItemId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Integer getLevelOnChain() {
		return levelOnChain;
	}

	public void setLevelOnChain(Integer levelOnChain) {
		this.levelOnChain = levelOnChain;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}



	public Integer getPortion() {
		return portion;
	}

	public void setPortion(Integer portion) {
		this.portion = portion;
	}

	public String getParticipaterId() {
		return participaterId;
	}

	public void setParticipaterId(String participaterId) {
		this.participaterId = participaterId;
	}
	
	
	

}
