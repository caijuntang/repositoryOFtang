package com.cooling.hydraulic.service;


import cn.hutool.core.util.ObjectUtil;
import com.cooling.hydraulic.dao.StationRepository;
import com.cooling.hydraulic.entity.Menu;
import com.cooling.hydraulic.entity.Role;
import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.exception.BadRequestException;
import com.cooling.hydraulic.exception.EntityExistException;
import com.cooling.hydraulic.model.menu.MenuDto;
import com.cooling.hydraulic.model.station.StationDto;
import com.cooling.hydraulic.model.station.StationQueryCriteria;
import com.cooling.hydraulic.utils.DateTimeUtil;
import com.cooling.hydraulic.utils.PageUtil;
import com.cooling.hydraulic.utils.QueryHelp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final static Logger log = LoggerFactory.getLogger(StationService.class);

    @Resource
    private StationRepository stationRepository;

    @Transactional
    public void update(Station form){
        if(null==form){
            throw new BadRequestException("更新对象为空");
        }
        try {
            Integer id = form.getId();
            Station oldStation = stationRepository.getOne(id);
            oldStation.setName(form.getName());
            oldStation.setCity(form.getCity());
            oldStation.setProvince(form.getProvince());
            oldStation.setAttitude(form.getAttitude());
            oldStation.setLongitude(form.getLongitude());
            boolean isDefault = form.getIsDefault();
            if(isDefault==true){
                stationRepository.changeDefalutStation(id);
            }
            oldStation.setIsDefault(isDefault);
            oldStation.setNameKey(form.getNameKey());
            oldStation.setCityCode(form.getCityCode());
            oldStation.setPumpCount(form.getPumpCount());
            oldStation.setRemark(form.getRemark());
            stationRepository.save(oldStation);

        } catch (Exception e) {
            log.error("泵站信息保存失败",e);
        }
    }

    public  List<Station> findAllStation(){
        return stationRepository.findAll();
    }

    public Station getOne(Integer stationId) {
         return stationRepository.getOne(stationId);
    }

    public Station getDefaultStation(){
        return stationRepository.findStationByIsDefault(true);
    }

    private Station formConvertToEntity(StationDto form){
        Station station = new Station();
        station.setName(form.getName());
        station.setCity(form.getCity());
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



    private StationDto toDto(Station station){
        StationDto form = new StationDto();
        form.setId(station.getId());
        form.setName(station.getName());
        form.setCity(station.getCity());
        form.setProvince(station.getProvince());
        form.setAttitude(station.getAttitude());
        form.setLongitude(station.getLongitude());
        form.setIsDefault(station.getIsDefault());
        form.setNameKey(station.getNameKey());
        form.setCityCode(station.getCityCode());
        form.setPumpCount(station.getPumpCount());
        form.setRemark(station.getRemark());
        form.setIsDefault(station.getIsDefault());
        form.setEnable(station.getEnable());
        form.setCreateTime(station.getCreateTime());
        return form;
    }

    public Object queryAll(StationQueryCriteria criteria, Pageable pageable) {
        Page<Station> page = stationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(this::toDto));
    }

   @Transactional
    public void create(Station resources) {
        if(stationRepository.findByName(resources.getName()) != null){
            throw new EntityExistException(Menu.class,"name",resources.getName());
        }
        if(StringUtils.isBlank(resources.getCityCode())||StringUtils.isBlank(resources.getAttitude())||StringUtils.isBlank(resources.getLongitude())){
            throw new BadRequestException("城市编码或经纬度参数缺失");
        }
       stationRepository.save(resources);
    }


    public void delete(Set<Integer> stationIdSet) {
        for (Integer s : stationIdSet) {
            stationRepository.updateStationStatus(s);
        }
    }
}
