package com.cooling.hydraulic.waterLine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cooling.hydraulic.model.WaterLine;
import com.cooling.hydraulic.utils.HttpClientUtils;
import org.json.JSONArray;
import org.json.JSONStringer;
import org.junit.Test;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WaterLineTest {

    @Test
    public void reportWaterLine() {
//        String url = "http://localhost:8090/screen/openApi/reportWaterLine";
        String url = "http://39.107.91.15:8090/screen/openApi/reportWaterLine";
        WaterLine waterLine = new WaterLine(6.41,6.79,6.41);
        String param = JSON.toJSONString(waterLine);
        System.out.println(param);
//        String reqStr="{\"indsideVal\":3.28,\"outsideVal\":5.58,\"fpreVal\":3.33}";
        try {
//            String response = HttpClientUtils.postMethod(url, param);
            String response=HttpClientUtils.getMethod(url+"?insideVal=6.41&outsideVal=6.79&foreVal=6.42&stationId=1",10000,10000);
            System.out.println("=======================================");
            System.out.printf(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void convertTest(){
        String waterLineContent = "【泵站水位详情】\r\n泵站名称：{stationName}\r\n外河水位：{outsideLine}米\r\n" +
                "前池水位：{foreLine}米\r\n内河水位：{insideLine}米";
        String content = waterLineContent.replace("{stationName}", "龙湖泵站");
        System.out.printf(content);
    }

}
