/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.cooling.hydraulic.service.user;

import com.cooling.hydraulic.exception.BadRequestException;
import com.cooling.hydraulic.exception.EntityNotFoundException;
import com.cooling.hydraulic.model.user.JwtUserDto;
import com.cooling.hydraulic.model.user.UserLoginDto;
import com.cooling.hydraulic.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {
    @Autowired
    private  UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private  UserCacheManagerService userCacheManagerservice;

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = userCacheManagerservice.getUserCache(username);
        if(jwtUserDto == null){
            UserLoginDto user;
            try {
                user = userService.getLoginData(username);
            } catch (EntityNotFoundException e) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw new UsernameNotFoundException(username, e);
            }
            if (user == null) {
                throw new UsernameNotFoundException("");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活！");
                }
                jwtUserDto=new JwtUserDto();
                jwtUserDto.setUser(user);
                jwtUserDto.setAuthorities(roleService.mapToGrantedAuthorities(user));
                // 添加缓存数据
                userCacheManagerservice.addUserCache(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}
