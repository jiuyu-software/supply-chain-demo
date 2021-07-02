package com.jiuyv.supplychain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: ReqNewChain</p>
 * <p>Description: </p>
 * @author he_jiebing@jiuyv.com
   @date   2021年4月23日 下午3:50:09
 */

public class ReqNewChain implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6094498793318107274L;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 链名称
	 */
	private String title;
	/**
	 * 链描述
	 */
	private String desc;
	/**
	 * 供应商信息
	 */
	private List<ReqChainItem> itemList;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<ReqChainItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<ReqChainItem> itemList) {
		this.itemList = itemList;
	}

	
	
	

}
