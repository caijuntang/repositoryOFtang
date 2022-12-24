package com.cooling.hydraulic.dao;

import com.cooling.hydraulic.entity.BaseEntity;
import com.cooling.hydraulic.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Integer>, JpaSpecificationExecutor<Station> {


    Station findStationByIsDefaultAndEnableAndStatus(boolean defaultFlag,boolean enableFlag,int statusFlag);

    Station findByName(String name);

    @Modifying
    @Query(value = " update sys_station set is_default = 0 where is_default = 1 ",nativeQuery = true)
    void changeDefalutStation();

    @Modifying
    @Query(value = " update sys_station set status = 0 where id = ?1 ",nativeQuery = true)
    void updateStationStatus(Integer stationId);

    List<Station> findAllByEnableAndStatus(boolean enableFlag,int statusFlag);
}
