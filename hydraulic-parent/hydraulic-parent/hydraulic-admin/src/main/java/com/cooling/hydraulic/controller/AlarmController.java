package com.cooling.hydraulic.controller;

import com.cooling.hydraulic.model.AlarmConfigForm;
import com.cooling.hydraulic.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/api/alarm")
public class AlarmController {

    @Resource
    private AlarmService alarmService;

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
}
