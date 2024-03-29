package com.cooling.hydraulic.requestDto;

public class MessageRequest {
    // 否 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向该企业应用的全部成员发送
    private String touser;
    // 否 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
    private String toparty;
    // 否 标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
    private String totag;
    // 是 消息类型
    private String msgtype;
    //文本
    private Text text;
    //否     表示是否是保密消息，0表示否，1表示是，默认0


    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

}
