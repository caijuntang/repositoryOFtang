package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.service.WaterLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path="/api/control")
public class ControlController {

    @Autowired
    private WaterLineService waterLineService;

//    @RequestMapping("/index")
//    public String index(HttpSession session, HttpServletRequest req, HttpServletResponse resp) {
//        return "index";
//    }

    @RequestMapping("/getWaterLine")
    @ResponseBody
    public Object getWaterLine() {
        return waterLineService.getWaterLine();
    }
}
