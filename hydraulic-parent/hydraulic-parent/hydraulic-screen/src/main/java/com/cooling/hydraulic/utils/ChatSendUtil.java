package com.cooling.hydraulic.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cooling.hydraulic.config.WCCPConfig;
import com.cooling.hydraulic.request.MessageRequest;
import com.cooling.hydraulic.request.Text;
import com.cooling.hydraulic.response.AccessToken;
import com.cooling.hydraulic.response.WCBaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ChatSendUtil {


    private static Logger log = LoggerFactory.getLogger(ChatSendUtil.class);

    private static final String SENDMESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
    private static final String GETTOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

    public static void sendTextMsg(String content) {
        String token = getToKen(WCCPConfig.corpId, WCCPConfig.agentSecret);
//        String token="87QmEc1tbwWtL945jqFpExZw1yUtwLjyjrGVjkjdh5uBPuTzLOuIRY2cmT17ai4mMZjHaQpSADLziDj2ezd1MqCMfaI2hzdtdLM3HQZhILTUnSs0O1zpbcvEemWBGNkOUXktcJGMJXte1ve223g4Hcezzq9-NJ8aUhsaLQbP7CTIu-bII2SXhE67FCvML5d4933ad-asP3duekdok2-XyA";
        if(null==token){
            return;
        }
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setAgentid(WCCPConfig.agentId);
        messageRequest.setMsgtype("text");
//        messageRequest.setToparty("2");
        messageRequest.setTouser("ZhangZhiDong");
        Text text = new Text();
        text.setContent(content);
        messageRequest.setText(text);
        messageRequest.setSafe(0);
        String requestStr = JSON.toJSONString(messageRequest);
        try {
            String resp = HttpClientUtils.postMethod(SENDMESSAGE_URL + token, requestStr);
            WCBaseResponse wcBaseResponse = JSON.parseObject(resp, WCBaseResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.toString());
        }
    }


    public static String getToKen(String corpid, String corpsecret) {
        String toKenBody=null;
        try {
             toKenBody = HttpClientUtils.getMethod(GETTOKEN_URL+"?corpid=" + corpid + "&corpsecret=" + corpsecret);
        } catch (Exception e) {
                log.error(e.getLocalizedMessage());
        }
        if(null==toKenBody){
            return null;
        }
        AccessToken token = JSON.parseObject(toKenBody, AccessToken.class);
        if(!"0".equals(token.getErrcode())){
            return null;
        }
        return token.getAccessToken();
    }
}
