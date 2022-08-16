package com.cooling.hydraulic.service;

import com.cooling.hydraulic.dao.AlarmConfigRepository;
import com.cooling.hydraulic.dao.StationRepository;
import com.cooling.hydraulic.entity.AlarmConfig;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.model.AlarmConfigModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AlarmService {
    private final Map<Integer,Double> alarmLineMap=new ConcurrentHashMap<Integer, Double>();

    @Resource
    private AlarmConfigRepository alarmConfigRepository;
    @Resource
    private StationRepository stationRepository;

    public  AlarmConfig findAlarmConfigsByStation(Station station){
       return alarmConfigRepository.findAlarmConfigByStation(station);
    }

    public boolean saveAlarmConfig(AlarmConfigModel config){
        Integer id = config.getId();
        if(null!=id){
            AlarmConfig old = alarmConfigRepository.getOne(id);
            old.setReceivers(config.getReceivers());
            old.setAlarmLine(config.getAlarmLine());
            alarmConfigRepository.save(old);
        }else {
            Station station = stationRepository.getOne(config.getStationId());
            AlarmConfig alarmConfig = new AlarmConfig();
            alarmConfig.setName(config.getAlarmName());
            alarmConfig.setAlarmLine(config.getAlarmLine());
            alarmConfig.setStatus(config.getStatus());
            alarmConfig.setReceivers(config.getReceivers());
            alarmConfig.setStation(station);
            alarmConfig.setCreateTime(new Date());
            alarmConfigRepository.save(alarmConfig);
        }
        if(config.getStatus()==1){
            alarmLineMap.put(1,config.getAlarmLine());
        }
        return true;
    }

    public List<AlarmConfig> findAll(){
       return alarmConfigRepository.findAll();
    }

    public List<AlarmConfig> findByStatus(Integer status){
        return alarmConfigRepository.findByStatus(status);
    }

    public double getAlarmLine(Integer stationId){
        Double alarmLine = alarmLineMap.get(stationId);
        if (null==alarmLine){
            return 0.00;
        }
        return alarmLine;
    }

    public AlarmConfig getAlarmConfig(Integer stationId) {
        Station station = new Station();
        station.setId(stationId);
        return alarmConfigRepository.findAlarmConfigByStation(station);
    }

    public boolean updateAlarmStatus(Integer id) {
        AlarmConfig config = alarmConfigRepository.getOne(id);
        if(null!=config){
            Integer status = config.getStatus();
            config.setStatus(status==0?1:0);
            alarmConfigRepository.save(config);
            Integer stationId = config.getStation().getId();
            if(status==1){
                alarmLineMap.remove(stationId);
            }else{
                alarmLineMap.put(stationId,config.getAlarmLine());
            }
        }
        return true;
    }
}
