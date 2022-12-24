package com.cooling.hydraulic.waterLine;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.model.PumpDataForm;
import com.cooling.hydraulic.model.WaterLine;
import com.cooling.hydraulic.requestDto.qyRequest.PumpDataRequest;
import com.cooling.hydraulic.utils.HttpClientUtil;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WaterLineTest {

    @Test
    public void reportWaterLine() {
        String url = "http://localhost:8080/admin/openApi/reportWaterLine";
//        String url = "http://39.107.91.15:8080/admin/openApi/reportWaterLine";
        WaterLine waterLine = new WaterLine(6.41,1.98,6.35,false);
        String param = JSON.toJSONString(waterLine);
        System.out.println(param);
//        String reqStr="{\"indsideVal\":3.28,\"outsideVal\":5.58,\"fpreVal\":3.33}";
        try {
//            String response = HttpClientUtils.postMethod(url, param);
            String response= HttpClientUtil.getMethod(url+"?insideVal=10.88&outsideVal=1.98&foreVal=6.38&stationId=2",10000,10000);
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

    @Test
    public void test1(){
        Map<Integer, String> map = new HashMap<>();
        map.put(2,"b");
        map.put(3,"c");
        map.put(4,"d");
        map.put(1,"a");
        Set<Map.Entry<Integer, String>> entries = map.entrySet();
        for (Map.Entry<Integer,String> e:entries){
            System.out.println(e.getKey()+"================"+e.getValue());
        }
        Collection<String> values = map.values();
        System.out.printf(values.toString());
    }

    @Test
    public void test2(){
        Double foreLine=6.42;
        Double insideLine=null;
        BigDecimal foreBigDecimal = BigDecimal.valueOf(foreLine);
        foreBigDecimal= foreBigDecimal.add(BigDecimal.valueOf(0.02));
//        insideLine = foreBigDecimal.setScale(2, RoundingMode.UP).doubleValue();
        insideLine = foreBigDecimal.doubleValue();
        System.out.printf("=========insideLine:"+insideLine);
    }


    @Test
    public void sendPumpData(){
//        String url = "http://localhost:8080/admin/openApi/reportPumpData";
        String url = "http://39.107.91.15:8080/admin/openApi/reportPumpData";
        PumpDataRequest pumpDataRequest = new PumpDataRequest();
        pumpDataRequest.setStationId(1);
        PumpDataForm m = new PumpDataForm();
        m.setPumpNo(1);
        m.setVa("5834.1");
        m.setVb("5847.4");
        m.setVc("5837.9");

//        m.setPumpNo(2);
//        m.setVa("5840.3");
//        m.setVb("5839.9");
//        m.setVc("5841.5");

//        m.setPumpNo(3);
//        m.setVa("5885.7");
//        m.setVb("5893.7");
//        m.setVc("5745.3");

//        m.setPumpNo(4);
//        m.setVa("5857.6");
//        m.setVb("5839.4");
//        m.setVc("5842.9");

        m.setAa("0.00");
        m.setAb("0.00");
        m.setAc("0.00");
        pumpDataRequest.setPumpData(m);
        String requestStr = JSON.toJSONString(pumpDataRequest);
        System.out.printf(requestStr);
        try {
            String response= HttpClientUtil.postMethod(url,requestStr,"application/json");
            System.out.printf(response);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
