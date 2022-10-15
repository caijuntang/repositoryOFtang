package com.cooling.hydraulic.waterLine;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.model.WXTemplateMsg;
import com.cooling.hydraulic.response.AccessToken;
import com.cooling.hydraulic.utils.DateTimeUtil;
import com.cooling.hydraulic.utils.WeChatSendUtil;
import com.cooling.hydraulic.utils.HttpClientUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SendMessageTest {

    //    @Test
    public void getTokenTest() {
        String token = WeChatSendUtil.getWCCPToKen("wwa1d74de552322ccc", "y3Xn3XE1C6dZuOj3e4G-UoRdzfi6S3FV558iz3lIsh4");
        System.out.println("+++++++++++++++" + token + "+++++++++++++++++");
    }

    //    @Test
    public void sendMessage() {
        String token = "87QmEc1tbwWtL945jqFpExZw1yUtwLjyjrGVjkjdh5uBPuTzLOuIRY2cmT17ai4mMZjHaQpSADLziDj2ezd1MqCMfaI2hzdtdLM3HQZhILTUnSs0O1zpbcvEemWBGNkOUXktcJGMJXte1ve223g4Hcezzq9-NJ8aUhsaLQbP7CTIu-bII2SXhE67FCvML5d4933ad-asP3duekdok2-XyA";
        WeChatSendUtil.sendWCCPTextMsg("测试消息,微信查看即可");
    }


    @Test
    public void sengWXMsg() {
        String token = getWXToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
//        String msg="{ \"touser\":\"odynm5sGxHh5-DA3JJorKzTez9P0\", \"msgtype\":\"text\", \"text\": { \"content\":\"【泵站水位告警】\\r\\n泵站名称：敬亭圩泵站\\r\\n外河水位：6.89米\\r\\n前池水位：6.39米\\r\\n内河水位：6.41米\\r\\n告警内容：泵站当前内河水位6.41米，已超警戒水位线（6.00米），请及时处置！\" } }";
        String msg = "{ \"touser\":[\"odynm5qCoBRwqNRkZRv2EJeDn_JE\",\"odynm5sGxHh5-DA3JJorKzTez9P0\"], \"msgtype\":\"text\", \"text\": { \"content\":\"【泵站水位告警】\\r\\n泵站名称：敬亭圩泵站\\r\\n外河水位：6.89米\\r\\n前池水位：6.39米\\r\\n内河水位：6.41米\\r\\n告警内容：泵站当前内河水位6.41米，已超警戒水位线（6.00米），请及时处置！\" } }";
        try {
            String resp = HttpClientUtil.postMethod(url, msg);
            System.out.printf(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sengWXTemplateMsg() {
        String token = getWXToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token;
        //消息模版
        TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
        //根据具体模板参数组装
//        params.put("first", this.item("", "#000000"));
        params.put("keyword1", this.item("敬亭圩泵站水位告警", "#000000"));
        params.put("keyword2", this.item(DateTimeUtil.getNowDateTimeString(), "#000000"));
        params.put("keyword3", this.item("该站内河水位现到达6.44米，已超警戒水位（6米）！", "#000000"));
        params.put("remark", this.item("请及时处置并持续观察！", "#000000"));
        WXTemplateMsg msg = new WXTemplateMsg();
        msg.setTemplate_id("LSvU_UPK6_JdWIi7dAlMU5BF6ALiP2Gh94UWG5muGgA");
//        msg.setTouser("odynm5qCoBRwqNRkZRv2EJeDn_JE");
        msg.setTouser("odynm5sGxHh5-DA3JJorKzTez9P0");
        msg.setData(params);
        String msgContent = JSON.toJSONString(msg);
        System.out.printf(msgContent);
        try {
            String resp = HttpClientUtil.postMethod(url, msgContent);
            System.out.printf(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sengWXEvent() {
        String url = "http://localhost:8090/screen/wx/handleWxEvent";
//        String actionRequest = "<xml><ToUserName><![CDATA[gh_a26585648753]]></ToUserName>" +
//                "<FromUserName><![CDATA[odynm5qCoBRwqNRkZRv2EJeDn_JE]]></FromUserName>" +
//                "<CreateTime>1661741520</CreateTime>" +
//                "<MsgType><![CDATA[event]]></MsgType>" +
//                "<Event><![CDATA[CLICK]]></Event>" +
//                "<EventKey><![CDATA[1]]></EventKey>" +
//                "</xml>";
        String request = "<xml><ToUserName><![CDATA[gh_a26585648753]]></ToUserName>" +
                "<FromUserName><![CDATA[odynm5qCoBRwqNRkZRv2EJeDn_JE]]></FromUserName>" +
                "<CreateTime>1661741520</CreateTime>" +
                "<MsgType><![CDATA[text]]></MsgType>" +
                "<Content><![CDATA[敬亭圩]]></Content>" +
                "<MsgId>23790484195379929</MsgId>" +
                "</xml>";
        try {
            String resp = HttpClientUtil.postMethod(url, request, "application/xml; charset=UTF-8");
            System.out.printf(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserList() {
        String token = "60_LNyEhkdqV63BBKG_E-P6XsxdQzLci7UI1r5gaYYH-ONE5A4rzDEA6fB9i0UpQl4ZiTn6PDFxQoDzRUBfTkNw62CZW63RMFHNFbf1_alxE28wvt6nbVTVQbP5cTFJCiKcs1BNYjU_kCFGMzHmESWhACAYMQ";
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + token;
        try {
            String resp = HttpClientUtil.getMethod(url);
            System.out.println(resp);
            //{"total":2,"count":2,"data":{"openid":["odynm5qCoBRwqNRkZRv2EJeDn_JE","odynm5sGxHh5-DA3JJorKzTez9P0"]},"next_openid":"odynm5sGxHh5-DA3JJorKzTez9P0"}
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void setMenu() {
        String token = getWXToken();
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token;
        try {
            String param = "{ \"button\":[{\"name\":\"水位详情\",\"sub_button\":[{ \"type\":\"click\",\"name\":\"敬亭圩泵站\", \"key\":\"1\" },{ \"type\":\"click\",\"name\":\"龙窝湖泵站\", \"key\":\"2\" }]}]}";
            String resp = HttpClientUtil.postMethod(url, param);
            System.out.println(resp);
            //{"total":2,"count":2,"data":{"openid":["odynm5qCoBRwqNRkZRv2EJeDn_JE","odynm5sGxHh5-DA3JJorKzTez9P0"]},"next_openid":"odynm5sGxHh5-DA3JJorKzTez9P0"}
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String getWXToken() {
        try {
            String tokenStr = HttpClientUtil.getMethod("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx9a636b2936416367&secret=74a4936aa3e72a99ba2570a2f5fc282a");
            AccessToken token = JSON.parseObject(tokenStr, AccessToken.class);
            return token.getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Test
    public void clearQuota() {
        String url = "https://api.weixin.qq.com/cgi-bin/clear_quota?access_token=" + getWXToken();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("appid", "wx9a636b2936416367");
            String resp = HttpClientUtil.postMethod(url, param);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public TreeMap<String, String> item(String value, String color) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("value", value);
        params.put("color", color);
        return params;
    }
}
