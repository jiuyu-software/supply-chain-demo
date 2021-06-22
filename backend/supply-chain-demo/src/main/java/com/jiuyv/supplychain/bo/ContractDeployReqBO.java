package com.jiuyv.supplychain.bo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: ContractDeployReqBO</p>
 * <p>Description: </p>
 * @author he_jiebing@jiuyv.com
   @date   2021年4月24日 下午6:49:37
 */

public class ContractDeployReqBO implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3581631939705496398L;

	private Integer groupId;
	
	private String user;
	
	private String contractName;
	
	private List abiInfo;
	
	private String bytecodeBin;
	
	private List funcParam;
	
	private String signUserId;
	
	private String contractSource;
	
	private String version;
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContractSource() {
		return contractSource;
	}

	public void setContractSource(String contractSource) {
		this.contractSource = contractSource;
	}

	public String getSignUserId() {
		return signUserId;
	}

	public void setSignUserId(String signUserId) {
		this.signUserId = signUserId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	

	public String getBytecodeBin() {
		return bytecodeBin;
	}

	public void setBytecodeBin(String bytecodeBin) {
		this.bytecodeBin = bytecodeBin;
	}

	public List getAbiInfo() {
		return abiInfo;
	}

	public void setAbiInfo(List abiInfo) {
		this.abiInfo = abiInfo;
	}

	public List getFuncParam() {
		return funcParam;
	}

	public void setFuncParam(List funcParam) {
		this.funcParam = funcParam;
	}

	
	
	

}
