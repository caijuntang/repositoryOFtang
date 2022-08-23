package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.model.AlarmConfigModel;
import com.cooling.hydraulic.service.AlarmService;
import com.cooling.hydraulic.service.WXService;
import com.cooling.hydraulic.service.WaterLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path="/control")
public class IndexController {

    @Autowired
    private WaterLineService waterLineService;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private WXService wxUserService;

    @RequestMapping("/index")
    public String index(HttpSession session, HttpServletRequest req, HttpServletResponse resp) {
        return "screen/control/control";
    }

    @RequestMapping("/getWaterLine")
    @ResponseBody
    public Object getWaterLine(Integer stationId) {
        return waterLineService.getWaterLine(stationId);
    }


    @RequestMapping("/getAlarmConfig")
    @ResponseBody
    public Object getAlarmConfig(Integer stationId) {
        return alarmService.getAlarmConfig(stationId);
    }

    @RequestMapping("/alarmConfigSave")
    @ResponseBody
    public Object alarmConfigSave(AlarmConfigModel configModel) {
        return alarmService.saveAlarmConfig(configModel);
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
