package com.cooling.hydraulic.service;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.entity.AlarmConfig;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.model.WaterLine;
import com.cooling.hydraulic.utils.WeChatSendUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WaterLineService {

    private final static Logger log = LoggerFactory.getLogger(WaterLineService.class);

    private final Map<Integer, WaterLine> waterLineMap = new ConcurrentHashMap<Integer, WaterLine>();
    public static Double insideVal = 0.01;
    public static Double outsideVal = 0.01;
    public static Double foreVal = 0.01;
    public static String waterLineAlarmContent = "【泵站水位告警】\r\n泵站名称：{stationName}\r\n外河水位：{outsideLine}米\r\n" +
            "前池水位：{foreLine}米\r\n内河水位：{insideLine}米\r\n告警内容：泵站当前内河水位{insideLine}米，已超警戒水位线（{alarmLine}米），请及时处置！";
    public static String waterLineContent = "【泵站水位详情】\r\n泵站名称：{stationName}\r\n外河水位：{outsideLine}米\r\n" +
            "前池水位：{foreLine}米\r\n内河水位：{insideLine}米";


    @Resource
    private AlarmService alarmService;
    @Resource
    private StationService stationService;
    @Resource
    private WXService wxService;

    public Map<String, String> reportWaterLine(Double insideLine, Double outsideLine, Double foreLine, Integer stationId) {
        log.info("站点Id为：" + stationId + "的泵站水位上报，内河水位：" + insideLine + "，前池水位：" + foreLine + "，外河水位：" + outsideLine);
        Map<String, String> result = new HashMap<>();
        if (null == insideLine && null == outsideLine && null == foreLine) {
            result.put("code", "999");
            result.put("msg", "data is empty!");
            return result;
        }
        if (null==insideLine){
            insideLine=foreLine+0.02;
        }
        WaterLine waterLine = new WaterLine(insideLine, outsideLine, foreLine);
        waterLineMap.put(stationId, waterLine);
        result.put("code", "200");
        result.put("msg", "report success!");
        return result;
    }

    public Map<String, Object> getWaterLine(Integer stationId) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", "200");
        WaterLine waterLine = waterLineMap.get(stationId);
        if (null != waterLine) {
            Double alarmLine = alarmService.getAlarmLine(stationId);
            result.put("alarmLine", alarmLine);
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

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void wateLineAlarm() {
        log.info("================水位告警查询启动==================");
        List<AlarmConfig> alarmConfigs = alarmService.findByStatus(1);
        if (null != alarmConfigs) {
            for (AlarmConfig config : alarmConfigs) {
                Station station = config.getStation();
                Integer stationId = station.getId();
                WaterLine waterLine = waterLineMap.get(stationId);
                if (null == waterLine) {
                    continue;
                }
                Double insideVal = waterLine.getInsideVal();
                //未超警戒水位
                Double alarmLine = config.getAlarmLine();
                if (insideVal.compareTo(alarmLine) < 0) {
                    continue;
                }
                String stationName = station.getName();
                String receivers = config.getReceivers();
                if (StringUtils.isEmpty(receivers)) {
                    continue;
                }
                try {
                    String content = waterLineAlarmContent.replace("{stationName}", stationName)
                            .replace("{outsideLine}", waterLine.getOutsideVal().toString())
                            .replace("{insideLine}", waterLine.getInsideVal().toString())
                            .replace("{foreLine}", waterLine.getForeVal().toString())
                            .replace("{alarmLine}", alarmLine.toString());
                    List<String> receiverList = JSON.parseObject(receivers, List.class);
                    for (String receiver : receiverList) {
                        wxService.sendWCTextMsg(content, receiver);
                    }
                    log.info("消息发送成功：" + content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public String getWaterLineContent(Integer stationId) {
        Station station = stationService.getOne(stationId);
        WaterLine waterLine = waterLineMap.get(stationId);
        if(null==station||null==waterLine){
            return "暂无水情报告！";
        }
        String content = waterLineContent.replace("{stationName}", station.getName())
                .replace("{outsideLine}", waterLine.getOutsideVal().toString())
                .replace("{insideLine}", waterLine.getInsideVal().toString())
                .replace("{foreLine}", waterLine.getForeVal().toString());
        return content;
    }
}
