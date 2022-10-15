package com.cooling.hydraulic.component;

import com.cooling.hydraulic.config.BaseConfig;
import com.cooling.hydraulic.service.StationService;
import com.cooling.hydraulic.service.WaterLineService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Component
public class TextMsgHandler implements WxMpMessageHandler {
    private final static Logger log = LoggerFactory.getLogger(TextMsgHandler.class);

    @Resource
    private WaterLineService waterLineService;
    @Resource
    private StationService stationService;


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        //判断传递过来的消息，类型是否为TEXT
        if (wxMessage.getMsgType().equals(WxConsts.XmlMsgType.TEXT)) {
            // 获取微信用户基本信息
            WxMpUser userWxInfo = wxMpService.getUserService().userInfo(wxMessage.getFromUser(), "zh_CN");
            if (null != userWxInfo){
                String content = wxMessage.getContent();
                String waterLineContent="暂无泵站水位信息";
                Integer stationId=0;
                if(StringUtils.isNotEmpty(content)){
                    int index=0;
                    List<String> stationNameList = BaseConfig.stationNameList;
                    int length = stationNameList.size();
                    while(index<length){
                        if(content.contains(stationNameList.get(index))){
                            stationId=++index;
                            break;
                        }
                        index++;
                    }
                    if (stationId!=0) {
                        waterLineContent = waterLineService.getWaterLineContent(stationId);
                    }
                }
                return WxMpXmlOutMessage.TEXT().content(waterLineContent)
                        .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                        .build();
            }
        }
        return null;
    }
}
