package com.cooling.hydraulic.dao;

import com.cooling.hydraulic.entity.AlarmConfig;
import com.cooling.hydraulic.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlarmConfigRepository  extends JpaRepository<AlarmConfig, Integer>, JpaSpecificationExecutor<AlarmConfig> {

    AlarmConfig findAlarmConfigByStation(Station station);

    AlarmConfig findAlarmConfigByStationAndStatus(Station station,boolean statusFlag);

    @Modifying
    @Query(value = " update alarm_config set status = ?1 where id = ?2 ",nativeQuery = true)
    void updateStatusById(boolean status, Integer id);


    List<AlarmConfig> findByStatus(boolean status);
}
