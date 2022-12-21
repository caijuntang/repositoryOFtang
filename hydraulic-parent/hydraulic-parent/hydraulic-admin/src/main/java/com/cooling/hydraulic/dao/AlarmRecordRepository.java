package com.cooling.hydraulic.dao;

import com.cooling.hydraulic.entity.AlarmRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmRecordRepository extends JpaRepository<AlarmRecord, Integer>, JpaSpecificationExecutor<AlarmRecord> {

    @Query(value = "SELECT * FROM alarm_record WHERE status=1 and station_id = ?1 and create_time > ?2 order by create_time desc " ,nativeQuery = true)
    List<AlarmRecord> findRecentByAndStation(Integer stationId, LocalDateTime recentDay);
}
