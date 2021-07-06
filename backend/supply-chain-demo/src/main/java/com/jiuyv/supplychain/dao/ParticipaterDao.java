package com.jiuyv.supplychain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuyv.supplychain.entity.ParticipaterEntity;

/**
 * 
 * 
 * @author he_jiebing@jiuyv.com
 * @date 2021-04-20 15:58:29
 */
@Mapper
public interface ParticipaterDao extends BaseMapper<ParticipaterEntity> {

	/**
	 * 查询所有参与者
	 * @return
	 */
	List<ParticipaterEntity> queryAll();

	/**
	 * 根据用户id查询参与方信息
	 * @param userId
	 * @return
	 */
	ParticipaterEntity queryByUserId(@Param("userId") String userId);
	
}
