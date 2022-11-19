package com.cooling.hydraulic.utils;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.config.WCCPConfig;
import com.cooling.hydraulic.requestDto.Text;
import com.cooling.hydraulic.requestDto.qyRequest.QYMsgRequest;
import com.cooling.hydraulic.response.wechat.WCAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WeChatSendUtil {


    private static Logger log = LoggerFactory.getLogger(WeChatSendUtil.class);

    //企业微信
    private static final String WCCPSENDMESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
    private static final String WCCPGETTOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

    public static void sendWCCPTextMsg(String content) {
        String token = getWCCPToKen(WCCPConfig.corpId, WCCPConfig.agentSecret);
        if(null==token){
            log.info("未获取到token");
            return;
        }
        QYMsgRequest messageRequest = new QYMsgRequest();
        messageRequest.setAgentid(WCCPConfig.agentId);
        messageRequest.setMsgtype("text");
        messageRequest.setToparty("2");
        com.cooling.hydraulic.requestDto.Text text = new Text();
        text.setContent(content);
        messageRequest.setText(text);
        messageRequest.setSafe(0);
        String requestStr = JSON.toJSONString(messageRequest);
        try {
            String resp = HttpClientUtil.postMethod(WCCPSENDMESSAGE_URL + token, requestStr);
            log.info(resp);
        } catch (IOException e) {
            log.error("企业微信消息发送错误",e);
        }
    }


    public static String getWCCPToKen(String corpid, String corpsecret) {
        String toKenBody=null;
        try {
             toKenBody = HttpClientUtil.getMethod(WCCPGETTOKEN_URL+"?corpid=" + corpid + "&corpsecret=" + corpsecret);
        } catch (Exception e) {
                log.error(e.getLocalizedMessage());
        }
        if(null==toKenBody){
            return null;
        }
        WCAccessToken token = JSON.parseObject(toKenBody, WCAccessToken.class);
        if(!"0".equals(token.getErrcode())){
            return null;
        }
        return token.getAccessToken();
    }
}