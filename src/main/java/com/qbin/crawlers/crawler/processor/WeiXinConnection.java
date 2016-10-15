package com.qbin.crawlers.crawler.processor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qbin.crawlers.common.util.Utils;

/**
 * 描述：TODO
 * author qiaobin   2016/10/12 18:41.
 */
public class WeiXinConnection {
    String url_getuuid="https://login.weixin.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_={t}";

    String url_login2weima="https://login.weixin.qq.com/qrcode/{}?t=webwx";

    String url_check_login="https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login?uuid={}&tip=1&_={t}";

    String url_init = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxinit?r={t}";

    String url_contactlist = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?r={t}";

    String url_synccheck = "https://webpush.weixin.qq.com/cgi-bin/mmwebwx-bin/synccheck?callback=jQuery18309326978388708085_1377482079946&r={t}&sid={0}&uin={1}&deviceid={2}&synckey={3}&_={t}";

    String url_msglist = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsync?sid={}&r={t}";

    String url_sendMsg = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg?sid={}&r={t}";
    public static void main(String[] args) throws Exception{
        new WeiXinConnection().init();
//      new Test().getCookies("http://www.baidu.com");
//      System.out.println(System.currentTimeMillis(););
    }

    class UInfo{
        String wxuin;
        String wxsid;
        String webwxuvid;
        String deviceid;

        String syncKey;
        JSONObject SyncKey;

        String SKey;

        JSONObject ujson;
        String name;   //帐号
        String nickName;//昵称

        boolean connect = false;
    }
    UInfo u = new UInfo();
    Object wait = new Object();

    //TODO
    /*LinkedList<WxMsgListener> listeners = new LinkedList<WxMsgListener>();
    public void addListener(WxMsgListener lis){
        this.listeners.add(lis);
    }*/
    //开始登录微信
    public void start(){
        //1.取得uuid
        String getUUIDUrl = url_getuuid.replace("{t}", ""+System.currentTimeMillis());
        String res = Utils.get(getUUIDUrl);

        String uuid = "";
        Pattern pat = Pattern.compile("window.QRLogin.code = ([0-9]+); window.QRLogin.uuid = \"([0-9a-z]+)\";");
        Matcher mat = pat.matcher(res);
        if(mat.find()){
            System.out.println(mat.group(1));
            System.out.println(mat.group(2));
            uuid = mat.group(2);
        }
        //2.取得登录二维码
        String url_login = url_login2weima.replace("{}", uuid);
        System.out.println("login_url=>"+url_login);
        String img_path = Utils.dowmImg(url_login);
        System.out.println("img_path=>"+img_path);
        //3.轮询手机端是否已经扫描二维码并确认在Web端登录
        String login_url = check_login(uuid);
        Map<String, String> cookies = Utils.getCookies(login_url);
        System.out.println("cookies = "+cookies);
        u.wxuin = cookies.get("wxuin");
        u.wxsid = cookies.get("wxsid");
        u.webwxuvid = cookies.get("webwxuvid");
        u.deviceid = "e960817075982756";

        //4.调用初始化url
        JSONObject post = new JSONObject();
        JSONObject BaseRequest = new JSONObject();
        post.put("BaseRequest", BaseRequest);
        BaseRequest.put("Uin", u.wxuin);
        BaseRequest.put("Sid", u.wxsid);
        BaseRequest.put("Skey", "");
        BaseRequest.put("DeviceID", u.deviceid);
        String intUrl = url_init.replace("{t}", ""+System.currentTimeMillis());
        res = Utils.postString(intUrl, post.toString());
        JSONObject init =  JSONObject.parseObject(res);
        JSONObject user  = init.getJSONObject("User");
        System.out.println("欢迎您："+user);
        u.ujson = user;
        u.name = user.getString("UserName");
        u.nickName = user.getString("NickName");
        JSONObject SyncKey = init.getJSONObject("SyncKey");
        u.syncKey = getSyncKey(init);
        u.SyncKey = SyncKey;

        u.SKey = init.getString("SKey");
        u.connect = true;

        startSyncCheck();

        u.connect = false;
        //               System.out.println(res);

    }

    public void init() throws Exception{
        while(true){
            Thread t =  new Thread(){
                public void run(){
                    try {
                        WeiXinConnection.this.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        u.connect = false;
                    }
                    synchronized (wait) {
                        wait.notify();
                    }
                }
            };
            t.start();
            Thread.sleep(300 * 1000);
            if(!u.connect){
                t.interrupt();
            }else{
                synchronized (wait) {
                    wait.wait();
                }
            }
        }
    }

    //取得synckey
    public String getSyncKey(JSONObject json){
        JSONObject SyncKey = json.getJSONObject("SyncKey");
        JSONArray Listarr = SyncKey.getJSONArray("List");
        String synckey = "";
        for(int i = 0;i<Listarr.size(); i++){
            JSONObject jj = Listarr.getJSONObject(i);
            synckey = synckey + jj.get("Key")+"_"+jj.get("Val")+"|"  ;
        }
        synckey = synckey.substring(0, synckey.length() - 2);
        return synckey;
    }

    //check_login
    public String check_login(String uuid){
        String url_check = url_check_login.replace("{}", uuid);

        url_check = url_check.replace("{t}", ""+System.currentTimeMillis());
        int c = 0;
        String login_url = null;
        while(login_url == null){
            String res = Utils.get(url_check);
            Pattern pat = Pattern.compile("window.redirect_uri=\"(.+)\";");
            Matcher mat = pat.matcher(res);
            if(mat.find()){
                System.out.println(mat.group(1));
                login_url = mat.group(1);
            }
            c++;
        }
        System.out.println("经历了"+c+"次轮询");
        return login_url;
    }
    //循环检查新消息
    public void startSyncCheck(){
        while(true){
            String url = url_synccheck.replace("{0}", u.wxsid);
            url = url.replace("{1}", u.wxuin);
            url = url.replace("{2}", u.deviceid);
            url = url.replace("{3}", u.syncKey);
            url = url.replaceAll("[|]", "%7C");
            long t = System.currentTimeMillis();
            url = url.replace("{t}", ""+t);
            url = url.replace("{t}", ""+t);
            String res = Utils.get(url);
            System.out.println("sync back="+res);
            Pattern pat = Pattern.compile("window.synccheck=\\{retcode:\"([0-9]+)\",selector:\"([0-9]+)\"}");
            Matcher mat = pat.matcher(res);
            if(mat.find()){
                int retcode = Integer.valueOf(mat.group(1));
                if(retcode != 0){
                    System.out.println(url);
                    System.out.println("退出登录"+res);
                    break;
                }
                int count = Integer.valueOf(mat.group(2));
//              System.out.println(mat.group(2));
                if(count > 0){
//                  System.out.println("有"+count+"条新消息");

                    String msgurl = url_msglist.replace("{}", u.wxsid);
                    msgurl = msgurl.replace("{t}", ""+t);

                    JSONObject post = new JSONObject();
                    JSONObject BaseRequest = new JSONObject();
                    BaseRequest.put("Uin", u.wxuin);
                    BaseRequest.put("Sid", u.wxsid);
                    post.put("BaseRequest", BaseRequest);
                    post.put("SyncKey", u.SyncKey);
                    post.put("rr", System.currentTimeMillis());

                    res = Utils.postString(msgurl, post.toString());

//                  System.out.println(res);

                    JSONObject rj =   JSONObject.parseObject(res);
                    u.SKey = rj.getString("SKey");
                    if(u.SKey.equals("")){

                        break;
                    }
                    JSONObject SyncKey = rj.getJSONObject("SyncKey");
                    u.syncKey = getSyncKey(rj);
                    u.SyncKey = SyncKey;


                    JSONArray AddMsgList = rj.getJSONArray("AddMsgList");
                    for(int i = 0;i<AddMsgList.size(); i ++ ){
                        JSONObject msg = AddMsgList.getJSONObject(i);
                        long msgid = msg.getLong("MsgId");
                        String fname = msg.getString("FromUserName");
                        String tname = msg.getString("ToUserName");
                        String cont = msg.getString("Content");
                        if(tname.equals(u.name)){
                            System.out.println("新消息："+fname+":"+cont);
//                          sendMsg(fname, "1");
                            //遍历
                            //TODO
                            /*for(WxMsgListener l : this.listeners){
                                if(l.isEqualToMsg(fname, cont)){
                                    l.exec(fname, cont);
                                }
                            }*/
                        }
                    }
//                  System.out.println(res);
                }
            }
        }
    }
    //发送消息
    public boolean sendMsg(String toname, String cont){
        String url = url_sendMsg.replace("{}", u.wxsid);
        url = url.replace("{t}", ""+ System.currentTimeMillis());
        JSONObject post = new JSONObject();
        JSONObject BaseRequest = new JSONObject();
        BaseRequest.put("Uin", u.wxuin);
        BaseRequest.put("Sid", u.wxsid);
        BaseRequest.put("DeviceID", u.deviceid);
        BaseRequest.put("Skey", u.SKey);
        post.put("BaseRequest", BaseRequest);
        JSONObject Msg = new JSONObject();
        Msg.put("ClientMsgId", System.currentTimeMillis());
        Msg.put("Content", cont);
        Msg.put("FromUserName", u.name);
        Msg.put("LocalID", System.currentTimeMillis());
        Msg.put("ToUserName", toname);
        Msg.put("Type",  1);
        post.put("Msg", Msg);

        String res = Utils.postString(url, post.toString());
        if(res != null){
            JSONObject r =   JSONObject.parseObject(res);
            if(r.getJSONObject("BaseResponse").getInteger("Ret") == 0){
                System.out.println("发送成功");
                return true;
            }else{
                System.out.println("发送失败");
            }
        }else{
            //多次测试此时是发送成功的
        }
        System.out.println(res);
        return false;
    }
}
