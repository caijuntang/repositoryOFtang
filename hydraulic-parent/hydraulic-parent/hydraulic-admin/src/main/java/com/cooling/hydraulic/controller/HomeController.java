package com.cooling.hydraulic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @RequestMapping("/index")
    public String index(HttpSession session, HttpServletRequest req, HttpServletResponse resp) {
        return "index";
    }
}
