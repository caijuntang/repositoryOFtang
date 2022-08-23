package com.cooling.hydraulic.waterLine;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.response.AccessToken;
import com.cooling.hydraulic.utils.WeChatSendUtil;
import com.cooling.hydraulic.utils.HttpClientUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SendMessageTest {

//    @Test
    public void getTokenTest(){
        String token = WeChatSendUtil.getWCCPToKen("wwa1d74de552322ccc", "y3Xn3XE1C6dZuOj3e4G-UoRdzfi6S3FV558iz3lIsh4");
        System.out.println("+++++++++++++++"+token+"+++++++++++++++++");
    }

//    @Test
    public void sendMessage(){
        String token="87QmEc1tbwWtL945jqFpExZw1yUtwLjyjrGVjkjdh5uBPuTzLOuIRY2cmT17ai4mMZjHaQpSADLziDj2ezd1MqCMfaI2hzdtdLM3HQZhILTUnSs0O1zpbcvEemWBGNkOUXktcJGMJXte1ve223g4Hcezzq9-NJ8aUhsaLQbP7CTIu-bII2SXhE67FCvML5d4933ad-asP3duekdok2-XyA";
        WeChatSendUtil.sendWCCPTextMsg("测试消息,微信查看即可");
    }


    @Test
    public void sengWXMsg(){
        String token=getWXToken();
        String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+token;
//        String msg="{ \"touser\":\"odynm5sGxHh5-DA3JJorKzTez9P0\", \"msgtype\":\"text\", \"text\": { \"content\":\"【泵站水位告警】\\r\\n泵站名称：敬亭圩泵站\\r\\n外河水位：6.89米\\r\\n前池水位：6.39米\\r\\n内河水位：6.41米\\r\\n告警内容：泵站当前内河水位6.41米，已超警戒水位线（6.00米），请及时处置！\" } }";
        String msg="{ \"touser\":[\"odynm5qCoBRwqNRkZRv2EJeDn_JE\",\"odynm5sGxHh5-DA3JJorKzTez9P0\"], \"msgtype\":\"text\", \"text\": { \"content\":\"【泵站水位告警】\\r\\n泵站名称：敬亭圩泵站\\r\\n外河水位：6.89米\\r\\n前池水位：6.39米\\r\\n内河水位：6.41米\\r\\n告警内容：泵站当前内河水位6.41米，已超警戒水位线（6.00米），请及时处置！\" } }";
        try {
            String resp = HttpClientUtils.postMethod(url, msg);
            System.out.printf(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserList(){
        String token="60_LNyEhkdqV63BBKG_E-P6XsxdQzLci7UI1r5gaYYH-ONE5A4rzDEA6fB9i0UpQl4ZiTn6PDFxQoDzRUBfTkNw62CZW63RMFHNFbf1_alxE28wvt6nbVTVQbP5cTFJCiKcs1BNYjU_kCFGMzHmESWhACAYMQ";
        String url="https://api.weixin.qq.com/cgi-bin/user/get?access_token="+token;
        try {
            String resp = HttpClientUtils.getMethod(url);
            System.out.println(resp);
            //{"total":2,"count":2,"data":{"openid":["odynm5qCoBRwqNRkZRv2EJeDn_JE","odynm5sGxHh5-DA3JJorKzTez9P0"]},"next_openid":"odynm5sGxHh5-DA3JJorKzTez9P0"}
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String getWXToken(){
        try {
            String tokenStr = HttpClientUtils.getMethod("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx9a636b2936416367&secret=74a4936aa3e72a99ba2570a2f5fc282a");
            AccessToken token = JSON.parseObject(tokenStr, AccessToken.class);
            return token.getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Test
    public void clearQuota(){
        String url="https://api.weixin.qq.com/cgi-bin/clear_quota?access_token="+getWXToken();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("appid","wx9a636b2936416367");
            String resp = HttpClientUtils.postMethod(url,param);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
