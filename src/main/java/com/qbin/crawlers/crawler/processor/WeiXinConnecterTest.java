package com.qbin.crawlers.crawler.processor;

import com.alibaba.fastjson.JSONObject;
import com.qbin.crawlers.common.globalconst.WeiXinConst;
import com.qbin.crawlers.tuitui.model.WeiXinInfo;
import com.qbin.crawlers.tuitui.model.WeiXinUser;
import com.qbin.crawlers.common.util.HttpRequest;
import com.qbin.crawlers.common.util.Json;
import com.qbin.crawlers.common.util.Utils;
import com.qbin.crawlers.common.util.WeiXinUtil;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：微信通讯协议登录
 * author qiaobin   2016/10/12 19:03.
 */
public class WeiXinConnecterTest {

    public static void main(String[] args) {
        //1.获取UUID
        String toGetUUIDUrl = WeiXinConst.url_getuuid.replace("{t}", ""+System.currentTimeMillis());
        String uuidUrl = HttpRequest.sendGet(toGetUUIDUrl, null);
        String uuid = uuidUrl.substring(uuidUrl.indexOf("\"")+1, uuidUrl.lastIndexOf("\""));
        //2.取得登录二维码
        String url_login = WeiXinConst.url_login2weima.replace("{}", uuid);
        String imgUrl = HttpRequest.dowmImg(url_login);
        //3.轮询手机端是否已经扫描二维码并确认在Web端登录
        String login_url = WeiXinUtil.check_login(uuid);
        //4.登陆成功，获取cookies及关键参数
        com.blade.kit.http.HttpRequest request = com.blade.kit.http.HttpRequest.get(login_url+"&fun=new");
        String res = request.body();

        Map<String, String> cookies = Utils.getCookies(login_url);
        WeiXinInfo info = new WeiXinInfo();
        info.setWxuin(cookies.get("wxuin"));
        info.setWxsid(cookies.get("wxsid"));
        info.setWebwxuvid(cookies.get("webwxuvid"));
        info.setDeviceid("e960817075982756");
        info.setPass_ticket(match("<pass_ticket>(\\S+)</pass_ticket>", res));
        //4.调用初始化url(初始化界面等信息)
        JSONObject post = new JSONObject();
        JSONObject BaseRequest = new JSONObject();
        post.put("BaseRequest", BaseRequest);
        BaseRequest.put("Uin", info.getWxuin());
        BaseRequest.put("Sid", info.getWxsid());
        BaseRequest.put("Skey", "");
        BaseRequest.put("DeviceID", info.getDeviceid());
        String intUrl = WeiXinConst.url_init.replace("{t}", ""+System.currentTimeMillis());
        //5.发送Post请求
        String initContent = Utils.postString(intUrl, post.toString());
        JSONObject init =  JSONObject.parseObject(initContent);
        JSONObject user  = init.getJSONObject("User");
        System.out.println("欢迎您："+user);
        info.setUjson(user);
        info.setName(user.getString("UserName"));
        info.setNickName(user.getString("NickName"));
        //6.获取好友列表
        String contactListUrl = WeiXinConst.url_contactlist.replace("{t}", ""+System.currentTimeMillis());
        String friendsList = Utils.postString(contactListUrl, post.toString());
        JSONObject friends =  JSONObject.parseObject(initContent);
        String ContactListString = friends.getString("ContactList");
//        JSONObject contactList =  JSONObject.parseObject(ContactListString);
        List<WeiXinUser> weiXinUser = null;
        try {
            weiXinUser = (List<WeiXinUser>) Json.toObjectList(ContactListString, WeiXinUser.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        List<WeiXinMember> members = weiXinUser.getMemberList();
        String toName = weiXinUser.get(2).getUserName();
        //7.发送消息
        WeiXinUtil.sendMsg(toName, "来自电脑~~~啦啦啦啦", info);

    }

    public static String match(String p, String str){
        Pattern pattern = Pattern.compile(p);
        Matcher m = pattern.matcher(str);
        if(m.find()){
            return m.group(1);
        }
        return null;
    }

}
