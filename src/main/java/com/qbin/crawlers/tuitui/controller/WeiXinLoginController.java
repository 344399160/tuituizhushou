package com.qbin.crawlers.tuitui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qbin.crawlers.common.util.Json;
import com.qbin.crawlers.weixin.service.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：登录控制器
 * author qiaobin   2016/10/18 18:57.
 */
@RestController
@RequestMapping("login")
public class WeiXinLoginController {

    @Autowired
    private WeiXinService weiXinService;

    /*
    *   标签列表
    * */
    @RequestMapping(method = RequestMethod.GET)
    public String labelList(@RequestParam(required = false) String objId){
        try {
            return Json.toJsonString(weiXinService.getQRCode());
        } catch (JsonProcessingException e) {
            return "fail";
        }
    }
}
