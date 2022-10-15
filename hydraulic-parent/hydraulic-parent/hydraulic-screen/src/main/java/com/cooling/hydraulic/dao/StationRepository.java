package com.cooling.hydraulic.dao;

import com.cooling.hydraulic.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StationRepository extends JpaRepository<Station, Integer>, JpaSpecificationExecutor<Station> {


    Station findStationByIsDefault(int defaultFlag);

}
