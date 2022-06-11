package com.cooling.hydraulic.service;

import com.cooling.hydraulic.model.WaterLine;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WaterLineService {
    public static Double insideVal = 0.01;
    public static Double outsideVal = 0.01;
    public static Double foreVal = 0.01;

    public Map<String, String> reportWaterLine(Double insideLine, Double outsideLine, Double foreLine) {
        Map<String, String> result = new HashMap<>();
        insideVal = insideLine;
        outsideVal = outsideLine;
        foreVal = foreLine;
        result.put("code", "200");
        result.put("msg", "report success!");
        return result;
    }

    public Map<String, Object> getWaterLine() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", "200");
        result.put("insideVal", insideVal);
        result.put("insideVal", outsideVal);
        result.put("insideVal", insideVal);
        return result;
    }
}
