package com.cooling.hydraulic.service.user;

import com.cooling.hydraulic.config.SecurityProperties;
import com.cooling.hydraulic.model.user.JwtUserDto;
import com.cooling.hydraulic.model.user.OnlineUserDto;
import com.cooling.hydraulic.utils.EncryptUtils;
import com.cooling.hydraulic.utils.NetWorkUtils;
import com.cooling.hydraulic.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUserService {
    private static final Logger log= LoggerFactory.getLogger(OnlineUserService.class);

    private static final Map<String, OnlineUserDto> userCacheMap=new ConcurrentHashMap<String,OnlineUserDto>();


    /**
     * 保存在线用户信息
     * @param jwtUserDto /
     * @param token /
     * @param request /
     */
    public void save(JwtUserDto jwtUserDto, String token, HttpServletRequest request){
        String dept = "总部";
        String ip = NetWorkUtils.getIp(request);
        String browser = NetWorkUtils.getBrowser(request);
        String address = NetWorkUtils.getCityInfo(ip);
        OnlineUserDto onlineUserDto = null;
        try {
            onlineUserDto = new OnlineUserDto(jwtUserDto.getUsername(), jwtUserDto.getUser().getNickName(), dept, browser , ip, address, EncryptUtils.desEncrypt(token), jwtUserDto.getLoginTime());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        userCacheMap.put(SecurityProperties.onlineKey + token,onlineUserDto);
//        redisUtils.set(properties.getOnlineKey() + token, onlineUserDto, properties.getTokenValidityInSeconds()/1000);
    }

    /**
     * 查询全部数据
     * @param filter /
     * @param pageable /
     * @return /
     */
    public Map<String,Object> getAll(String filter, Pageable pageable){
        List<OnlineUserDto> onlineUserDtos = getAll(filter);
        return PageUtil.toPage(
                PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(), onlineUserDtos),
                onlineUserDtos.size()
        );
    }

    /**
     * 查询全部数据，不分页
     * @param filter /
     * @return /
     */
    public List<OnlineUserDto> getAll(String filter){
        Collection<OnlineUserDto> users = userCacheMap.values();
        List<OnlineUserDto> onlineUserDtos = new ArrayList<>();
        for (OnlineUserDto onlineUserDto : users) {
            if(StringUtils.isNotBlank(filter)){
                if(onlineUserDto.toString().contains(filter)){
                    onlineUserDtos.add(onlineUserDto);
                }
            } else {
                onlineUserDtos.add(onlineUserDto);
            }
        }
        onlineUserDtos.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUserDtos;
    }

    /**
     * 踢出用户
     * @param key /
     */
    public void kickOut(String key){
        key =SecurityProperties.onlineKey + key;
        userCacheMap.remove(key);
    }

    /**
     * 退出登录
     * @param token /
     */
    public void logout(String token) {
        String key = SecurityProperties.onlineKey + token;
        userCacheMap.remove(key);
    }

    /**
     * 导出
     * @param all /
     * @param response /
     * @throws IOException /
     */
//    public void download(List<OnlineUserDto> all, HttpServletResponse response) throws IOException {
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (OnlineUserDto user : all) {
//            Map<String,Object> map = new LinkedHashMap<>();
//            map.put("用户名", user.getUserName());
//            map.put("部门", user.getDept());
//            map.put("登录IP", user.getIp());
//            map.put("登录地点", user.getAddress());
//            map.put("浏览器", user.getBrowser());
//            map.put("登录日期", user.getLoginTime());
//            list.add(map);
//        }
//        FileUtil.downloadExcel(list, response);
//    }

    /**
     * 查询用户
     * @param key /
     * @return /
     */
    public OnlineUserDto getOne(String key) {
       return userCacheMap.get(key);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String igoreToken){
        List<OnlineUserDto> onlineUserDtos = getAll(userName);
        if(onlineUserDtos ==null || onlineUserDtos.isEmpty()){
            return;
        }
        for(OnlineUserDto onlineUserDto : onlineUserDtos){
            if(onlineUserDto.getUserName().equals(userName)){
                try {
                    String token =EncryptUtils.desDecrypt(onlineUserDto.getKey());
                    if(StringUtils.isNotBlank(igoreToken)&&!igoreToken.equals(token)){
                        this.kickOut(token);
                    }else if(StringUtils.isBlank(igoreToken)){
                        this.kickOut(token);
                    }
                } catch (Exception e) {
                    log.error("checkUser is error",e);
                }
            }
        }
    }

    /**
     * 根据用户名强退用户
     * @param username /
     */
    @Async
    public void kickOutForUsername(String username) throws Exception {
        List<OnlineUserDto> onlineUsers = getAll(username);
        for (OnlineUserDto onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(username)) {
                String token =EncryptUtils.desDecrypt(onlineUser.getKey());
                kickOut(token);
            }
        }
    }
}
