package com.jiuyv.supplychain.service.impl;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuyv.supplychain.common.GlobalConstant;
import com.jiuyv.supplychain.common.R;
import com.jiuyv.supplychain.dao.ChainDao;
import com.jiuyv.supplychain.dao.ParticipaterDao;
import com.jiuyv.supplychain.dao.UserDao;
import com.jiuyv.supplychain.entity.ParticipaterEntity;
import com.jiuyv.supplychain.entity.UserEntity;
import com.jiuyv.supplychain.service.UserService;
import com.jiuyv.supplychain.util.BizUtils;
import com.jiuyv.supplychain.vo.LoginResp;
import com.jiuyv.supplychain.vo.LoginVO;
import com.jiuyv.supplychain.vo.RegisterVO;
import com.jiuyv.supplychain.vo.ResTotalInfo;
import com.webank.webase.app.sdk.client.AppClient;
import com.webank.webase.app.sdk.config.HttpConfig;
import com.webank.webase.app.sdk.dto.req.ReqNewUser;
import com.webank.webase.app.sdk.dto.rsp.RspUserInfo;


/**
 * 
* <p>Title: UserServiceImpl</p>
* <p>Description: </p>
* @author he_jiebing@jiuyv.com
  @date   2021年5月24日 上午11:35:06
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ParticipaterDao participaterDao;
	
	@Autowired
	private ChainDao chainDao;
	
	@Value("${webase.node.mgr.url}")
	private String url;
	
	@Value("${webase.node.mgr.appKey}")
	private String appKey;
	
	@Value("${webase.node.mgr.appSecret}")
	private String appSecret;
	
	@Value("${webase.node.mgr.isTransferEncrypt}")
	private Boolean isTransferEncrypt;
	
	private static AppClient appClient = null;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public R register(RegisterVO registerVO) {
		// 1.先落地user
		Date date = Date.from(Instant.now());
		UserEntity userEntity = new UserEntity();
		userEntity.setInsertedAt(date);
		userEntity.setUpdatedAt(date);
		userEntity.setUsername(registerVO.getUsername());
		// 密码加密
		userEntity.setEncryptedPassword(registerVO.getPassword());
		userDao.insert(userEntity);
		
		// 调用WeBASE-APP-SDK
		ReqNewUser reqNewUser = new ReqNewUser();
        reqNewUser.setGroupId(1);
        reqNewUser.setUserName(userEntity.getUsername());
        reqNewUser.setAccount("admin");
        reqNewUser.setDescription(userEntity.getUsername());
        HttpConfig httpConfig = new HttpConfig(30, 30, 30);
        appClient = new AppClient(url, appKey, appSecret, isTransferEncrypt, httpConfig);
        RspUserInfo resp = appClient.newUser(reqNewUser);
		
		// 3.落地 participater 表
		ParticipaterEntity entity = new ParticipaterEntity();
		entity.setBalance(0L);
		entity.setOrgDescription(registerVO.getOrgDescription());
		entity.setDid(GlobalConstant.DID_WEID_PREFIX+BizUtils.subStrAddress(resp.getAddress()));
		entity.setInsertedAt(date);
		entity.setOrgName(registerVO.getOrgName());
		entity.setNameOnWebase(resp.getUserName());
		entity.setUpdatedAt(date);
		entity.setUserId(userEntity.getId());
		entity.setUserAddress(resp.getAddress());
		entity.setSignUserId(resp.getSignUserId());
		participaterDao.insert(entity);
		// 4.更新user表 participater_id字段
		UserEntity newUserEntity = new UserEntity();
		newUserEntity.setId(userEntity.getId());
		newUserEntity.setParticipaterId(entity.getId());
		userDao.updateById(newUserEntity);
		return R.ok();
	}

	@Override
	public R login(LoginVO loginVO) {
		UserEntity user = userDao.queryUserInfo(loginVO);
		if(null != user){
			LoginResp loginResp = new LoginResp();
			loginResp.setUserId(user.getId());
			loginResp.setParticipaterId(user.getParticipaterId());
			return R.ok(loginResp);
		}else{
			return R.error("用户名或密码不正确");
		}
		
	}
	@Override
	public R getUserInfo(Integer userId) {
		ParticipaterEntity entity = participaterDao.queryByUserId(userId);
		return R.ok(entity);
	}
	@Override
	public R logout() {
		// TODO 前端退出跳转页面即可
		return null;
	}
	@Override
	public R getTotalInfo() {
		Integer totalUsers =  userDao.countUsers();
		Integer totalChains = chainDao.countChains();
		ResTotalInfo res = new ResTotalInfo();
		res.setTotalChains(totalChains);
		res.setTotalUsers(totalUsers);
		return R.ok(res);
	}

}