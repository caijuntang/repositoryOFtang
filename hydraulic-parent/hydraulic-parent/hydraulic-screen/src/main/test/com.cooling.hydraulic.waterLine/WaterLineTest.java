package com.cooling.hydraulic.waterLine;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.model.WaterLine;
import com.cooling.hydraulic.utils.HttpClientUtils;
import org.json.JSONStringer;
import org.junit.Test;
import sun.net.www.http.HttpClient;

import java.io.IOException;

public class WaterLineTest {

    @Test
    public void reportWaterLine() {
        String url = "http://localhost:8090/screen/openApi/reportWaterLine";
//        String url = "http://47.102.142.109:8090/screen/openApi/reportWaterLine";
        WaterLine waterLine = new WaterLine(3.44,5.15,3.28);
        String param = JSON.toJSONString(waterLine);
        System.out.println(param);
//        String reqStr="{\"indsideVal\":3.28,\"outsideVal\":5.58,\"fpreVal\":3.33}";
        try {
//            String response = HttpClientUtils.postMethod(url, param);
            String response=HttpClientUtils.getMethod(url+"?insideVal=3.28&outsideVal=5.33&foreVal=3.13&stationId=1",10000,10000);
            System.out.println("=======================================");
            System.out.printf(response);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
