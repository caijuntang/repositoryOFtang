package com.cooling.hydraulic.controller;


import com.cooling.hydraulic.service.WaterLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private WaterLineService waterLineService;

    @RequestMapping(name = "水位获取 ", value = "/getWaterLine.json", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Object getActivityGoods() {

        return waterLineService.getWaterLine();
    }
}
