package com.cooling.hydraulic.service.user;

import cn.hutool.core.util.RandomUtil;
import com.cooling.hydraulic.model.user.JwtUserDto;
import com.cooling.hydraulic.model.user.OnlineUserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserCacheManagerService {

    @Value("${login.user-cache.idle-time}")
    private long idleTime;

    private static final Map<String, JwtUserDto> userCacheMap=new ConcurrentHashMap<String,JwtUserDto>();

    /**
     * 返回用户缓存
     * @param userName 用户名
     * @return JwtUserDto
     */
    public JwtUserDto getUserCache(String userName) {
        if (StringUtils.isNotEmpty(userName)) {
            return userCacheMap.get(userName);
        }
        return null;
    }

    /**
     *  添加缓存到Redis
     * @param userName 用户名
     */
    @Async
    public void addUserCache(String userName, JwtUserDto user) {
        if (StringUtils.isNotEmpty(userName)) {
            // 添加数据, 避免数据同时过期
            long time = idleTime + RandomUtil.randomInt(900, 1800);
            user.setLoginTime(System.currentTimeMillis()/1000);
            userCacheMap.put(userName,user);
        }
    }

    /**
     * 清理用户缓存信息
     * 用户信息变更时
     * @param userName 用户名
     */
    @Async
    public void cleanUserCache(String userName) {
        if (StringUtils.isNotEmpty(userName)) {
            // 清除数据
            userCacheMap.remove(userName);
        }
    }
}