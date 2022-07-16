package com.cooling.hydraulic.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Order(Ordered.HIGHEST_PRECEDENCE)
//@WebFilter(filterName = "crossFilter", urlPatterns = "/*")
//@Component
public class CrossFilter {
//public class CrossFilter implements Filter {

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest servletRequest = (HttpServletRequest) request;
//        HttpServletResponse servletResponse = (HttpServletResponse) response;
//        servletResponse.setHeader("Access-Control-Allow-Origin", servletRequest.getHeader("Origin"));
//        servletResponse.setHeader("Access-Control-Allow-Methods", "*");
//        servletResponse.setHeader("Access-Control-Allow-Headers", "*");
//        servletResponse.setHeader("Access-Control-Max-Age", "3600");
//        servletResponse.setHeader("Access-Control-Allow-Credentials", "true");
//        servletResponse.setHeader("Access-Control-Expose-Headers", "*");
//
//        //判断是否为可选择性批量操作的请求，设置状态码返回
//        if ("OPTIONS".equals(servletRequest.getMethod())) {
//            servletResponse.setStatus(HttpStatus.ACCEPTED.value());
//            return;
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//    }

}
