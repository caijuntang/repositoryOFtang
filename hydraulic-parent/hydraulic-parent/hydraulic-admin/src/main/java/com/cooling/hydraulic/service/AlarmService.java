package com.cooling.hydraulic.service;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.dao.AlarmConfigRepository;
import com.cooling.hydraulic.dao.AlarmRecordRepository;
import com.cooling.hydraulic.dao.StationRepository;
import com.cooling.hydraulic.entity.AlarmConfig;
import com.cooling.hydraulic.entity.AlarmRecord;
import com.cooling.hydraulic.entity.Menu;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.enums.AlarmTypeEnum;
import com.cooling.hydraulic.exception.BadRequestException;
import com.cooling.hydraulic.exception.EntityExistException;
import com.cooling.hydraulic.model.alarm.AlarmDto;
import com.cooling.hydraulic.model.WXTemplateMsg;
import com.cooling.hydraulic.model.WaterLine;
import com.cooling.hydraulic.model.alarm.AlarmQueryCriteria;
import com.cooling.hydraulic.utils.DateTimeUtil;
import com.cooling.hydraulic.utils.PageUtil;
import com.cooling.hydraulic.utils.QueryHelp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AlarmService {
    private final static Logger log = LoggerFactory.getLogger(WaterLineService.class);
    private final Map<Integer, Double> alarmLineMap = new ConcurrentHashMap<Integer, Double>();
    private final Map<Integer, Long> alarmFrequecyMap = new ConcurrentHashMap<Integer, Long>();
    public static String waterLineAlarmContent = "泵站当前内河水位到达{insideLine}米，已超警戒水位线（{alarmLine}米），请及时处置并持续观察！";
    public static String waterLineDetail = "泵站水位详情\r\n外河水位：{outsideLine}米\r\n前池水位：{foreLine}米\r\n内河水位：{insideLine}米";

    @Resource
    private  WaterLineService waterLineService;
    @Resource
    private WXService wxService;
    @Resource
    private AlarmConfigRepository alarmConfigRepository;
    @Resource
    private StationRepository stationRepository;
    @Resource
    private AlarmRecordService alarmRecordService;


    @PostConstruct
    void init() {
        List<AlarmConfig> all = alarmConfigRepository.findByStatus(true);
        for (AlarmConfig config : all) {
            alarmLineMap.put(config.getStation().getId(), config.getAlarmLine());
        }
    }

    public AlarmConfig findAlarmConfigsByStation(Station station) {
        return alarmConfigRepository.findAlarmConfigByStation(station);
    }

    public boolean saveAlarmConfig(AlarmDto config) {
        Integer id = config.getId();
        List<String> receivers = config.getReceivers();
        String reveiversStr = JSON.toJSONString(receivers);
        int frequency = config.getFrequency();
        boolean status = config.getStatus();
        Integer stationId = config.getStationId();
        double alarmLine = config.getAlarmLine();
        if (null != id) {
            AlarmConfig old = alarmConfigRepository.getOne(id);
            old.setReceivers(reveiversStr);
            old.setAlarmLine(alarmLine);
            old.setName(config.getName());
            old.setFrequency(frequency);
            old.setStatus(status);
            alarmConfigRepository.save(old);
        } else {
            Station station = stationRepository.getOne(stationId);
            AlarmConfig alarmConfig = new AlarmConfig();
            alarmConfig.setName(config.getName());
            alarmConfig.setAlarmLine(alarmLine);
            alarmConfig.setStatus(status);
            alarmConfig.setReceivers(reveiversStr);
            alarmConfig.setStation(station);
            alarmConfig.setCreateTime(LocalDateTime.now());
            alarmConfig.setFrequency(frequency);
            alarmConfigRepository.save(alarmConfig);
        }
        if (status == true) {
            alarmLineMap.put(stationId, alarmLine);
        }else{
            alarmLineMap.remove(stationId);
        }
        return true;
    }

    public List<AlarmConfig> findAll() {
        return alarmConfigRepository.findAll();
    }

    public List<AlarmConfig> findByStatus(boolean status) {
        return alarmConfigRepository.findByStatus(status);
    }

    public double getAlarmLine(Integer stationId) {
        Double alarmLine = alarmLineMap.get(stationId);
        if (null == alarmLine) {
            return 0.00;
        }
        return alarmLine;
    }

    public AlarmDto getAlarmConfig(Integer stationId) {
        Station station = new Station();
        station.setId(stationId);
        AlarmConfig config = alarmConfigRepository.findAlarmConfigByStation(station);
        AlarmDto model = this.toDto(config);
        return model;
    }


    public boolean updateAlarmStatus(Integer id) {
        AlarmConfig config = alarmConfigRepository.getOne(id);
        if (null != config) {
            boolean status = config.getStatus();
            config.setStatus(status == false ? true : false);
            alarmConfigRepository.save(config);
            Integer stationId = config.getStation().getId();
            if (status == true) {
                alarmLineMap.remove(stationId);
            } else {
                alarmLineMap.put(stationId, config.getAlarmLine());
            }
        }
        return true;
    }

    private AlarmDto toDto(AlarmConfig config) {
        AlarmDto model = new AlarmDto();
        model.setId(config.getId());
        model.setAlarmLine(config.getAlarmLine());
        model.setStatus(config.getStatus());
        model.setName(config.getName());
        Station station = config.getStation();
        model.setStationId(station.getId());
        model.setStationName(station.getName());
        model.setFrequency(config.getFrequency());
        model.setCreateTime(config.getCreateTime());
        String receivers = config.getReceivers();
        List<String> receiverList = JSON.parseObject(receivers, List.class);
        model.setReceivers(receiverList);
        return model;
    }

    @Async
    public void wateLineAlarm() {
        log.info("================水位告警查询启动==================");
        List<AlarmConfig> alarmConfigs = this.findByStatus(true);
        long curTime = System.currentTimeMillis() / 1000;
        if (null != alarmConfigs) {
            for (AlarmConfig config : alarmConfigs) {
                Station station = config.getStation();
                Integer stationId = station.getId();
                WaterLine waterLine = waterLineService.getWaterLineObject(stationId);
                if (null == waterLine) {
                    continue;
                }
                String receivers = config.getReceivers();
                if (org.springframework.util.StringUtils.isEmpty(receivers)) {
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
                try {
                    for (String receiver : receiverList) {
                        WXTemplateMsg msg = new WXTemplateMsg();
                        msg.setTemplate_id(templateId);
                        msg.setTouser(receiver);
                        msg.setData(params);
                        String msgContent = JSON.toJSONString(msg);
                        String resp = wxService.sendWCTemlateMsg(msgContent);
                        log.info("消息发送成功：" + resp);
                    }
                    //设置告警频率
                    alarmFrequecyMap.put(configId, curTime);
                } catch (Exception e) {
                    log.error("告警通知发送失败！", e);
                }
                //保存告警记录
                AlarmRecord record = new AlarmRecord();
                record.setStation(station);
                record.setAlarmType(AlarmTypeEnum.WATERLINE);
                record.setContent(content);
                alarmRecordService.create(record);
            }
        }
    }

    public TreeMap<String, String> item(String value, String color) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("value", value);
        params.put("color", color);
        return params;
    }

    public Object queryAll(AlarmQueryCriteria criteria, Pageable pageable) {
        Page<AlarmConfig> page = alarmConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(this::toDto));
    }

    @Transactional
    public void create(AlarmDto resources) {
        Integer stationId = resources.getStationId();
        Station station = new Station();
        station.setId(stationId);
        if(alarmConfigRepository.findAlarmConfigByStation(station) != null){
            throw new EntityExistException(Menu.class,"name",resources.getName());
        }
        Double alarmLine = resources.getAlarmLine();
        if(null== alarmLine){
            throw new BadRequestException("告警水位参数缺失");
        }
        AlarmConfig alarmConfig = new AlarmConfig();
        alarmConfig.setName(resources.getName());
        alarmConfig.setAlarmLine(alarmLine);
        boolean status = resources.getStatus();
        alarmConfig.setStatus(status);
        List<String> receivers = resources.getReceivers();
        String reveiversStr = JSON.toJSONString(receivers);
        alarmConfig.setReceivers(reveiversStr);
        alarmConfig.setStation(station);
        alarmConfig.setCreateTime(LocalDateTime.now());
        alarmConfig.setFrequency(resources.getFrequency());
        alarmConfigRepository.save(alarmConfig);
        if (status == true) {
            alarmLineMap.put(stationId, alarmLine);
        }else{
            alarmLineMap.remove(stationId);
        }
    }

    @Transactional
    public void update(AlarmDto config) {
        Integer id = config.getId();
        if(null==id){
            throw new BadRequestException("id缺失");
        }
        int frequency = config.getFrequency();
        List<String> receivers = config.getReceivers();
        String reveiversStr = JSON.toJSONString(receivers);
        boolean status = config.getStatus();
        Integer stationId = config.getStationId();
        double alarmLine = config.getAlarmLine();
        AlarmConfig old = alarmConfigRepository.getOne(id);
        old.setReceivers(reveiversStr);
        old.setAlarmLine(alarmLine);
        old.setName(config.getName());
        old.setFrequency(frequency);
        old.setStatus(status);
        alarmConfigRepository.save(old);
        if (status == true) {
            alarmLineMap.put(stationId, alarmLine);
        }else{
            alarmLineMap.remove(stationId);
        }
    }

    public void delete(Set<Integer> ids) {
        for (Integer id : ids) {
            alarmConfigRepository.deleteById(id);
        }
    }
}
