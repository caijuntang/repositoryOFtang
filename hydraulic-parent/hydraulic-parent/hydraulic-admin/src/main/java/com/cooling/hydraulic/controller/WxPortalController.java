package com.cooling.hydraulic.controller;

import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

@Controller
@RequestMapping("/wx/handleWxEvent")
public class WxPortalController {
    private final static Logger log = LoggerFactory.getLogger(WxPortalController.class);
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpMessageRouter messageRouter;


    @GetMapping
    @ResponseBody
    public String get(@RequestParam(name = "signature", required = false) String signature,
                                @RequestParam(name = "timestamp", required = false) String timestamp,
                                @RequestParam(name = "nonce", required = false) String nonce,
                                @RequestParam(name = "echostr", required = false) String echostr) {
        log.info("\n接收微信请求：signature=[{}], echostr=[{}], timestamp=[{}], nonce=[{}] ",signature,echostr, timestamp, nonce);
        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }else {
            return "";
        }
    }

    @PostMapping(produces = {"application/xml; charset=UTF-8"})
    @ResponseBody
    public String post(@RequestBody String requestBody,
                                @RequestParam(name = "signature", required = false) String signature,
                                @RequestParam(name = "timestamp", required = false) String timestamp,
                                @RequestParam(name = "nonce", required = false) String nonce,
                                @RequestParam(name = "openid", required = false) String openid,
                                @RequestParam(name = "encrypt_type", required = false) String encType,
                                @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        String requestContent = URLDecoder.decode(requestBody);
        log.info("\n接收微信请求：openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[{}] ",
                openid, signature, encType, msgSignature, timestamp, nonce, requestContent);
//        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
//            log.error("签名校验失败");
//            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
//        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            try {
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestContent);
                WxMpXmlOutMessage outMessage = this.route(inMessage);
                if (outMessage == null) {
                    return "";
                }

                out = outMessage.toXml();
            } catch (Exception e) {
                log.error("请求解析出错",e);
            }
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxMpService.getWxMpConfigStorage(),
                    timestamp, nonce, msgSignature);
            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toEncryptedXml(wxMpService.getWxMpConfigStorage());
        }

        log.debug("\n组装回复信息：{}", out);
        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return messageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }

        return null;
    }

}
