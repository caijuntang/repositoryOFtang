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
        String url = "http://localhost:8090/openApi/reportWaterLine";
        WaterLine waterLine = new WaterLine();
        waterLine.setForeVal(3.28);
        waterLine.setInsideVal(3.44);
        waterLine.setOutsideVal(5.14);
        String param = JSON.toJSONString(waterLine);
        System.out.println(param);
//        String reqStr="{\"indsideVal\":3.28,\"outsideVal\":5.58,\"fpreVal\":3.33}";
        try {
//            String response = HttpClientUtils.postMethod(url, param);
            String response=HttpClientUtils.getMethod(url+"?insideVal=3.28&outsideVal=5.33&foreVal=3.13");
            System.out.println("=======================================");
            System.out.printf(response);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
