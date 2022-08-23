package com.cooling.hydraulic.service;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.config.WCConfig;
import com.cooling.hydraulic.dao.WXUserRepository;
import com.cooling.hydraulic.entity.WXUser;
import com.cooling.hydraulic.request.MessageRequest;
import com.cooling.hydraulic.request.Text;
import com.cooling.hydraulic.response.AccessToken;
import com.cooling.hydraulic.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WXService {

    private final static Logger log = LoggerFactory.getLogger(WXService.class);

    //公众号
    private static final String WCSENDMSG_URL="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token={token}";
    private static final String WCTOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}";

    private static  Map<String, String> tokenMap=new ConcurrentHashMap<>();
    @Resource
    private WXUserRepository wxUserRepository;

    @PostConstruct
    private void init(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                String wcToKen = getWCToKen();
                if(StringUtils.hasText(wcToKen)){
                    String appId = WCConfig.appId;
                    tokenMap.put(appId,wcToKen);
                    log.info(appId+"的token更新成功："+wcToKen);
                }
            }
        };
        // 计时器
        Timer timer = new Timer();
        // 添加执行任务（延迟 1s 执行，每 3s 执行一次）
        timer.schedule(timerTask, 5000, 6000000);
    }

    public List<WXUser> getWXUserList() {
        return wxUserRepository.findAll();
    }

    public void sendWCTextMsg(String content,String reciver){
        String wcToKen = tokenMap.get(WCConfig.appId);
        if(null==wcToKen){
            log.info("未获取到token");
            return;
        }
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMsgtype("text");
        messageRequest.setTouser(reciver);
        Text text = new Text();
        text.setContent(content);
        messageRequest.setText(text);
        String requestStr = JSON.toJSONString(messageRequest);
        try {
            String resp = HttpClientUtils.postMethod(WCSENDMSG_URL.replace("{token}",wcToKen), requestStr);
            log.info(resp);
        } catch (IOException e) {
            log.error("公众号消息发送出错",e);
        }
    }

    public String getWCToKen() {
        String toKenBody=null;
        try {
            String url = WCTOKEN_URL.replace("{appid}", WCConfig.appId).replace("{secret}", WCConfig.appSecret);
            toKenBody = HttpClientUtils.getMethod(url);
        } catch (Exception e) {
            log.error("获取token出错",e);
        }
        if(null==toKenBody){
            return null;
        }
        AccessToken token = JSON.parseObject(toKenBody, AccessToken.class);
        return token.getAccessToken();
    }

}
