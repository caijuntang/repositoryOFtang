package com.cooling.hydraulic.component;

import com.cooling.hydraulic.service.WaterLineService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class MenuActionHandler implements WxMpMessageHandler {

    @Resource
    private WaterLineService waterLineService;


    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws  WxErrorException {
        if (wxMessage.getMsgType().equals(WxConsts.XmlMsgType.EVENT)) {
            // 获取微信用户基本信息
            WxMpUser userWxInfo = wxMpService.getUserService().userInfo(wxMessage.getFromUser(), "zh_CN");
            if (null != userWxInfo){
                String eventKey = wxMessage.getEventKey();
                Integer stationId=0;
                if(null!=eventKey){
                    stationId= Integer.parseInt(eventKey);
                }
                String waterLineContent="暂无泵站水位信息！";
                if(stationId!=0){
                     waterLineContent = waterLineService.getWaterLineContent(stationId);
                }
                return WxMpXmlOutMessage.TEXT().content(waterLineContent)
                        .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                        .build();
            }
        }
        return null;
    }
}
