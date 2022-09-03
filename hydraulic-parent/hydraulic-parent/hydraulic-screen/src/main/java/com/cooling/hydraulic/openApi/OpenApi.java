package com.cooling.hydraulic.openApi;

import com.cooling.hydraulic.model.PumpDataModel;
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

    @RequestMapping(name = "水泵信息上报 ", value = "/reportPumpData")
    @ResponseBody
    public Object reportPumpData(Integer stationId, PumpDataModel model) {
        return waterLineService.reportPumpData(stationId,model);
    }

    @RequestMapping(name = "水泵信息获取 ", value = "/getPumpData")
    @ResponseBody
    public Object getPumpData(Integer stationId, Integer pumpNo) {
        return waterLineService.getPumpData(stationId,pumpNo);
    }

    @RequestMapping(name = "水位获取 ", value = "/getWaterLine")
    @ResponseBody
    public Object getWaterLine(Integer stationId) {
        return waterLineService.getWaterLineObject(stationId);

    }
}
