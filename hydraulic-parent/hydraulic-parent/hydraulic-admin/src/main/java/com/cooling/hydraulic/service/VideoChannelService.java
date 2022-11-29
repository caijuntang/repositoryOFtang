package com.cooling.hydraulic.service;


import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.config.BaseConfig;
import com.cooling.hydraulic.consts.BaseConst;
import com.cooling.hydraulic.consts.ResponseConst;
import com.cooling.hydraulic.dao.VideoChannelRepository;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.entity.VideoChannel;
import com.cooling.hydraulic.model.VideoChannelForm;
import com.cooling.hydraulic.requestDto.YsRequest;
import com.cooling.hydraulic.response.YsResponse;
import com.cooling.hydraulic.utils.DateTimeUtil;
import com.cooling.hydraulic.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VideoChannelService {

    private final static Logger log = LoggerFactory.getLogger(VideoChannelService.class);
    private static final String YS_URL = "https://open.ys7.com/api/lapp/token/get";
    private static Map<String, String> tokenMap = new ConcurrentHashMap<>();


    @Resource
    private StationService stationService;
    @Resource
    private VideoChannelRepository videoChannelRepository;

    @PostConstruct
    private void init(){
        this.getYsTokenTask();
    }



    @Transactional
    public boolean save(VideoChannelForm form){
        if(null==form){
            return false;
        }
        try {
            Integer id = form.getId();
            if(null==id){
                VideoChannel channel = this.formToEntity(form);
                videoChannelRepository.save(channel);
            }else{
                VideoChannel oldChannel = videoChannelRepository.getOne(id);
                oldChannel.setChannel(form.getChannel());
                oldChannel.setIsLive(form.getIsLive());
                oldChannel.setSerialNo(form.getSerialNo());
                oldChannel.setVideoName(form.getVideoName());
                oldChannel.setStatus(form.getStatus());
                videoChannelRepository.save(oldChannel);
            }
        } catch (Exception e) {
            log.error("视频监控渠道保存失败！",e);
            return false;
        }
        return true;
    }


    public Object findVideoByStationId(Integer stationId){
        Station station = new Station();
        station.setId(stationId);
        List<VideoChannel> channels = videoChannelRepository.findByStationIdAndStatus(stationId,1);
        Map<String, Object> dataMap = new HashMap<String,Object>();
        List<VideoChannel> liveVideo = new ArrayList<>();
        if(null!=channels&&!channels.isEmpty()){
            for(VideoChannel c:channels){
                int isLive = c.getIsLive();
                if(isLive==1){
                    liveVideo.add(c);
                }
            }
        }
        String token = this.getYsToken(BaseConfig.ysKey,BaseConfig.ysSecret);
        dataMap.put("token",token);
        dataMap.put("live",liveVideo);
        dataMap.put("preview",channels);
        return dataMap;
    }

    private VideoChannel formToEntity(VideoChannelForm form) {
        VideoChannel channel = new VideoChannel();
        channel.setChannel(form.getChannel());
        channel.setIsLive(form.getIsLive());
        channel.setSerialNo(form.getSerialNo());
        channel.setVideoName(form.getVideoName());
        channel.setStatus(form.getStatus());
        Integer stationId = form.getStationId();
        Station station = stationService.getOne(stationId);
        channel.setStation(station);
        channel.setCreateTime(DateTimeUtil.getNowDateTime());
        return channel;
    }


    private VideoChannelForm entityToForm(VideoChannel channel) {
        VideoChannelForm form = new VideoChannelForm();
        form.setChannel(channel.getChannel());
        form.setIsLive(channel.getIsLive());
        form.setSerialNo(channel.getSerialNo());
        form.setVideoName(channel.getVideoName());
        form.setStatus(channel.getStatus());
        Integer stationId = channel.getStation().getId();
        form.setStationId(stationId);
        return form;
    }


    @Scheduled(cron = "0 0 0 0/3 * ? ")
    public void getYsTokenSchedule() {
        this.getYsTokenTask();
    }

    public void getYsTokenTask() {
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
