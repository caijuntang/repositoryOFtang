package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/api/station")
public class StationController {

    @Resource
    private StationService stationService;

    @RequestMapping("/getStationAll")
    @ResponseBody
    public Object getStationAll() {
        return stationService.findAllStation();
    }
}
