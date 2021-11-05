package com.jiuyv.supplychain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;

import com.jiuyv.supplychain.vo.ReqChainItem;
import com.jiuyv.supplychain.vo.ReqNewChain;

/**
 * <p>Title: TestMain</p>
 * <p>Description: </p>
 * @author he_jiebing@jiuyv.com
   @date   2021年4月21日 下午4:10:03
 */

public class TestMain {
	
	public static void testNewChain(){
		ReqNewChain reqNewChain = new ReqNewChain();
		reqNewChain.setDesc("小米手机供应链0429");
		reqNewChain.setTitle("小米手机供应链0429");
		//reqNewChain.setUserId(16);
		
		List<ReqChainItem> itemList = new ArrayList<ReqChainItem>();
		ReqChainItem item1 = new ReqChainItem();
		item1.setItemId(UUID.fastUUID().toString().replace("-", ""));
		item1.setLastItemId(null);
		item1.setLevelOnChain(0);
		//item1.setParticipaterId(38);
		item1.setPortion(30);
		item1.setRole("供应商1");
		
		
		ReqChainItem item2 = new ReqChainItem();
		item2.setItemId(UUID.fastUUID().toString().replace("-", ""));
		item2.setLastItemId(item1.getItemId());
		item2.setLevelOnChain(1);
		//item2.setParticipaterId(39);
		item2.setPortion(40);
		item2.setRole("供应商2");
		
		ReqChainItem item3 = new ReqChainItem();
		item3.setItemId(UUID.fastUUID().toString().replace("-", ""));
		item3.setLastItemId(item2.getItemId());
		item3.setLevelOnChain(2);
		//item3.setParticipaterId(40);
		item3.setPortion(30);
		item3.setRole("供应商3");
		
		itemList.add(item1);
		itemList.add(item2);
		itemList.add(item3);
		reqNewChain.setItemList(itemList);
		
		System.out.println(JSONUtil.toJsonPrettyStr(reqNewChain));
	}
	
	public static void main(String[] args) {
		//String privateKey = UUID.fastUUID().toString();
		//System.out.println(privateKey.replaceAll("-", ""));
		Map<String,Integer> evidenceValMap = new HashMap<>();
		evidenceValMap.put("0x123", 20);
		
		evidenceValMap.put("0x456", 80);
		
		String jsonStr = JSONUtil.toJsonStr(evidenceValMap);
		List params = new ArrayList();
		params.add(jsonStr);
		params.add("1");
		System.out.println(params);
		System.out.println(jsonStr);
		System.out.println("======");
		testNewChain();
		
	}

}
