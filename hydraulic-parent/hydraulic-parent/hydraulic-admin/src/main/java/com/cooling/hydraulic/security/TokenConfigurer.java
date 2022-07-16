package com.cooling.hydraulic.security;

import com.cooling.hydraulic.config.SecurityProperties;
import com.cooling.hydraulic.service.user.OnlineUserService;
import com.cooling.hydraulic.service.user.UserCacheManagerService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;


public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private  TokenProvider tokenProvider;
    private OnlineUserService onlineUserService;
    private  UserCacheManagerService userCacheManager;

    public TokenConfigurer(TokenProvider tokenProvider,OnlineUserService onlineUserService, UserCacheManagerService userCacheManager) {
        this.tokenProvider = tokenProvider;
        this.onlineUserService = onlineUserService;
        this.userCacheManager = userCacheManager;
    }

    @Override
    public void configure(HttpSecurity http) {
        TokenFilter customFilter = new TokenFilter(tokenProvider, onlineUserService, userCacheManager);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
