package com.cooling.hydraulic.dao;

import com.cooling.hydraulic.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface StationRepository extends JpaRepository<Station, Integer>, JpaSpecificationExecutor<Station> {


    Station findStationByIsDefault(int defaultFlag);

    @Query(value = " update sys_station set is_default = 0 where is_default = 1 ",nativeQuery = true)
    void changeDefalutStation();

}
