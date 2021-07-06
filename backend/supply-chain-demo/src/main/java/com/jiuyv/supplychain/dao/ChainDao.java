package com.jiuyv.supplychain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuyv.supplychain.entity.ChainEntity;

/**
 * 
 * 
 * @author he_jiebing@jiuyv.com
 * @date 2021-04-20 15:58:29
 */
@Mapper
public interface ChainDao extends BaseMapper<ChainEntity> {

	/**
	 * 根据用户id查询链路
	 * @param userId
	 * @return
	 */
	List<ChainEntity> queryByUserId(@Param("userId") String userId);

	/**
	 * 统计总链路数
	 * @return
	 */
	Integer countChains();
	
}
