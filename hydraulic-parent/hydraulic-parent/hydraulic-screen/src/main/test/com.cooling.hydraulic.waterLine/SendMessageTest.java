package com.cooling.hydraulic.waterLine;

import com.cooling.hydraulic.utils.ChatSendUtil;
import org.junit.Test;

public class SendMessageTest {

    @Test
    public void getTokenTest(){
        String token = ChatSendUtil.getToKen("wwa1d74de552322ccc", "y3Xn3XE1C6dZuOj3e4G-UoRdzfi6S3FV558iz3lIsh4");
        System.out.println("+++++++++++++++"+token+"+++++++++++++++++");
    }

    @Test
    public void sendMessage(){
        String token="87QmEc1tbwWtL945jqFpExZw1yUtwLjyjrGVjkjdh5uBPuTzLOuIRY2cmT17ai4mMZjHaQpSADLziDj2ezd1MqCMfaI2hzdtdLM3HQZhILTUnSs0O1zpbcvEemWBGNkOUXktcJGMJXte1ve223g4Hcezzq9-NJ8aUhsaLQbP7CTIu-bII2SXhE67FCvML5d4933ad-asP3duekdok2-XyA";
        ChatSendUtil.sendTextMsg("测试消息");
    }
}
