package com.cooling.hydraulic.dao;

import com.cooling.hydraulic.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface StationRepository extends JpaRepository<Station, Integer>, JpaSpecificationExecutor<Station> {


    Station findStationByIsDefault(boolean defaultFlag);

    Station findByName(String name);

    @Query(value = " update sys_station set is_default = false where id  != ?1 ",nativeQuery = true)
    void changeDefalutStation(Integer stationId);

    @Query(value = " update sys_station set status = 0 where id = ?1 ",nativeQuery = true)
    void updateStationStatus(Integer stationId);

}
