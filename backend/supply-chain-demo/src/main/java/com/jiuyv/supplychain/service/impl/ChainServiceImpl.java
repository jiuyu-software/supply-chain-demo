package com.jiuyv.supplychain.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuyv.supplychain.bo.ContractDeployReqBO;
import com.jiuyv.supplychain.bo.ReservedMoneyBO;
import com.jiuyv.supplychain.bo.TransDataRespBO;
import com.jiuyv.supplychain.bo.TransHandleReqBO;
import com.jiuyv.supplychain.common.GlobalConstant;
import com.jiuyv.supplychain.common.R;
import com.jiuyv.supplychain.dao.ChainDao;
import com.jiuyv.supplychain.dao.ContractDao;
import com.jiuyv.supplychain.dao.ContractTemplateDao;
import com.jiuyv.supplychain.dao.EvidenceDao;
import com.jiuyv.supplychain.dao.ItemDao;
import com.jiuyv.supplychain.dao.ParticipaterDao;
import com.jiuyv.supplychain.entity.ChainEntity;
import com.jiuyv.supplychain.entity.ContractEntity;
import com.jiuyv.supplychain.entity.ContractTemplateEntity;
import com.jiuyv.supplychain.entity.EvidenceEntity;
import com.jiuyv.supplychain.entity.ItemEntity;
import com.jiuyv.supplychain.entity.ParticipaterEntity;
import com.jiuyv.supplychain.enums.SignFlagEnum;
import com.jiuyv.supplychain.enums.StatusEnum;
import com.jiuyv.supplychain.enums.TypeEnum;
import com.jiuyv.supplychain.service.ChainService;
import com.jiuyv.supplychain.util.DateUtils;
import com.jiuyv.supplychain.util.HttpUtils;
import com.jiuyv.supplychain.vo.IndexChainItemResp;
import com.jiuyv.supplychain.vo.IndexChainResp;
import com.jiuyv.supplychain.vo.ReqChainItem;
import com.jiuyv.supplychain.vo.ReqNewChain;
import com.jiuyv.supplychain.vo.ReqPay;
import com.jiuyv.supplychain.vo.ReqSign;
import com.jiuyv.supplychain.vo.ResCheckSigners;
import com.jiuyv.supplychain.vo.ResSign;
import com.jiuyv.supplychain.vo.ResSignList;
import com.webank.webase.app.sdk.client.AppClient;
import com.webank.webase.app.sdk.config.HttpConfig;
import com.webank.webase.app.sdk.dto.req.ReqContractAddressSave;
import com.webank.webase.app.sdk.dto.req.ReqContractSourceSave;
import com.webank.webase.app.sdk.dto.req.ReqContractSourceSave.ContractSource;

/**
 * 
* <p>Title: ChainServiceImpl</p>
* <p>Description: </p>
* @author he_jiebing@jiuyv.com
  @date   2021年4月25日 下午5:58:09
 */
@Service("chainService")
public class ChainServiceImpl extends ServiceImpl<ChainDao, ChainEntity> implements ChainService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChainServiceImpl.class);
	
	@Autowired
	private ChainDao chainDao;
	
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private ParticipaterDao participaterDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private EvidenceDao evidenceDao;
	
	@Autowired
	private ContractTemplateDao contractTemplateDao;
	
	@Value("${webase-front.contract.deploy.url}")
	private String webaseFrontContractDeployUrl;
	
	@Value("${webase-front.trans.handle.url}")
	private String webaseFrontTransHandleUrl;
	
	@Value("${erc20.supply.user.signUserId}")
	private String erc20SupplySignUserId;
	
	@Value("${erc20.contract.address}")
	private String erc20ContractAddress;
	
	@Value("${erc20.contract.name}")
	private String erc20ContractName;
	
	@Value("${webase.node.mgr.url}")
	private String url;
	
	@Value("${webase.node.mgr.appKey}")
	private String appKey;
	
	@Value("${webase.node.mgr.appSecret}")
	private String appSecret;
	
	@Value("${webase.node.mgr.isTransferEncrypt}")
	private Boolean isTransferEncrypt;
	
	
	private static final String ACCOUNT = "admin";
	
	private static final String CONTRACT_VERSION = "1.0.0";
	
	private static final String CONTRACT_NAME_EVIDENCE_FACTORY = "EvidenceFactory";
	
	private static final String CONTRACT_NAME_EVIDENCE = "Evidence";
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public R newChain(ReqNewChain reqNewChain) {
		// 建链需签名各供应方
		List<String> participaterAddrs = new ArrayList<String>();
		// 已签名各供应方
		List<Integer> signedParticipaterIds = new ArrayList<Integer>();
		Date date = Date.from(Instant.now());
		ParticipaterEntity participaterEntity = participaterDao.queryByUserId(reqNewChain.getUserId());
		// 1.落地chain表
		ChainEntity entity = new ChainEntity();
		entity.setUserId(reqNewChain.getUserId());
		entity.setTitle(reqNewChain.getTitle());
		entity.setChainDescribe(reqNewChain.getDesc());
		entity.setInsertedAt(date);
		entity.setUpdatedAt(date);
		entity.setSignStatus(StatusEnum.DRAFT.getStatus());
		chainDao.insert(entity);
		
		// 2.落地item表
		List<ReqChainItem> itemList = reqNewChain.getItemList();
		Map<String,Integer> evidenceValMap = new HashMap<>();
		
		for(ReqChainItem reqChainItem : itemList){
			Integer participaterId = reqChainItem.getParticipaterId();
			ItemEntity entity1 = new ItemEntity();
			entity1.setChainId(entity.getId());
			entity1.setId(reqChainItem.getItemId());
			entity1.setLastItemId(reqChainItem.getLastItemId());
			entity1.setLevelOnChain(reqChainItem.getLevelOnChain());
			entity1.setParticipaterId(participaterId);
			entity1.setInsertedAt(date);
			entity1.setUpdatedAt(date);
			entity1.setPortion(reqChainItem.getPortion());
			entity1.setRole(reqChainItem.getRole());
			// 如果建链方跟供应方是同一人默认是签名的
			if(participaterEntity.getId() == participaterId){
				entity1.setIsSigned(SignFlagEnum.SIGNED.getSignFlag());
				signedParticipaterIds.add(participaterId);
			}else{
				entity1.setIsSigned(SignFlagEnum.UNSIGN.getSignFlag());
			}
			
			ParticipaterEntity dbEntity = participaterDao.selectById(reqChainItem.getParticipaterId());
			evidenceValMap.put(dbEntity.getUserAddress(), reqChainItem.getPortion());
			itemDao.insert(entity1);
		}
		// 3.1 调用webase-front 部署合约方法
		// 获取所有参与方链上地址
		List<Integer> participaterIds = itemList.stream().filter(x->x!=null).map(x->x.getParticipaterId()).collect(Collectors.toList());
		for(Integer participaterId:participaterIds){
			ParticipaterEntity dbParticipaterEntity = participaterDao.selectById(participaterId);
			participaterAddrs.add(dbParticipaterEntity.getUserAddress());
		}
		ContractDeployReqBO contractDeployReqBO = new ContractDeployReqBO();
		String contractName = TypeEnum.EVIDENCE.getType()+"_"+DateUtils.dateTimeNow();
		ContractTemplateEntity template = contractTemplateDao.queryByTemplate(GlobalConstant.EVIDENCE_FACTORY_CONTRACT_TEMPLATE);
		JSONArray parseArray = JSONUtil.parseArray(template.getContractAbi());
		List<Object> abiList = JSONUtil.toList(parseArray, Object.class);
		contractDeployReqBO.setGroupId(1);
		contractDeployReqBO.setAbiInfo(abiList);
		contractDeployReqBO.setBytecodeBin(template.getContractBin());
		contractDeployReqBO.setContractName(contractName);
		contractDeployReqBO.setContractSource(template.getContractBase64());
		contractDeployReqBO.setVersion(CONTRACT_VERSION);
		List list = new ArrayList<>();
		list.add(participaterAddrs);
		contractDeployReqBO.setFuncParam(list);
		//contractDeployReqBO.setUser(poarticipaterEntity.getUserAddress());
		contractDeployReqBO.setSignUserId(participaterEntity.getSignUserId());
		LOGGER.info("调用webase-front接口,url>>{},请求参数:>>{}",webaseFrontContractDeployUrl,JSONUtil.toJsonStr(contractDeployReqBO));
		String response = HttpUtils.httpPostByJson(webaseFrontContractDeployUrl, JSONUtil.toJsonStr(contractDeployReqBO));
		LOGGER.info("调用webase-front接口,响应结果reslut:>>{}",response);
		
		// TODO 调用webase-sdk 同步绑定
		AppClient appClient = getAppClient();
		callWebaseSdkContractSourceSave(appClient,contractDeployReqBO);
		
		callWebaseSdkContractAddressSave(appClient,contractDeployReqBO,response,participaterEntity.getNameOnWebase());
		
		
		
		// 3.2 落地contract表
		ContractEntity contractEntity = new ContractEntity();
		contractEntity.setAddr(response);
		contractEntity.setChainId(entity.getId());
		contractEntity.setContractName(contractName);
		contractEntity.setContractDescribe(TypeEnum.EVIDENCE.getType());
		contractEntity.setInsertedAt(date);
		contractEntity.setOwnerDid(participaterEntity.getDid());
		contractEntity.setType(TypeEnum.EVIDENCE.getType());
		contractEntity.setUpdatedAt(date);
		contractDao.insert(contractEntity);
		// 4.1 调用newEvidence方法
		TransHandleReqBO transHandleReqBO = new TransHandleReqBO();
		transHandleReqBO.setContractAbi(abiList);
		// 部署的合约地址
		transHandleReqBO.setContractAddress(response);
		transHandleReqBO.setContractName(contractName);
		transHandleReqBO.setFuncName("newEvidence");
		List params = new ArrayList();
		params.add(JSONUtil.toJsonStr(evidenceValMap));
		transHandleReqBO.setFuncParam(params);
		transHandleReqBO.setGroupId(1);
		transHandleReqBO.setUseCns(false);
		//transHandleReqBO.setUser(poarticipaterEntity.getUserAddress());
		transHandleReqBO.setSignUserId(participaterEntity.getSignUserId());
		LOGGER.info("调用webase-front接口,url>>{},请求参数:>>{}",webaseFrontTransHandleUrl,JSONUtil.toJsonStr(transHandleReqBO));
		String resp = HttpUtils.httpPostByJson(webaseFrontTransHandleUrl, JSONUtil.toJsonStr(transHandleReqBO));
		LOGGER.info("调用webase-front接口,响应结果reslut:>>{}",resp);
		TransDataRespBO resBO = JSONUtil.toBean(resp, TransDataRespBO.class);
		// 4.2 落地evidence表
		EvidenceEntity evidenceEntity = new EvidenceEntity();
		evidenceEntity.setContractId(contractEntity.getId());
		evidenceEntity.setEvidenceDescribe(TypeEnum.EVIDENCE.getType());
		evidenceEntity.setInsertedAt(date);
		evidenceEntity.setEvidenceKey(resBO.getLogs().get(0).getAddress());
		evidenceEntity.setOwners(JSONUtil.toJsonStr(participaterIds));
		evidenceEntity.setSigners(JSONUtil.toJsonStr(signedParticipaterIds));
		List<String> txIds = new ArrayList<String>();
		txIds.add(resBO.getTransactionHash());
		evidenceEntity.setTxId(JSONUtil.toJsonStr(txIds));
		evidenceEntity.setUpdatedAt(date);
		evidenceEntity.setEvidenceValue(JSONUtil.toJsonStr(evidenceValMap));
		evidenceDao.insert(evidenceEntity);
		return R.ok();
	}

	@Override
	public R getIndexNewInfo(Integer userId) {
		ParticipaterEntity poarticipaterEntity = participaterDao.queryByUserId(userId);
		List<IndexChainResp> respList = new ArrayList<IndexChainResp>();
		List<ChainEntity> chainList =  chainDao.queryByUserId(userId);
		for(ChainEntity chainEntity : chainList){
			IndexChainResp indexChainResp = new IndexChainResp();
			ContractEntity dbContract = contractDao.queryByChainId(chainEntity.getId());
			indexChainResp.setDesc(chainEntity.getChainDescribe());
			indexChainResp.setContractAddress(dbContract.getAddr());
			EvidenceEntity dbEvidenceEntity =  evidenceDao.queryByContractId(dbContract.getId());
			indexChainResp.setEvidenceKey(dbEvidenceEntity.getEvidenceKey());
			indexChainResp.setStatus(chainEntity.getSignStatus());
			indexChainResp.setChainId(chainEntity.getId());
			// 根据chainId获取对应供应商信息
			List<ItemEntity> items = itemDao.queryByChainId(chainEntity.getId());
			List<IndexChainItemResp> itemList = new ArrayList<IndexChainItemResp>();
			for(ItemEntity itemEntity : items){
				ParticipaterEntity dbParticipaterEntity = participaterDao.selectById(itemEntity.getParticipaterId());
				IndexChainItemResp itemResp = new IndexChainItemResp();
				itemResp.setParticipaterDidId(dbParticipaterEntity.getDid());
				itemResp.setParticipaterOrgName(dbParticipaterEntity.getOrgName());
				itemResp.setPortion(itemEntity.getPortion());
				itemResp.setRole(itemEntity.getRole());
				itemList.add(itemResp);
			}
			indexChainResp.setItemList(itemList);
			indexChainResp.setOrgName(poarticipaterEntity.getOrgName());
			indexChainResp.setTitle(chainEntity.getTitle());
			respList.add(indexChainResp);
		}
		return R.ok(respList);
	}
	@Override
	public R getIndexJoinInfo(Integer userId) {
		ParticipaterEntity poarticipaterEntity = participaterDao.queryByUserId(userId);
		List<IndexChainResp> respList = new ArrayList<IndexChainResp>();
		List<EvidenceEntity> dbEvidences =  evidenceDao.queryByParticipaterId(String.valueOf(poarticipaterEntity.getId()));
		for(EvidenceEntity evidenceEntity : dbEvidences){
			IndexChainResp indexChainResp = new IndexChainResp();
			indexChainResp.setEvidenceKey(evidenceEntity.getEvidenceKey());
			ContractEntity dbContract = contractDao.selectById(evidenceEntity.getContractId());
			ChainEntity dbChain = chainDao.selectById(dbContract.getChainId());
			indexChainResp.setStatus(dbChain.getSignStatus());
			indexChainResp.setChainId(dbChain.getId());
			indexChainResp.setDesc(dbChain.getChainDescribe());
			indexChainResp.setContractAddress(dbContract.getAddr());
			// 根据chainId获取对应供应商信息
			List<ItemEntity> items = itemDao.queryByChainId(dbChain.getId());
			List<IndexChainItemResp> itemList = new ArrayList<IndexChainItemResp>();
			for(ItemEntity itemEntity : items){
				ParticipaterEntity dbParticipaterEntity = participaterDao.selectById(itemEntity.getParticipaterId());
				IndexChainItemResp itemResp = new IndexChainItemResp();
				itemResp.setParticipaterDidId(dbParticipaterEntity.getDid());
				itemResp.setParticipaterOrgName(dbParticipaterEntity.getOrgName());
				itemResp.setPortion(itemEntity.getPortion());
				itemResp.setRole(itemEntity.getRole());
				itemList.add(itemResp);
			}
			indexChainResp.setItemList(itemList);
			indexChainResp.setOrgName(poarticipaterEntity.getOrgName());
			indexChainResp.setTitle(dbChain.getTitle());
			respList.add(indexChainResp);
		}
		return R.ok(respList);
	}
	@Override
	public R checkSignStatus(Integer chainId) {
		List<String> signedOrgNames = new ArrayList<String>();
		List<String> needSignOrgNames = new ArrayList<String>();
		ContractEntity dbChain = contractDao.queryByChainId(chainId);
		EvidenceEntity dbEvidence = evidenceDao.queryByContractId(dbChain.getId());
		String owners = dbEvidence.getOwners();
		List<Integer> ownerparticipaterIds = com.alibaba.fastjson.JSONArray.parseArray(owners, Integer.class);
		for(Integer id : ownerparticipaterIds){
			ParticipaterEntity dbEntity = participaterDao.selectById(id);
			needSignOrgNames.add(dbEntity.getOrgName());
		}
		String signers = dbEvidence.getSigners();
		List<Integer> signedparticipaterIds = com.alibaba.fastjson.JSONArray.parseArray(signers, Integer.class);
		for(Integer id : signedparticipaterIds){
			ParticipaterEntity dbEntity = participaterDao.selectById(id);
			signedOrgNames.add(dbEntity.getOrgName());
		}
		// TODO 调用合约的getEvidence方法
		// 先简单点只check ownerparticipaterIds 是否与signedparticipaterIds 匹配
		ResCheckSigners res = new ResCheckSigners();
		res.setSignedOrgNames(signedOrgNames );
		res.setNeedSignOrgNames(needSignOrgNames );
		return R.ok(res);
	}
	@Override
	public R getChainInfoByUserId(Integer userId) {
		// 获取自己建的链
		List<ChainEntity> chainList =  chainDao.queryByUserId(userId);
		Set<ResSignList> resSignLists = new HashSet<ResSignList>();
		for(ChainEntity chainEntity : chainList){
			ResSignList resSignList = new ResSignList();
			resSignList.setChainId(chainEntity.getId());
			resSignList.setTitle(chainEntity.getTitle());
			resSignLists.add(resSignList);
		}
		// TODO 查询自己参与的链
		ParticipaterEntity poarticipaterEntity = participaterDao.queryByUserId(userId);
		List<EvidenceEntity> dbEvidences =  evidenceDao.queryByParticipaterId(String.valueOf(poarticipaterEntity.getId()));
		for(EvidenceEntity evidenceEntity : dbEvidences){
			IndexChainResp indexChainResp = new IndexChainResp();
			indexChainResp.setEvidenceKey(evidenceEntity.getEvidenceKey());
			ContractEntity dbContract = contractDao.selectById(evidenceEntity.getContractId());
			ChainEntity dbChain = chainDao.selectById(dbContract.getChainId());
			ResSignList resSignList = new ResSignList();
			resSignList.setChainId(dbChain.getId());
			resSignList.setTitle(dbChain.getTitle());
			
			resSignLists.add(resSignList);
		}
		
		return R.ok(resSignLists);
	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public R sign(ReqSign reqSign) {
		Date date = Date.from(Instant.now());
		// 根据userId
		Integer participaterId = reqSign.getParticipaterId();
		// 1.check 已加签的不需要重复加签
		ParticipaterEntity dbParticipaterEntity = participaterDao.selectById(participaterId);
		ContractEntity dbContract = contractDao.queryByChainId(reqSign.getChainId());
		EvidenceEntity dbEvidence = evidenceDao.queryByContractId(dbContract.getId());
		String owners = dbEvidence.getOwners();
		List<Integer> ownerparticipaterIds = com.alibaba.fastjson.JSONArray.parseArray(owners, Integer.class);
		String signers = dbEvidence.getSigners();
		List<Integer> signedparticipaterIds = com.alibaba.fastjson.JSONArray.parseArray(signers, Integer.class);
		if(ownerparticipaterIds.contains(participaterId)&&signedparticipaterIds.contains(participaterId)){
			return R.error("用户已签名，无须重复加签");
		}
		// 2.调用合约的addSignatures 方法
		TransHandleReqBO transHandleReqBO = new TransHandleReqBO();
		ContractTemplateEntity template = contractTemplateDao.queryByTemplate(GlobalConstant.EVIDENCE_FACTORY_CONTRACT_TEMPLATE);
		JSONArray parseArray = JSONUtil.parseArray(template.getContractAbi());
		List<Object> abiList = JSONUtil.toList(parseArray, Object.class);
		transHandleReqBO.setContractAbi(abiList);
		// 部署的合约地址
		transHandleReqBO.setContractAddress(dbContract.getAddr());
		transHandleReqBO.setContractName(dbContract.getContractName());
		transHandleReqBO.setFuncName("addSignatures");
		List params = new ArrayList();
		params.add(dbEvidence.getEvidenceKey());
		transHandleReqBO.setFuncParam(params);
		transHandleReqBO.setGroupId(1);
		transHandleReqBO.setUseCns(false);
		//transHandleReqBO.setUser(dbParticipaterEntity.getUserAddress());
		transHandleReqBO.setSignUserId(dbParticipaterEntity.getSignUserId());
		LOGGER.info("调用webase-front接口,url>>{},请求参数:>>{}",webaseFrontTransHandleUrl,JSONUtil.toJsonStr(transHandleReqBO));
		String resp = HttpUtils.httpPostByJson(webaseFrontTransHandleUrl, JSONUtil.toJsonStr(transHandleReqBO));
		LOGGER.info("调用webase-front接口,响应结果reslut:>>{}",resp);
		TransDataRespBO resBO = JSONUtil.toBean(resp, TransDataRespBO.class);
		// 3.更新 item 表的is_signed字段
		ItemEntity query = new ItemEntity();
		query.setChainId(reqSign.getChainId());
		query.setParticipaterId(participaterId);
		ItemEntity dbEntity = itemDao.queryInfo(query);
		ItemEntity updateEntity = new ItemEntity();
		updateEntity.setId(dbEntity.getId());
		updateEntity.setUpdatedAt(date);
		updateEntity.setIsSigned(SignFlagEnum.SIGNED.getSignFlag());
		itemDao.updateById(updateEntity);
		// 4.更新 evidence表的tx_id 和 signers 字段
		EvidenceEntity dbEvidenceEntity = new EvidenceEntity();
		dbEvidenceEntity.setId(dbEvidence.getId());
		JSONArray txIdArray = JSONUtil.parseArray(dbEvidence.getTxId());
		List<String> txIds = JSONUtil.toList(txIdArray, String.class);
		txIds.add(resBO.getTransactionHash());
		dbEvidenceEntity.setTxId(JSONUtil.toJsonStr(txIds));
		JSONArray signerArray = JSONUtil.parseArray(dbEvidence.getSigners());
		List<Integer> signerList = JSONUtil.toList(signerArray, Integer.class);
		signerList.add(participaterId);
		dbEvidenceEntity.setSigners(JSONUtil.toJsonStr(signerList));
		evidenceDao.updateById(dbEvidenceEntity);
		// 5.更新chain 主表 sign_status 字段
		// TODO 调用链上合约方法getEvidence方法再比对 evidence表 owners字段和signers字段是否相等
		EvidenceEntity evidenceEntity = evidenceDao.selectById(dbEvidence.getId());
		String owners2 = evidenceEntity.getOwners();
		List<String> ownerparticipaterIds2 = com.alibaba.fastjson.JSONArray.parseArray(owners2, String.class);
		String signers2 = evidenceEntity.getSigners();
		List<String> signedparticipaterIds2 = com.alibaba.fastjson.JSONArray.parseArray(signers2, String.class);
		boolean flag = ownerparticipaterIds2.stream().sorted().collect(Collectors.joining()).equals(signedparticipaterIds2.stream().sorted().collect(Collectors.joining()));
		ChainEntity updateChainEntity = new ChainEntity();
		if(flag){
			updateChainEntity.setSignStatus(StatusEnum.CONFIRMED.getStatus());
			updateChainEntity.setId(reqSign.getChainId());
			chainDao.updateById(updateChainEntity);
		}
		ResSign resSign = new ResSign();
		resSign.setTxHash(resBO.getTransactionHash());
		return R.ok(resSign);
	}
	@Transactional(rollbackFor=Exception.class)
	@Override
	public R pay(ReqPay reqPay) {
		// 1.check 状态是确认过的才可以分账
		Integer chainId = reqPay.getChainId();
		Long totalAmount = reqPay.getTotalAmount();
		ChainEntity dbChain = chainDao.selectById(chainId);
		if(StatusEnum.DRAFT.getStatus().equals(dbChain.getSignStatus())){
			return R.error("该链还未被确认，不能进行分账");
		}
		// 2.TODO check 发币方有足够余额
		List<ItemEntity> items = itemDao.queryByChainId(chainId);
		List<ReservedMoneyBO> reservedUsers = new ArrayList<>();
		for(ItemEntity item : items){
			ReservedMoneyBO bo = new ReservedMoneyBO();
			bo.setParticipaterId(item.getParticipaterId());
			ParticipaterEntity dbParticipaterEntity = participaterDao.selectById(item.getParticipaterId());
			bo.setUserAddress(dbParticipaterEntity.getUserAddress());
			bo.setAmount(calculateAmount(totalAmount,item.getPortion()));
			bo.setOrgName(dbParticipaterEntity.getOrgName());
			bo.setBalance(dbParticipaterEntity.getBalance()+bo.getAmount());
			// 3.调用erc20 合约的transfer 方法
			TransHandleReqBO transHandleReqBO = new TransHandleReqBO();
			ContractTemplateEntity template = contractTemplateDao.queryByTemplate(GlobalConstant.ERC20_CONTRACT_TEMPLATE);
			JSONArray parseArray = JSONUtil.parseArray(template.getContractAbi());
			List<Object> abiList = JSONUtil.toList(parseArray, Object.class);
			transHandleReqBO.setContractAbi(abiList);
			// 部署的合约地址
			transHandleReqBO.setContractAddress(erc20ContractAddress);
			transHandleReqBO.setContractName(erc20ContractName);
			transHandleReqBO.setFuncName("transfer");
			List params = new ArrayList();
			params.add(bo.getUserAddress());
			params.add(new BigDecimal(bo.getAmount()).divide(new BigDecimal(100)).longValue());
			transHandleReqBO.setFuncParam(params);
			transHandleReqBO.setGroupId(1);
			transHandleReqBO.setUseCns(false);
			transHandleReqBO.setSignUserId(erc20SupplySignUserId);
			LOGGER.info("调用webase-front接口,url>>{},请求参数:>>{}",webaseFrontTransHandleUrl,JSONUtil.toJsonStr(transHandleReqBO));
			String resp = HttpUtils.httpPostByJson(webaseFrontTransHandleUrl, JSONUtil.toJsonStr(transHandleReqBO));
			LOGGER.info("调用webase-front接口,响应结果reslut:>>{}",resp);
			TransDataRespBO resBO = JSONUtil.toBean(resp, TransDataRespBO.class);
			bo.setTxHash(resBO.getTransactionHash());
			reservedUsers.add(bo);
		}
		// 4.更新participater的balance字段
		for(ReservedMoneyBO moneyBO : reservedUsers){
			ParticipaterEntity entity = new ParticipaterEntity();
			entity.setId(moneyBO.getParticipaterId());
			entity.setBalance(moneyBO.getBalance());
			participaterDao.updateById(entity);
		}
		return R.ok(reservedUsers);
	}
	/**
	 * 计算应获得的金额
	 * @param totalAmount
	 * @param portion
	 * @return
	 */
	private Long calculateAmount(Long totalAmount,Integer portion){
		return new BigDecimal(totalAmount).multiply(new BigDecimal(portion)).divide(new BigDecimal(100L)).longValue();
	}
	
	private void callWebaseSdkContractSourceSave(AppClient appClient,ContractDeployReqBO contractDeployReqBO){
		ReqContractSourceSave reqContractSourceSave = new ReqContractSourceSave();
        reqContractSourceSave.setAccount(ACCOUNT);
        reqContractSourceSave.setContractVersion(CONTRACT_VERSION);

        List<ContractSource> contractList = new ArrayList<>();
        // add EvidenceFactory contract
        ContractSource evidenceFactoryContractSource = new ContractSource();
        evidenceFactoryContractSource.setContractName(CONTRACT_NAME_EVIDENCE_FACTORY);
        evidenceFactoryContractSource.setContractSource(contractDeployReqBO.getContractSource());
        evidenceFactoryContractSource.setContractAbi(JSONUtil.toJsonStr(contractDeployReqBO.getAbiInfo()));
        evidenceFactoryContractSource.setBytecodeBin(contractDeployReqBO.getBytecodeBin());
        // add Evidence contract
        ContractSource evidenceContractSource = new ContractSource();
        ContractTemplateEntity template = contractTemplateDao.queryByTemplate(GlobalConstant.EVIDENCE_CONTRACT_TEMPLATE);
		JSONArray parseArray = JSONUtil.parseArray(template.getContractAbi());
		List<Object> abiList = JSONUtil.toList(parseArray, Object.class);
        evidenceContractSource.setContractName(CONTRACT_NAME_EVIDENCE);
        evidenceContractSource.setContractSource(template.getContractBase64());
        evidenceContractSource.setContractAbi(JSONUtil.toJsonStr(abiList));
        evidenceContractSource.setBytecodeBin(template.getContractBin());
        
        
        
        contractList.add(evidenceContractSource);
        contractList.add(evidenceFactoryContractSource);
        reqContractSourceSave.setContractList(contractList);
        LOGGER.info("调用WebaseSdk ContractSourceSave接口,请求参数:>>{}",JSONUtil.toJsonStr(reqContractSourceSave));
        
        appClient.contractSourceSave(reqContractSourceSave);
	}
	private void callWebaseSdkContractAddressSave(AppClient appClient,ContractDeployReqBO contractDeployReqBO,String contractAddr,String username){
		ReqContractAddressSave reqContractAddressSave = new ReqContractAddressSave();
        reqContractAddressSave.setGroupId(1);
        reqContractAddressSave.setContractName(CONTRACT_NAME_EVIDENCE_FACTORY);
        reqContractAddressSave.setContractPath(username+"_"+DateUtils.dateTimeNow());
        reqContractAddressSave.setContractVersion(CONTRACT_VERSION);
        reqContractAddressSave.setContractAddress(contractAddr);
        LOGGER.info("调用WebaseSdk AddressSave接口,请求参数:>>{}",JSONUtil.toJsonStr(reqContractAddressSave));
        appClient.contractAddressSave(reqContractAddressSave);
	}
	
	private AppClient getAppClient(){
		HttpConfig httpConfig = new HttpConfig(30, 30, 30);
        return new AppClient(url, appKey, appSecret, isTransferEncrypt, httpConfig);
	}

	
	

}