package com.jiuyv.supplychain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyv.supplychain.service.ItemService;



/**
 * 
 *
 * @author he_jiebing@jiuyv.com
 * @date 2021-04-20 15:58:29
 */
@RestController
@RequestMapping("supplychain/item")
public class ItemController {
    @Autowired
    private ItemService itemService;



}
