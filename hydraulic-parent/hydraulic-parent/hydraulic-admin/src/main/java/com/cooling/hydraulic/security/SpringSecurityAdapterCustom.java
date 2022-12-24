package com.cooling.hydraulic.security;

import com.cooling.hydraulic.config.SecurityProperties;
import com.cooling.hydraulic.service.user.OnlineUserService;
import com.cooling.hydraulic.service.user.UserCacheManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityAdapterCustom extends WebSecurityConfigurerAdapter {

    @Resource
    private TokenProvider tokenProvider;
    @Resource
    private CorsFilter corsFilter;
    @Resource
    private JwtAuthenticationEntryPoint authenticationErrorHandler;
    @Resource
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
   @Resource
   private SecurityProperties properties;
//    @Resource
//    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Resource
    private OnlineUserService onlineUserService;
    @Resource
    private UserCacheManagerService userCacheManagerService;

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 去除 ROLE_ 前缀
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 密码加密方式
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 获取匿名标记
        httpSecurity
                // 禁用 CSRF
                .csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                // 授权异常
                .exceptionHandling()
                .authenticationEntryPoint(authenticationErrorHandler)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                // 防止iframe 造成跨域
                .and()
                .headers()
                .frameOptions()
                .disable()
                // 不创建会话
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 静态资源等等
                .antMatchers(
//                        "**/*.html",
//                        "*.html.gz",
//                        "**/*.html",
//                        "**/*.html.gz",
//                        "**/*.css",
//                        "**/*.css.gz",
//                        "**/*.js",
//                        "**/*.js.gz",
//                        "/dist/static/**",
                        "/dist/**"
                ).permitAll()
                // swagger 文档
                .antMatchers("/swagger-ui.html","/swagger-resources/**","/webjars/**","/*/api-docs","/avatar/**","/file/**","/druid/**").permitAll()
                // 放行OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 自定义匿名访问所有url放行：允许匿名和带Token访问
                .antMatchers("/auth/login","/auth/logout","/openApi/**","/index","/wx/*").permitAll()
                // 所有请求都需要认证
                .anyRequest().authenticated()
                .and().apply(securityConfigurerAdapter());
    }

    private TokenConfigurer securityConfigurerAdapter() {
        return new TokenConfigurer(tokenProvider, onlineUserService, userCacheManagerService);
    }

}
