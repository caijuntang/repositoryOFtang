package com.cooling.hydraulic.service;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.config.BaseConfig;
import com.cooling.hydraulic.consts.BaseConst;
import com.cooling.hydraulic.requestDto.YsRequest;
import com.cooling.hydraulic.consts.ResponseConst;
import com.cooling.hydraulic.response.YsResponse;
import com.cooling.hydraulic.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VideoService {
    private final static Logger log = LoggerFactory.getLogger(VideoService.class);

    private static final String YS_URL = "https://open.ys7.com/api/lapp/token/get";
    private static Map<String, String> tokenMap = new ConcurrentHashMap<>();

    @Scheduled(cron = "0 0 0 0/3 * ? ")
    public void getYsTokenSchedule() {
        log.info("================萤石token更新启动==================");
        YsRequest ysRequest = new YsRequest(BaseConfig.ysKey, BaseConfig.ysSecret);
        String param = JSON.toJSONString(ysRequest);
        String resp = null;
        try {
            resp = HttpClientUtil.postMethod(YS_URL, param);

        } catch (IOException e) {
            log.error("============萤石token获取异常=======", e);
        }
        if (null == resp) {
            log.error("============萤石token获取异常=======");
        } else {
            YsResponse ysResponse = JSON.parseObject(resp, YsResponse.class);
            if (!ResponseConst.SUCCESS.equals(ysResponse.getCode())) {
                return;
            }
            String accessToken = ysResponse.getData().getAccessToken();
            if (StringUtils.hasText(accessToken)) {
                tokenMap.put(BaseConfig.ysKey + BaseConst.DASH + BaseConfig.ysSecret, accessToken);
            }
        }

    }

    public String getYsToken(String appKey,String appSecret){
        if(StringUtils.isEmpty(appKey)||StringUtils.isEmpty(appSecret)){
            return null;
        }
        String key=appKey+BaseConst.DASH+appSecret;
        String token = tokenMap.get(key);
        return token;
    }

}
