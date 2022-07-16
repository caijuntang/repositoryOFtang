package com.cooling.hydraulic.openApi;

import com.cooling.hydraulic.service.WaterLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@RequestMapping("/openApi")
public class OpenApi {

    @Autowired
    private WaterLineService waterLineService;

    @RequestMapping(name = "水位上报 ", value = "/reportWaterLine")
    @ResponseBody
    public Object reportWaterLine(Double insideVal,Double outsideVal,Double foreVal) {
        return waterLineService.reportWaterLine(insideVal, outsideVal, foreVal);
    }
}
