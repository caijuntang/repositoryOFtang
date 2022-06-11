package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.service.WaterLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private WaterLineService waterLineService;

    @RequestMapping("/")
    @ResponseBody
    public String index() {

        return "hello world";
    }

    @RequestMapping("/getWaterLine")
//    @ResponseBody
    public Object getWaterLine() {
        return waterLineService.getWaterLine();
    }
}
