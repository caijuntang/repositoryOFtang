package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/station")
public class StationController {

    @Resource
    private StationService stationService;

    @RequestMapping("/getStationData")
    @ResponseBody
    public Object getStationData() {
        Station defaultStation = stationService.getDefaultStation();
        List<Station> allStation = stationService.findAllStation();
        Map<String, Object> result = new HashMap<>();
        result.put("default",defaultStation);
        result.put("all",allStation);
        return result;
    }
}
