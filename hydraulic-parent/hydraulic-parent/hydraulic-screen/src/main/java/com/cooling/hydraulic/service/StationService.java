package com.cooling.hydraulic.service;


import com.cooling.hydraulic.config.BaseConfig;
import com.cooling.hydraulic.dao.StationRepository;
import com.cooling.hydraulic.entity.Station;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class StationService {

    @Resource
    private StationRepository stationRepository;



     public  List<Station> findAllStation(){
        return stationRepository.findAll();
    }

    public Station getOne(Integer stationId) {
         return stationRepository.getOne(stationId);
    }
}
