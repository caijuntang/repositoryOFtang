package com.cooling.hydraulic.service;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.entity.AlarmConfig;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.model.WXTemplateMsg;
import com.cooling.hydraulic.model.WaterLine;
import com.cooling.hydraulic.model.PumpDataModel;
import com.cooling.hydraulic.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WaterLineService {

    private final static Logger log = LoggerFactory.getLogger(WaterLineService.class);

    private final Map<Integer, WaterLine> waterLineMap = new ConcurrentHashMap<Integer, WaterLine>();
    private final Map<Integer, Map<Integer ,PumpDataModel>> stationPumpDataMap = new ConcurrentHashMap<Integer, Map<Integer,PumpDataModel>>();
    private final Map<Integer, Long> alarmFrequecyMap = new ConcurrentHashMap<Integer, Long>();
    public static Double insideVal = 0.01;
    public static Double outsideVal = 0.01;
    public static Double foreVal = 0.01;
    public static String waterLineAlarmContent = "泵站当前内河水位到达{insideLine}米，已超警戒水位线（{alarmLine}米），请及时处置并持续观察！";
    public static String waterLineContent = "【泵站水位详情】\r\n  泵站名称：{stationName}\r\n  通知时间：{dateTime}\r\n  外河水位：{outsideLine}米\r\n" +
            "  前池水位：{foreLine}米\r\n  内河水位：{insideLine}米";
    public static String waterLineDetail = "泵站水位详情\r\n外河水位：{outsideLine}米\r\n前池水位：{foreLine}米\r\n内河水位：{insideLine}米";


    @Resource
    private AlarmService alarmService;
    @Resource
    private StationService stationService;
    @Resource
    private WXService wxService;

    @PostConstruct
    void init() {
        List<Station> allStation = stationService.findAllStation();
        for(Station s:allStation){
            Integer pumpCount = s.getPumpCount();
            HashMap<Integer, PumpDataModel> map = new HashMap<>();
            for(int i=1;i<=pumpCount;i++){
                PumpDataModel model = new PumpDataModel();
                model.setPumpNo(i);
                map.put(i,model);
            }
            stationPumpDataMap.put(s.getId(), map);
        }
    }

    public Map<String, String> reportWaterLine(Double insideLine, Double outsideLine, Double foreLine, Integer stationId) {
        log.info("站点Id为：" + stationId + "的泵站水位上报，内河水位：" + insideLine + "，前池水位：" + foreLine + "，外河水位：" + outsideLine);
        Map<String, String> result = new HashMap<>();
        if (null == insideLine && null == outsideLine && null == foreLine) {
            result.put("code", "999");
            result.put("msg", "data is empty!");
            return result;
        }
        if (null == insideLine) {
            insideLine = foreLine + 0.02;
        }

        boolean isAlarm = false;
        Double alarmLine = alarmService.getAlarmLine(stationId);
        if (insideLine >= alarmLine) {
            isAlarm = true;
        }
        WaterLine waterLine = new WaterLine(insideLine, outsideLine, foreLine, isAlarm);
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

    @Scheduled(cron = "0 0/5 * * * ? ")
    public void wateLineAlarm() {
        log.info("================水位告警查询启动==================");
        List<AlarmConfig> alarmConfigs = alarmService.findByStatus(1);
        long curTime = System.currentTimeMillis() / 1000;
        if (null != alarmConfigs) {
            for (AlarmConfig config : alarmConfigs) {
                Station station = config.getStation();
                Integer stationId = station.getId();
                WaterLine waterLine = waterLineMap.get(stationId);
                if (null == waterLine) {
                    continue;
                }
                String receivers = config.getReceivers();
                if (StringUtils.isEmpty(receivers)) {
                    continue;
                }
                Integer configId = config.getId();
                Long lastTime = alarmFrequecyMap.get(configId);
                if (null != lastTime) {
                    int frequecy = config.getFrequency() * 60;
                    if (curTime - lastTime.longValue() < frequecy) {
                        continue;
                    }
                }
                Double insideVal = waterLine.getInsideVal();
                //未超警戒水位
                Double alarmLine = config.getAlarmLine();
                if (insideVal.compareTo(alarmLine) < 0) {
                    continue;
                }
                String stationName = station.getName();
                try {
                    //发送消息
                    String templateId = config.getTemplateId();
                    String content = waterLineAlarmContent.replace("{insideLine}", waterLine.getInsideVal().toString())
                            .replace("{alarmLine}", alarmLine.toString());
                    //消息模版
                    TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
                    //根据具体模板参数组装
                    params.put("keyword1", this.item(stationName + "水位告警", "#000000"));
                    params.put("keyword2", this.item(DateTimeUtil.getNowDateTimeString(), "#000000"));
                    params.put("keyword3", this.item(content, "#000000"));
                    String remark = waterLineDetail.replace("{outsideLine}", waterLine.getOutsideVal().toString())
                            .replace("{insideLine}", insideVal.toString())
                            .replace("{foreLine}", waterLine.getForeVal().toString());
                    params.put("remark", this.item(remark, "#000000"));
                    List<String> receiverList = JSON.parseObject(receivers, List.class);
                    for (String receiver : receiverList) {
                        WXTemplateMsg msg = new WXTemplateMsg();
                        msg.setTemplate_id(templateId);
                        msg.setTouser(receiver);
                        msg.setData(params);
                        String msgContent = JSON.toJSONString(msg);
                        String resp = wxService.sendWCTemlateMsg(msgContent);
                        log.info("消息发送成功：" + resp);
                    }
                    alarmFrequecyMap.put(configId, curTime);
                } catch (Exception e) {
                    log.error("告警通知发送失败！", e);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 8,13 * * ? ")
    public void reportWaterDetail() {
        log.info("================上下午水位工作预报查询启动==================");
        List<AlarmConfig> alarmConfigs = alarmService.findByStatus(1);
        if (null != alarmConfigs) {
            for (AlarmConfig config : alarmConfigs) {
                Station station = config.getStation();
                Integer stationId = station.getId();
                WaterLine waterLine = waterLineMap.get(stationId);
                if (null == waterLine) {
                    continue;
                }
                String receivers = config.getReceivers();
                if (StringUtils.isEmpty(receivers)) {
                    continue;
                }
                Double insideVal = waterLine.getInsideVal();
                String stationName = station.getName();
                try {
                    //发送消息
                    String content = waterLineContent.replace("{stationName}", stationName)
                            .replace("{dateTime}", DateTimeUtil.getNowDateTimeString())
                            .replace("{outsideLine}", waterLine.getOutsideVal().toString())
                            .replace("{insideLine}", insideVal.toString())
                            .replace("{foreLine}", waterLine.getForeVal().toString());
                    //消息模版
                    List<String> receiverList = JSON.parseObject(receivers, List.class);
                    for (String receiver : receiverList) {
                        wxService.sendWCTextMsg(content, receiver);
                    }
                } catch (Exception e) {
                    log.error("水位通知发送失败！", e);
                }
            }
        }
    }

    public String getWaterLineContent(Integer stationId) {
        Station station = stationService.getOne(stationId);
        WaterLine waterLine = waterLineMap.get(stationId);
        if (null == station || null == waterLine) {
            return "暂无水情报告！";
        }
        String content = waterLineContent.replace("{stationName}", station.getName())
                .replace("{dateTime}", DateTimeUtil.getNowDateTimeString())
                .replace("{outsideLine}", waterLine.getOutsideVal().toString())
                .replace("{insideLine}", waterLine.getInsideVal().toString())
                .replace("{foreLine}", waterLine.getForeVal().toString());
        return content;
    }

    public WaterLine getWaterLineObject(Integer stationId) {
        WaterLine waterLine = waterLineMap.get(stationId);
        return waterLine;
    }


    public TreeMap<String, String> item(String value, String color) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("value", value);
        params.put("color", color);
        return params;
    }

    public Object reportPumpData(Integer stationId, PumpDataModel request) {
        log.info("站点Id为：" + stationId + "的泵机数据：" + request.toString());
        Map<String, String> result = new HashMap<>();
        Integer pumpNo = request.getPumpNo();
        if (null == pumpNo || null == stationId) {
            result.put("code", "999");
            result.put("msg", "data is empty!");
            return result;
        }
        Map<Integer, PumpDataModel> pumpDataMap = stationPumpDataMap.get(stationId);
        if(null==pumpDataMap){
            pumpDataMap=new ConcurrentHashMap<>();
        }
        pumpDataMap.put(pumpNo,request);
        stationPumpDataMap.put(stationId,pumpDataMap);
        result.put("code", "200");
        result.put("msg", "report success!");
        return result;

    }

    public Map<Integer, PumpDataModel> getPumpDataMap(Integer stationId) {
     return stationPumpDataMap.get(stationId);
    }

    public PumpDataModel getPumpData(Integer stationId, Integer pumpNo) {
        Map<Integer, PumpDataModel> pumpDataModelMap = stationPumpDataMap.get(stationId);
        PumpDataModel pumpDataModel = pumpDataModelMap.get(pumpNo);
        return pumpDataModel;
    }
}
