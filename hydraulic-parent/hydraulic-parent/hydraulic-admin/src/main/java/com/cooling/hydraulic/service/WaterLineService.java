package com.cooling.hydraulic.service;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.entity.AlarmConfig;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.model.PumpDataForm;
import com.cooling.hydraulic.model.WXTemplateMsg;
import com.cooling.hydraulic.model.WaterLine;
import com.cooling.hydraulic.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WaterLineService {

    private final static Logger log = LoggerFactory.getLogger(WaterLineService.class);
    private final Map<Integer, WaterLine> waterLineMap = new ConcurrentHashMap<Integer, WaterLine>();
    private final Map<Integer, Map<Integer , PumpDataForm>> stationPumpDataMap = new ConcurrentHashMap<Integer, Map<Integer, PumpDataForm>>();
    public static Double insideVal = 0.01;
    public static Double outsideVal = 0.01;
    public static Double foreVal = 0.01;
    public static String waterLineContent = "【泵站水位详情】\r\n  泵站名称：{stationName}\r\n  通知时间：{dateTime}\r\n  外河水位：{outsideLine}米\r\n" +
            "  前池水位：{foreLine}米\r\n  内河水位：{insideLine}米";


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
            HashMap<Integer, PumpDataForm> map = new HashMap<>();
            for(int i=1;i<=pumpCount;i++){
                PumpDataForm model = new PumpDataForm();
                model.setPumpNo(i);
                map.put(i,model);
            }
            Integer stationId = s.getId();
            stationPumpDataMap.put(stationId, map);
            WaterLine waterLine = new WaterLine();
            waterLineMap.put(stationId,waterLine);
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
        if (null == insideLine&&null!=foreLine) {
            BigDecimal foreBigDecimal = BigDecimal.valueOf(foreLine);
            foreBigDecimal=foreBigDecimal.add(BigDecimal.valueOf(0.02));
            insideLine = foreBigDecimal.setScale(2).doubleValue();
        }

        boolean isAlarm = false;
        Double alarmLine = alarmService.getAlarmLine(stationId);
        if (null!=insideLine &&insideLine >= alarmLine) {
            isAlarm = true;
        }
        WaterLine waterLine = waterLineMap.get(stationId);
        if(null==waterLine){
            waterLine = new WaterLine(insideLine, outsideLine, foreLine, isAlarm);
        }else {
            if(null!=insideLine){
                waterLine.setInsideVal(insideLine);
            }
            if(null!=foreLine){
                waterLine.setForeVal(foreLine);
            }
            if(null!=outsideLine){
                waterLine.setOutsideVal(outsideLine);
            }
            waterLine.setAlarm(isAlarm);
        }
        waterLineMap.put(stationId,waterLine);
        result.put("code", "200");
        result.put("msg", "report success!");
        //校验水位
        alarmService.wateLineAlarm();
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

//    @Scheduled(cron = "0 0/5 * * * ? ")
//    @Async
//    public void wateLineAlarm() {
//        log.info("================水位告警查询启动==================");
//        List<AlarmConfig> alarmConfigs = alarmService.findByStatus(1);
//        long curTime = System.currentTimeMillis() / 1000;
//        if (null != alarmConfigs) {
//            for (AlarmConfig config : alarmConfigs) {
//                Station station = config.getStation();
//                Integer stationId = station.getId();
//                WaterLine waterLine = waterLineMap.get(stationId);
//                if (null == waterLine) {
//                    continue;
//                }
//                String receivers = config.getReceivers();
//                if (StringUtils.isEmpty(receivers)) {
//                    continue;
//                }
//                Integer configId = config.getId();
//                Long lastTime = alarmFrequecyMap.get(configId);
//                if (null != lastTime) {
//                    int frequecy = config.getFrequency() * 60;
//                    if (curTime - lastTime.longValue() < frequecy) {
//                        continue;
//                    }
//                }
//                Double insideVal = waterLine.getInsideVal();
//                //未超警戒水位
//                Double alarmLine = config.getAlarmLine();
//                if (insideVal.compareTo(alarmLine) < 0) {
//                    continue;
//                }
//                String stationName = station.getName();
//                try {
//                    //发送消息
//                    String templateId = config.getTemplateId();
//                    String content = waterLineAlarmContent.replace("{insideLine}", waterLine.getInsideVal().toString())
//                            .replace("{alarmLine}", alarmLine.toString());
//                    //消息模版
//                    TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
//                    //根据具体模板参数组装
//                    params.put("keyword1", this.item(stationName + "水位告警", "#000000"));
//                    params.put("keyword2", this.item(DateTimeUtil.getNowDateTimeString(), "#000000"));
//                    params.put("keyword3", this.item(content, "#000000"));
//                    String remark = waterLineDetail.replace("{outsideLine}", waterLine.getOutsideVal().toString())
//                            .replace("{insideLine}", insideVal.toString())
//                            .replace("{foreLine}", waterLine.getForeVal().toString());
//                    params.put("remark", this.item(remark, "#000000"));
//                    List<String> receiverList = JSON.parseObject(receivers, List.class);
//                    for (String receiver : receiverList) {
//                        WXTemplateMsg msg = new WXTemplateMsg();
//                        msg.setTemplate_id(templateId);
//                        msg.setTouser(receiver);
//                        msg.setData(params);
//                        String msgContent = JSON.toJSONString(msg);
//                        String resp = wxService.sendWCTemlateMsg(msgContent);
//                        log.info("消息发送成功：" + resp);
//                    }
//                    alarmFrequecyMap.put(configId, curTime);
//                } catch (Exception e) {
//                    log.error("告警通知发送失败！", e);
//                }
//            }
//        }
//    }

    @Scheduled(cron = "0 0 8,13, * * ? ")
    public void reportWaterDetail() {
        log.info("================上下午水位工作预报查询启动==================");
        List<AlarmConfig> alarmConfigs = alarmService.findByStatus(true);
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

    public Object reportPumpData(Integer stationId, PumpDataForm request) {
        Map<String, String> result = new HashMap<>();
        if (null == request || null == stationId) {
            result.put("code", "999");
            result.put("msg", "data is empty!");
            return result;
        }
        Integer pumpNo = request.getPumpNo();
        log.info("站点Id为：" + stationId + "的泵机数据：" + request.toString());
        Map<Integer, PumpDataForm> pumpDataMap = stationPumpDataMap.get(stationId);
        if(null==pumpDataMap){
            pumpDataMap=new ConcurrentHashMap<>();
        }else{
            String va = request.getVa();
            String vb = request.getVb();
            String vc = request.getVc();
            PumpDataForm pumpDataModel = pumpDataMap.get(pumpNo);
            if(StringUtils.isEmpty(va)||"0.00".equals(va)){
                if(StringUtils.isEmpty(vb)||"0.00".equals(vb)){
                    request.setVa(vc);
                    request.setVb(vc);
                    va=vc;
                    vb=vc;
                }else{
                   request.setVa(vb);
                   va=vb;
                }
            }
            if(StringUtils.isEmpty(va)||"0.00".equals(va)){
                request.setVa(pumpDataModel.getVa());
            }
            if(StringUtils.isEmpty(vb)||"0.00".equals(vb)){
                request.setVb(pumpDataModel.getVb());
            }
        }
        pumpDataMap.put(pumpNo,request);
        stationPumpDataMap.put(stationId,pumpDataMap);
        result.put("code", "200");
        result.put("msg", "report success!");
        return result;

    }

    public Map<Integer, PumpDataForm> getPumpDataMap(Integer stationId) {
     return stationPumpDataMap.get(stationId);
    }

    public PumpDataForm getPumpData(Integer stationId, Integer pumpNo) {
        Map<Integer, PumpDataForm> pumpDataModelMap = stationPumpDataMap.get(stationId);
        PumpDataForm pumpDataModel = pumpDataModelMap.get(pumpNo);
        return pumpDataModel;
    }
}
