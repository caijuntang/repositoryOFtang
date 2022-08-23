package com.cooling.hydraulic.dao;

import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.entity.WXUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface WXUserRepository extends JpaRepository<WXUser, Integer>, JpaSpecificationExecutor<WXUser> {

}
