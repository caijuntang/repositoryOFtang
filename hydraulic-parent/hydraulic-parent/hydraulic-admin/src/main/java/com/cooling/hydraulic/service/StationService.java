package com.cooling.hydraulic.service;


import com.cooling.hydraulic.dao.StationRepository;
import com.cooling.hydraulic.entity.Station;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
