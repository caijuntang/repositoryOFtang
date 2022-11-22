package com.cooling.hydraulic.controller;

import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.entity.VideoChannel;
import com.cooling.hydraulic.service.VideoChannelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(path="/api/video")
public class VideoController {

    @Resource
    private VideoChannelService videoChannelService;

    @RequestMapping("/getVideoMap")
    @ResponseBody
    public Object getVideoMap(Integer stationId) {
        return videoChannelService.findVideoByStationId(stationId);
    }

}
