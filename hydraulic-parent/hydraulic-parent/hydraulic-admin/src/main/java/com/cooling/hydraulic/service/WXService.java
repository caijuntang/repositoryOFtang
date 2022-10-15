package com.cooling.hydraulic.service;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.component.MenuActionHandler;
import com.cooling.hydraulic.component.NullHandler;
import com.cooling.hydraulic.component.TextMsgHandler;
import com.cooling.hydraulic.config.WCConfig;
import com.cooling.hydraulic.dao.WXUserRepository;
import com.cooling.hydraulic.entity.WXUser;
import com.cooling.hydraulic.requestDto.MessageRequest;
import com.cooling.hydraulic.requestDto.Text;
import com.cooling.hydraulic.response.AccessToken;
import com.cooling.hydraulic.utils.HttpClientUtil;
import com.google.common.collect.Maps;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
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

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.TEXT;


@Service
public class WXService {

    private final static Logger log = LoggerFactory.getLogger(WXService.class);

    //公众号
    private static final String WCSENDMSG_URL="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token={token}";
    private static final String WCSENDTEMPLATEMSG_URL="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={token}";
    private static final String WCTOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}";

    private static  Map<String, String> tokenMap=new ConcurrentHashMap<>();

    @Resource
    private NullHandler nullHandler;
    @Resource
    private MenuActionHandler menuActionHandler;
    @Resource
    private TextMsgHandler textMsgHandler;
    @Resource
    private WXUserRepository wxUserRepository;

    @PostConstruct
    private void init(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
//                String wcToKen = getWCToKen();
//                if(StringUtils.hasText(wcToKen)){
//                    String appId = WCConfig.appId;
//                    tokenMap.put(appId,wcToKen);
//                    log.info(appId+"的token更新成功："+wcToKen);
//                }
            }
        };
        // 计时器
        Timer timer = new Timer();
        // 添加执行任务（延迟 10s 执行，每 3s 执行一次）
        timer.schedule(timerTask, 10000, 900000);
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
            String resp = HttpClientUtil.postMethod(WCSENDMSG_URL.replace("{token}",wcToKen), requestStr);
            log.info(resp);
        } catch (IOException e) {
            log.error("公众号消息发送出错",e);
        }
    }

    public String sendWCTemlateMsg(String content){
        String wcToKen = tokenMap.get(WCConfig.appId);
        String resp="";
        if(null==wcToKen){
            log.info("未获取到token");
            return resp;
        }
        try {
             resp = HttpClientUtil.postMethod(WCSENDTEMPLATEMSG_URL.replace("{token}",wcToKen), content);
            log.info(resp);
        } catch (IOException e) {
            log.error("公众号消息发送出错",e);
        }
        return resp;
    }

    public String getWCToKen() {
        String toKenBody=null;
        try {
            String url = WCTOKEN_URL.replace("{appid}", WCConfig.appId).replace("{secret}", WCConfig.appSecret);
            toKenBody = HttpClientUtil.getMethod(url);
        } catch (Exception e) {
            log.error("获取token出错",e);
        }
        if(null==toKenBody){
            return null;
        }
        AccessToken token = JSON.parseObject(toKenBody, AccessToken.class);
        return token.getAccessToken();
    }

    @Bean("WXMpService")
    public WxMpService wxMpService() {
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(WCConfig.appId);
        wxMpDefaultConfig.setSecret(WCConfig.appSecret);
        wxMpDefaultConfig.setToken(WCConfig.token);
        wxMpDefaultConfig.setAesKey(WCConfig.aesKey);
        Map<String, WxMpConfigStorage> configMap = Maps.newHashMap();
        configMap.put(WCConfig.appId,wxMpDefaultConfig);
        WxMpService service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configMap);
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.CLICK).handler(menuActionHandler).end();
        newRouter.rule().async(false).msgType(TEXT).handler(textMsgHandler).end();

        // 默认
        newRouter.rule().async(false).handler(nullHandler).end();

        return newRouter;
    }

}
