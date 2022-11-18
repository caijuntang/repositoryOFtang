package com.cooling.hydraulic.service;


import com.cooling.hydraulic.config.BaseConfig;
import com.cooling.hydraulic.dao.StationRepository;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.model.StationForm;
import com.cooling.hydraulic.utils.DateTimeUtil;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.DateUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class StationService {

    private final static Logger log = LoggerFactory.getLogger(StationService.class);

    @Resource
    private StationRepository stationRepository;

    @Transactional
    public boolean save(StationForm form){
        if(null==form){
            return false;
        }
        try {
            Integer id = form.getId();
            if(null==id){
                Station station = this.formConvertToEntity(form);
                stationRepository.save(station);
            }else{
                Station oldStation = stationRepository.getOne(id);
                oldStation.setName(form.getName());
                oldStation.setCity(form.getCity());
                oldStation.setLocation(form.getLocation());
                oldStation.setProvince(form.getProvince());
                oldStation.setAttitude(form.getAttitude());
                oldStation.setLongitude(form.getLongitude());
                int isDefault = form.getIsDefault();
                if(isDefault==1){
                    stationRepository.changeDefalutStation();
                }
                oldStation.setIsDefault(isDefault);
                oldStation.setNameKey(form.getNameKey());
                oldStation.setCityCode(form.getCityCode());
                oldStation.setPumpCount(form.getPumpCount());
                oldStation.setRemark(form.getRemark());
                stationRepository.save(oldStation);
            }
        } catch (Exception e) {
            log.error("泵站信息保存失败",e);
            return false;
        }
        return true;
    }



     public  List<Station> findAllStation(){
        return stationRepository.findAll();
    }

    public Station getOne(Integer stationId) {
         return stationRepository.getOne(stationId);
    }

    public Station getDefaultStation(){
        return stationRepository.findStationByIsDefault(1);
    }

    private Station formConvertToEntity(StationForm form){
        Station station = new Station();
        station.setName(form.getName());
        station.setCity(form.getCity());
        station.setLocation(form.getLocation());
        station.setProvince(form.getProvince());
        station.setAttitude(form.getAttitude());
        station.setLongitude(form.getLongitude());
        station.setIsDefault(form.getIsDefault());
        station.setNameKey(form.getNameKey());
        station.setCityCode(form.getCityCode());
        station.setPumpCount(form.getPumpCount());
        station.setRemark(form.getRemark());
        station.setCreateTime(DateTimeUtil.getNowDateTime());
        return station;
    }



    private StationForm entityConvertToForm(Station station){
        StationForm form = new StationForm();
        form.setId(station.getId());
        form.setName(station.getName());
        form.setCity(station.getCity());
        form.setLocation(station.getLocation());
        form.setProvince(station.getProvince());
        form.setAttitude(station.getAttitude());
        form.setLongitude(station.getLongitude());
        form.setIsDefault(station.getIsDefault());
        form.setNameKey(station.getNameKey());
        form.setCityCode(station.getCityCode());
        form.setPumpCount(station.getPumpCount());
        form.setRemark(station.getRemark());
        form.setCreateTime(station.getCreateTime());
        return form;
    }

}
