package com.cooling.hydraulic.security;

import cn.hutool.core.util.StrUtil;
import com.cooling.hydraulic.config.SecurityProperties;
import com.cooling.hydraulic.model.user.OnlineUserDto;
import com.cooling.hydraulic.service.user.OnlineUserService;
import com.cooling.hydraulic.service.user.UserCacheManagerService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;


public class TokenFilter extends GenericFilterBean {
    private static final Logger log = LoggerFactory.getLogger(TokenFilter.class);


    private final TokenProvider tokenProvider;
    private final OnlineUserService onlineUserService;
    private final UserCacheManagerService userCacheManagerService;

    /**
     * @param tokenProvider     Token
     * @param onlineUserService 用户在线
     * @param userCacheManagerService   用户缓存工具
     */
    public TokenFilter(TokenProvider tokenProvider, OnlineUserService onlineUserService, UserCacheManagerService  userCacheManagerService) {
        this.onlineUserService = onlineUserService;
        this.tokenProvider = tokenProvider;
        this.userCacheManagerService = userCacheManagerService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            OnlineUserDto onlineUserDto = null;
            boolean cleanUserCache = false;
            try {
                onlineUserDto = onlineUserService.getOne(SecurityProperties.onlineKey + token);
            } catch (ExpiredJwtException e) {
                log.error(e.getMessage());
                cleanUserCache = true;
            } finally {
                if (cleanUserCache || Objects.isNull(onlineUserDto)) {
                    userCacheManagerService.cleanUserCache(String.valueOf(tokenProvider.getClaims(token).get(TokenProvider.AUTHORITIES_KEY)));
                }
            }
            if (onlineUserDto != null && StringUtils.hasText(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Token 续期
                tokenProvider.checkRenewal(token);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityProperties.header);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityProperties.tokenStartWith)) {
            // 去掉令牌前缀
            return bearerToken.replace(SecurityProperties.tokenStartWith, "");
        } else {
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }
}
