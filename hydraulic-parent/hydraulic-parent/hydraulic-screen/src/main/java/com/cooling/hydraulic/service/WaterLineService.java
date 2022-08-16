package com.cooling.hydraulic.service;

import com.cooling.hydraulic.entity.AlarmConfig;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.model.WaterLine;
import com.cooling.hydraulic.utils.ChatSendUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class WaterLineService {

    private final static Logger log = LoggerFactory.getLogger(WaterLineService.class);

    private final Map<Integer,WaterLine> waterLineMap=new ConcurrentHashMap<Integer, WaterLine>();
    public static Double insideVal = 0.01;
    public static Double outsideVal = 0.01;
    public static Double foreVal = 0.01;

    @Resource
    private AlarmService alarmService;

    public Map<String, String> reportWaterLine(Double insideLine, Double outsideLine, Double foreLine,Integer stationId) {
        Map<String, String> result = new HashMap<>();
        if(null==insideLine||null==outsideLine||null==foreLine){
            result.put("code", "999");
            result.put("msg", "data is empty!");
            return result;
        }
        if(null==stationId){
            stationId=1;
        }
        WaterLine waterLine=new WaterLine(insideLine,outsideLine,foreLine);
        waterLineMap.put(stationId,waterLine);
        result.put("code", "200");
        result.put("msg", "report success!");
        return result;
    }

    public Map<String, Object> getWaterLine(Integer stationId) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", "200");
        WaterLine waterLine = waterLineMap.get(stationId);
        if(null!=waterLine){
            Double alarmLine = alarmService.getAlarmLine(stationId);
            result.put("alarmLine",alarmLine);
            result.put("insideVal", waterLine.getInsideVal());
            result.put("outsideVal", waterLine.getOutsideVal());
            result.put("foreVal", waterLine.getForeVal());
            return result;
        }
        result.put("insideVal", insideVal);
        result.put("outsideVal", outsideVal);
        result.put("foreVal", foreVal);
        return result;
    }

    @Scheduled(cron = "0 */5 * * * ? ")
    public void wateLineAlarm(){
        List<AlarmConfig> alarmConfigs = alarmService.findByStatus(1);
        if(null!=alarmConfigs){
            for (AlarmConfig config :alarmConfigs){
                Station station = config.getStation();
                Integer stationId = station.getId();
                WaterLine waterLine = waterLineMap.get(stationId);
                if(null==waterLine){
                    continue;
                }
                Double insideVal = waterLine.getInsideVal();
                //未超警戒水位
                double alarmLine = config.getAlarmLine();
                if(insideVal.compareTo(alarmLine)<0){
                    continue;
                }
                String stationName = station.getName();
                String receivers = config.getReceivers();
                try {
                    String content=stationName+"内河水位："+insideVal+"，已超警戒线（"+alarmLine+"米），请及时处置！";
                    ChatSendUtil.sendTextMsg(content);
                    log.info("消息发送成功："+content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
