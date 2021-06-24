package com.jiuyv.supplychain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuyv.supplychain.entity.EvidenceEntity;

/**
 * 
 * 
 * @author he_jiebing@jiuyv.com
 * @date 2021-04-20 15:58:29
 */
@Mapper
public interface EvidenceDao extends BaseMapper<EvidenceEntity> {

	/**
	 * 根据合约id查询存证信息
	 * @param contractId
	 * @return
	 */
	EvidenceEntity queryByContractId(@Param("contractId") Integer contractId);

	/**
	 * 根据参与者id查询存证信息
	 * @param participaterId
	 * @return
	 */
	List<EvidenceEntity> queryByParticipaterId(@Param("participaterId") String participaterId);
	
}
