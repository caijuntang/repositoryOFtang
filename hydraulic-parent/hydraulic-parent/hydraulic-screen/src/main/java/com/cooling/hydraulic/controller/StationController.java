package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/station")
public class StationController {

    @Autowired
    private StationService stationService;

    @RequestMapping("/getStationData")
    @ResponseBody
    public Object getStationList() {
        HashMap<String,Object> result = new HashMap<>();
        List<Station> allStation = stationService.findAllStation();
        result.put("stationList",allStation);
        Station defaultStation = stationService.getDefaultStation();
        result.put("defaultStation",defaultStation);
        return  result;
    }


}
