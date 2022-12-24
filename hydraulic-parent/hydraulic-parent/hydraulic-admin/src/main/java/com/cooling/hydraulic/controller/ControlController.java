package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.entity.AlarmRecord;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.model.PumpDataForm;
import com.cooling.hydraulic.service.AlarmRecordService;
import com.cooling.hydraulic.service.StationService;
import com.cooling.hydraulic.service.WXService;
import com.cooling.hydraulic.service.WaterLineService;
import com.cooling.hydraulic.utils.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping(path="/api/control")
public class ControlController {

    @Resource
    private WaterLineService waterLineService;
    @Resource
    private StationService stationService;
    @Resource
    private AlarmRecordService alarmRecordService;
    @Resource
    private WXService wxUserService;


    @RequestMapping("/getWaterLine")
    @ResponseBody
    public Object getWaterLine(Integer stationId) {
        return waterLineService.getWaterLine(stationId);
    }

    @RequestMapping("/getPumpData")
    @ResponseBody
    public Object getPumpData(Integer stationId) {
        Map<Integer, PumpDataForm> pumpDataMap = waterLineService.getPumpDataMap(stationId);
        Map<String, List<Object>> resultMap=new HashMap<>();

        List<Object> vResult= new ArrayList<>();
        List<Object> aRsesult = new ArrayList<>();
        Set<Map.Entry<Integer, PumpDataForm>> entries = pumpDataMap.entrySet();
        for(Map.Entry<Integer, PumpDataForm> e:entries){
            List<String> vList = new ArrayList<>();
            List<String> aList = new ArrayList<>();
            Integer key = e.getKey();
            PumpDataForm p = e.getValue();
            String kStr = String.valueOf(key);
            vList.add(kStr);
            vList.add(p.getVa());
            vList.add(p.getVb());
            vList.add(p.getVc());
            aList.add(kStr);
            aList.add(p.getAa());
            aList.add(p.getAb());
            aList.add(p.getAc());
            vResult.add(vList);
            aRsesult.add(aList);
        }
        resultMap.put("vol",vResult);
        resultMap.put("ele",aRsesult);
        return resultMap;
    }


    @RequestMapping("/getAlarmRecord")
    @ResponseBody
    public Object getAlarmRecord(Integer stationId) {
        List<AlarmRecord> recordList = alarmRecordService.getRecordList(stationId);
        List<List<Object>> result = new ArrayList<>();
        int index=0;
        int size = recordList.size();
        while (index<size){
            List<Object> data = new ArrayList<>();
            AlarmRecord r = recordList.get(index);
            data.add(index+1);
            data.add(r.getAlarmType().getName());
            data.add(r.getContent());
            data.add(DateUtil.localDateTimeFormatyMdHms(r.getCreateTime()));
            result.add(data);
            index++;
        }
        if(size<5){
            while(size<=5) {
                List<Object> extr = new ArrayList<>();
                extr.add(size+1);
                extr.add("");
                extr.add("");
                extr.add("");
                result.add(extr);
                size++;
            }
        }
        return result;
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




    @RequestMapping("/getWXReceivers")
    @ResponseBody
    public Object getWXUsers() {
        return wxUserService.getWXUserList();
    }
}
