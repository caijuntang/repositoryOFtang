package com.cooling.hydraulic.video;

import com.alibaba.fastjson.JSON;
import com.cooling.hydraulic.utils.HttpClientUtils;
import org.json.JSONString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.ILoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


//@RunWith(SpringRunner.class)
//@SpringBootTest
//@SpringBootConfiguration
public class VideoTest {


    /**
     * 获取用户列表
     */
    @Test
    public void getDviceList() {
        Map<String, String> map = new HashMap<>();
        map.put("1","1");
        map.put("2","2");
        map.put("3","3");
        System.out.printf(JSON.toJSONString(map));
        System.out.println("==============================");


    }

    /**
     * 获取视频直播列表
     * <p>
     * appKey=5e7d7522445c45768161688953c1ca42
     * appSecret=7f084520e03606ade8e69a2e59aee35c
     * accessToken=at.dv0d3u5yaupdng4g5gj5rbjf2uw0nlyo-2kqxkt04m3-1q4x74l-iokzwhxof
     * https://open.ys7.com/api/lapp/live/video/list
     * ezopen://open.ys7.com/J59438499/1.hd.live
     */
    @Test
    public void getLiveList() {
//        String url = "https://open.ys7.com/api/lapp/live/video/list";
        String url = "https://open.ys7.com/api/lapp/v2/live/address/get";

        String appKey = "5e7d7522445c45768161688953c1ca42";
        String appSecret = "7f084520e03606ade8e69a2e59aee35c";
        String accessToken = "at.22spw1h24tnz9eu875p490rv2h2sv84w-4qqc9m6ds9-14ztm5a-enzws3kdi";
        Map<String, String> request = new HashMap<String,String>();
        request.put("accessToken","at.22spw1h24tnz9eu875p490rv2h2sv84w-4qqc9m6ds9-14ztm5a-enzws3kdi");
        request.put("pageStart","0");
        request.put("pageSize","5");
//        String requestStr= JSON.toJSONString(request);
        String requestStr = request.toString();
        String reqStr="accessToken=at.22spw1h24tnz9eu875p490rv2h2sv84w-4qqc9m6ds9-14ztm5a-enzws3kdi&deviceSerial=C39292726";
        System.out.println("==========="+requestStr+"==============");
        try {
            String response = HttpClientUtils.postMethod(url, reqStr);
            System.out.printf(response);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void controlDevice() {
        String url = "https://open.ys7.com/api/lapp/device/ptz/start";
        String reqStr="accessToken=at.c3wpzrlqb77tr7q6b5zq5qo19yk3vppx-77cl79pc4s-1tu9bia-ttgrzh5od&deviceSerial=C39292726&channelNo=6&direction=1&speed=1";
        try {
            String response = HttpClientUtils.postMethod(url, reqStr);
            System.out.println("=======================================");
            System.out.printf(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void controlDeviceStop() {
        String url = "https://open.ys7.com/api/lapp/device/ptz/stop";
        String reqStr="accessToken=at.c3wpzrlqb77tr7q6b5zq5qo19yk3vppx-77cl79pc4s-1tu9bia-ttgrzh5od&deviceSerial=C39292726&channelNo=6&direction=1";
        try {
            String response = HttpClientUtils.postMethod(url, reqStr);
            System.out.println("=======================================");
            System.out.printf(response);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
