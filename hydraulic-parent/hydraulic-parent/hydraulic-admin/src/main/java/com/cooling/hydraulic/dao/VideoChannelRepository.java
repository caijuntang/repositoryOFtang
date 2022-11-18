package com.cooling.hydraulic.dao;

import com.cooling.hydraulic.entity.VideoChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface VideoChannelRepository extends JpaRepository<VideoChannel, Integer>, JpaSpecificationExecutor<VideoChannel> {

    List<VideoChannel> findVideoChannelByStation(Integer stationId);

}
