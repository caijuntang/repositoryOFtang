package com.cooling.hydraulic.openApi;

import com.cooling.hydraulic.model.WaterLine;
import com.cooling.hydraulic.service.WaterLineService;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequestMapping("/openApi")
public class OpenApi {

    @Autowired
    private WaterLineService waterLineService;

    @RequestMapping(name = "水位上报 ", value = "/reportWaterLine")
    @ResponseBody
    public Object reportWaterLine(Double insideVal,Double outsideVal,Double foreVal,Integer stationId) {
        return waterLineService.reportWaterLine(insideVal, outsideVal, foreVal,stationId);
    }

    @RequestMapping(name = "水位获取 ", value = "/getWaterLine")
    @ResponseBody
    public Object getWaterLine(Integer stationId) {
        return waterLineService.getWaterLineContent(stationId);

    }


    @RequestMapping(name = "回复消息 ", value = "/reply")
    @ResponseBody
    public Object reply(@RequestBody String requestBody,String openid) {
        return "";
    }
}
