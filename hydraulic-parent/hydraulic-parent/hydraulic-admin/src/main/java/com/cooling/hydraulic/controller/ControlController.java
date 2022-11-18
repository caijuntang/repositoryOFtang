package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.model.AlarmConfigForm;
import com.cooling.hydraulic.model.PumpDataForm;
import com.cooling.hydraulic.service.AlarmService;
import com.cooling.hydraulic.service.StationService;
import com.cooling.hydraulic.service.WXService;
import com.cooling.hydraulic.service.WaterLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(path="/api/control")
public class ControlController {

    @Autowired
    private WaterLineService waterLineService;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private StationService stationService;
    @Autowired
    private WXService wxUserService;

//    @RequestMapping("/index")
//    public String index(HttpSession session, HttpServletRequest req, HttpServletResponse resp) {
//        return "screen/control/control";
//    }

    @RequestMapping("/getWaterLine")
    @ResponseBody
    public Object getWaterLine(Integer stationId) {
        return waterLineService.getWaterLine(stationId);
    }

    @RequestMapping("/getPumpData")
    @ResponseBody
    public Object getPumpData(Integer stationId) {
        Map<Integer, PumpDataForm> pumpDataMap = waterLineService.getPumpDataMap(stationId);
        return pumpDataMap.values();
    }

    @RequestMapping("/getPumpCount")
    @ResponseBody
    public Object getPumpCount(Integer stationId) {
        Station station = stationService.getOne(stationId);
        if(null == station){
            return 4;
        }
        return station.getPumpCount();
    }


    @RequestMapping("/getAlarmConfig")
    @ResponseBody
    public Object getAlarmConfig(Integer stationId) {
        return alarmService.getAlarmConfig(stationId);
    }

    @RequestMapping("/alarmConfigSave")
    @ResponseBody
    public Object alarmConfigSave(AlarmConfigForm form) {
        return alarmService.saveAlarmConfig(form);
    }

    @RequestMapping("/updateAlarmStatus")
    @ResponseBody
    public Object updateAlarmStatus(Integer id) {
        if(null==id){
            return false;
        }
        return alarmService.updateAlarmStatus(id);
    }

    @RequestMapping("/getWXReceivers")
    @ResponseBody
    public Object getWXUsers() {
        return wxUserService.getWXUserList();
    }
}
