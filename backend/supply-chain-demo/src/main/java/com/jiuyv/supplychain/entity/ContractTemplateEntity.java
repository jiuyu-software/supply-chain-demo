package com.jiuyv.supplychain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 
 * 
 * @author hejiebing
 * @email 18817326785@163.com
 * @date 2021-04-24 17:13:31
 */
@TableName("contract_template")
public class ContractTemplateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 合约名称
	 */
	private String contractName;
	/**
	 * 合约abi
	 */
	private String contractAbi;
	/**
	 * 合约bin
	 */
	private String contractBin;
	
	private String contractBase64;
	
	public String getContractBase64() {
		return contractBase64;
	}
	public void setContractBase64(String contractBase64) {
		this.contractBase64 = contractBase64;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getContractAbi() {
		return contractAbi;
	}
	public void setContractAbi(String contractAbi) {
		this.contractAbi = contractAbi;
	}
	public String getContractBin() {
		return contractBin;
	}
	public void setContractBin(String contractBin) {
		this.contractBin = contractBin;
	}
	
	

}
