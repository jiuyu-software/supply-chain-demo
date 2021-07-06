package com.jiuyv.supplychain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author he_jiebing@jiuyv.com
 * @date 2021-04-20 15:58:29
 */
@TableName("item")
public class ItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.INPUT)
	private String id;
	/**
	 * 链ID
	 */
	private Integer chainId;
	/**
	 * 上一级供应商id
	 */
	private String lastItemId;
	/**
	 * 供应商级别
	 */
	private Integer levelOnChain;
	/**
	 * 参与者id
	 */
	private String participaterId;
	/**
	 * 插入时间
	 */
	private Date insertedAt;
	/**
	 * 更新时间
	 */
	private Date updatedAt;
	/**
	 * 分成比例
	 */
	private Integer portion;
	/**
	 * 角色
	 */
	private String role;
	/**
	 * 是否签名：0 未签名 1 已签名
	 */
	private Integer isSigned;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getChainId() {
		return chainId;
	}
	public void setChainId(Integer chainId) {
		this.chainId = chainId;
	}
	
	public String getLastItemId() {
		return lastItemId;
	}
	public void setLastItemId(String lastItemId) {
		this.lastItemId = lastItemId;
	}
	public Integer getLevelOnChain() {
		return levelOnChain;
	}
	public void setLevelOnChain(Integer levelOnChain) {
		this.levelOnChain = levelOnChain;
	}
	public String getParticipaterId() {
		return participaterId;
	}
	public void setParticipaterId(String participaterId) {
		this.participaterId = participaterId;
	}
	public Date getInsertedAt() {
		return insertedAt;
	}
	public void setInsertedAt(Date insertedAt) {
		this.insertedAt = insertedAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Integer getPortion() {
		return portion;
	}
	public void setPortion(Integer portion) {
		this.portion = portion;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Integer getIsSigned() {
		return isSigned;
	}
	public void setIsSigned(Integer isSigned) {
		this.isSigned = isSigned;
	}
	
	

}
