package com.cooling.hydraulic.service;


import com.cooling.hydraulic.dao.VideoChannelRepository;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.entity.VideoChannel;
import com.cooling.hydraulic.model.VideoChannelForm;
import com.cooling.hydraulic.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoChannelService {

    private final static Logger log = LoggerFactory.getLogger(VideoChannelService.class);

    @Resource
    private StationService stationService;
    @Resource
    private VideoChannelRepository videoChannelRepository;

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
        List<VideoChannel> channels = videoChannelRepository.findVideoChannelByStation(stationId);
        Map<String, List<VideoChannel>> dataMap = new HashMap<String, List<VideoChannel>>();
        List<VideoChannel> liveVideo = new ArrayList<>();
        if(null!=channels&&!channels.isEmpty()){
            for(VideoChannel c:channels){
                int isLive = c.getIsLive();
                if(isLive==1){
                    liveVideo.add(c);
                }
            }
        }
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

}
