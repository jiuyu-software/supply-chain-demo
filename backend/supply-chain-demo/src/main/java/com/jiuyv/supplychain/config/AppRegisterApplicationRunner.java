package com.jiuyv.supplychain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.webank.webase.app.sdk.client.AppClient;
import com.webank.webase.app.sdk.config.HttpConfig;
import com.webank.webase.app.sdk.dto.req.ReqAppRegister;

/**
 * <p>Title: AppRegisterApplicationRunner</p>
 * <p>Description: </p>
 * @author he_jiebing@jiuyv.com
   @date   2021年6月24日 上午11:17:09
 */
@Component
public class AppRegisterApplicationRunner implements ApplicationRunner{
	
	@Value("${webase.node.mgr.url}")
	private String url;
	
	@Value("${webase.node.mgr.appKey}")
	private String appKey;
	
	@Value("${webase.node.mgr.appSecret}")
	private String appSecret;
	
	@Value("${webase.node.mgr.isTransferEncrypt}")
	private Boolean isTransferEncrypt;
	
	@Value("${webase.node.mgr.appIp}")
	private String appIp;
	
	@Value("${webase.node.mgr.appPort}")
	private Integer appPort;
	
	@Value("${webase.node.mgr.appLink}")
	private String appLink;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		ReqAppRegister req = new ReqAppRegister();
        req.setAppIp(appIp);
        req.setAppPort(appPort);
        req.setAppLink(appLink);
        HttpConfig httpConfig = new HttpConfig(30, 30, 30);
        AppClient appClient = new AppClient(url, appKey, appSecret, isTransferEncrypt, httpConfig);
        appClient.appRegister(req);
		
	}

}
