package com.cooling.hydraulic.dao;

import com.cooling.hydraulic.entity.Station;
import com.cooling.hydraulic.entity.WXUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface WXUserRepository extends JpaRepository<WXUser, Integer>, JpaSpecificationExecutor<WXUser> {

    @Query(value = "SELECT openid FROM wxgzh_user", nativeQuery = true)
    List<String> findOpenId();
}
