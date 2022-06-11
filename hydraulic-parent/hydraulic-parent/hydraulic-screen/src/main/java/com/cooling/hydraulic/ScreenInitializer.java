package com.cooling.hydraulic;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ScreenInitializer extends SpringBootServletInitializer {
    public static void main(String... args) {
        SpringApplication.run(ScreenInitializer.class, args);
        System.out.println("==============服务启动==================");
    }

}
