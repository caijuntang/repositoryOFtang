package com.cooling.hydraulic.service;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.dao.AlarmConfigRepository;
import com.cooling.hydraulic.dao.StationRepository;
import com.cooling.hydraulic.entity.AlarmConfig;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.model.AlarmConfigModel;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AlarmService {
    private final Map<Integer, Double> alarmLineMap = new ConcurrentHashMap<Integer, Double>();

    @Resource
    private AlarmConfigRepository alarmConfigRepository;
    @Resource
    private StationRepository stationRepository;


    @PostConstruct
    void init() {
        List<AlarmConfig> all = alarmConfigRepository.findByStatus(1);
        for (AlarmConfig config : all) {
            alarmLineMap.put(config.getStation().getId(), config.getAlarmLine());
        }
    }

    public AlarmConfig findAlarmConfigsByStation(Station station) {
        return alarmConfigRepository.findAlarmConfigByStation(station);
    }

    public boolean saveAlarmConfig(AlarmConfigModel config) {
        Integer id = config.getId();
        List<String> receivers = config.getReceivers();
        String reveiversStr = JSON.toJSONString(receivers);
        String frequency = config.getFrequency();
        Integer frequencyInt = 5;
        if (StringUtils.isNotEmpty(frequency)) {
            frequencyInt = Integer.parseInt(frequency);
        }
        if (null != id) {
            AlarmConfig old = alarmConfigRepository.getOne(id);
            old.setReceivers(reveiversStr);
            old.setAlarmLine(config.getAlarmLine());
            old.setName(config.getAlarmName());
            old.setFrequency(frequencyInt);
            alarmConfigRepository.save(old);
        } else {
            Station station = stationRepository.getOne(config.getStationId());
            AlarmConfig alarmConfig = new AlarmConfig();
            alarmConfig.setName(config.getAlarmName());
            alarmConfig.setAlarmLine(config.getAlarmLine());
            alarmConfig.setStatus(config.getStatus());
            alarmConfig.setReceivers(reveiversStr);
            alarmConfig.setStation(station);
            alarmConfig.setCreateTime(new Date());
            alarmConfig.setFrequency(frequencyInt);
            alarmConfigRepository.save(alarmConfig);
        }
        if (config.getStatus() == 1) {
            alarmLineMap.put(1, config.getAlarmLine());
        }
        return true;
    }

    public List<AlarmConfig> findAll() {
        return alarmConfigRepository.findAll();
    }

    public List<AlarmConfig> findByStatus(Integer status) {
        return alarmConfigRepository.findByStatus(status);
    }

    public double getAlarmLine(Integer stationId) {
        Double alarmLine = alarmLineMap.get(stationId);
        if (null == alarmLine) {
            return 0.00;
        }
        return alarmLine;
    }

    public AlarmConfigModel getAlarmConfig(Integer stationId) {
        Station station = new Station();
        station.setId(stationId);
        AlarmConfig config = alarmConfigRepository.findAlarmConfigByStation(station);
        AlarmConfigModel model = this.convertToModel(config);
        return model;
    }


    public boolean updateAlarmStatus(Integer id) {
        AlarmConfig config = alarmConfigRepository.getOne(id);
        if (null != config) {
            Integer status = config.getStatus();
            config.setStatus(status == 0 ? 1 : 0);
            alarmConfigRepository.save(config);
            Integer stationId = config.getStation().getId();
            if (status == 1) {
                alarmLineMap.remove(stationId);
            } else {
                alarmLineMap.put(stationId, config.getAlarmLine());
            }
        }
        return true;
    }

    private AlarmConfigModel convertToModel(AlarmConfig config) {
        AlarmConfigModel model = new AlarmConfigModel();
        model.setId(config.getId());
        model.setAlarmLine(config.getAlarmLine());
        model.setStatus(config.getStatus());
        model.setAlarmName(config.getName());
        model.setStationId(config.getStation().getId());
        model.setFrequency(config.getFrequency().toString());
        String receivers = config.getReceivers();
        List<String> receiverList = JSON.parseObject(receivers, List.class);
        model.setReceivers(receiverList);
        return model;
    }
}
