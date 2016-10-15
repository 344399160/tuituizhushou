package com.qbin.crawlers.tuitui.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 描述：微信实体
 * author qiaobin   2016/10/12 19:44.
 */
@Data
public class WeiXinInfo {
    String wxuin;
    String wxsid;
    String webwxuvid;
    String deviceid;
    String syncKeyStr;
    JSONObject SyncKey;
    String SKey;
    JSONObject ujson;
    String name;   //帐号
    String nickName;//昵称
    boolean connect = false;
    String pass_ticket;
}
