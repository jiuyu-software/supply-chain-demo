package com.jiuyv.supplychain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyv.supplychain.common.R;
import com.jiuyv.supplychain.service.ChainService;
import com.jiuyv.supplychain.vo.ReqNewChain;
import com.jiuyv.supplychain.vo.ReqPay;
import com.jiuyv.supplychain.vo.ReqSign;

/**
 * 
 *
 * @author he_jiebing@jiuyv.com
 * @date 2021-04-20 15:58:29
 */
@RestController
@RequestMapping("supplychain/chain")
public class ChainController {
    @Autowired
    private ChainService chainService;
    
    @PostMapping("/new")
    public R newChain(@RequestBody ReqNewChain reqNewChain){
    	return chainService.newChain(reqNewChain);
    }
    @GetMapping("/{userId}")
    public R getIndexNewInfo(@PathVariable("userId") Integer userId){
    	return chainService.getIndexNewInfo(userId);
    }
    @GetMapping("/join/{userId}")
    public R getIndexJoinInfo(@PathVariable("userId") Integer userId){
    	return chainService.getIndexJoinInfo(userId);
    }
    @GetMapping("/checkSign/{chainId}")
    public R checkSignStatus(@PathVariable("chainId") Integer chainId){
    	return chainService.checkSignStatus(chainId);
    }
    @GetMapping("/getInfo/{userId}")
    public R getChainInfoByUserId(@PathVariable("userId") Integer userId){
    	return chainService.getChainInfoByUserId(userId);
    }
    @PostMapping("/sign")
    public R sign(@RequestBody ReqSign reqSign){
    	return chainService.sign(reqSign);
    }
    @PostMapping("/pay")
    public R pay(@RequestBody ReqPay reqPay){
    	return chainService.pay(reqPay);
    }

    

}
