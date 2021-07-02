package com.jiuyv.supplychain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiuyv.supplychain.common.R;
import com.jiuyv.supplychain.entity.UserEntity;
import com.jiuyv.supplychain.vo.LoginVO;
import com.jiuyv.supplychain.vo.RegisterVO;

/**
 * 
 *
 * @author he_jiebing@jiuyv.com
 * @date 2021-04-20 15:58:29
 */
public interface UserService extends IService<UserEntity> {
	
	/**
	 * 注册接口
	 * @param userEntity
	 * @return
	 */
	R register(RegisterVO registerVO) throws Exception;
	
	/**
	 * 登录
	 * @param loginVO
	 * @return
	 */
	R login(LoginVO loginVO) throws Exception;
	
	/**
	 * 获取用户信息
	 * @param userId
	 * @return
	 */
	R getUserInfo(String userId);
	
	/**
	 * 登出
	 * @return
	 */
	R logout();
	
	/**
	 * 获取首页未登陆信息数据
	 * @return
	 */
	R getTotalInfo();

}

