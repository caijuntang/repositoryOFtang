package com.cooling.hydraulic.openApi;

import com.cooling.hydraulic.model.WaterLine;
import com.cooling.hydraulic.service.WaterLineService;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/openapi")
public class OpenApi {

    @Autowired
    private WaterLineService waterLineService;

    @RequestMapping(name = "水位获取 ", value = "/getWaterLine.json", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Object getActivityGoods(Double insideVal,Double outsideVal,Double foreVal) {
        return waterLineService.reportWaterLine(insideVal, outsideVal, foreVal);

    }
}
