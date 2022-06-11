package com.cooling.hydraulic;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

@Configuration
public class ScreenConfiguration {
    static {
        System.out.printf("===========加载配置文件==============");
    }
//    @Bean
//    public ConfigurableServletWebServerFactory configurableServletWebServerFactory(){
//        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
//        factory.setPort(8088);
//        factory.setUriEncoding(Charset.forName("UTF-8"));
//        return factory;
//    }
}
