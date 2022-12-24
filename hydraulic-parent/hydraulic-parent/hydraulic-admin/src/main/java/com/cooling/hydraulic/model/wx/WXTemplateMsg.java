package com.cooling.hydraulic.model.wx;

import java.util.TreeMap;

public class WXTemplateMsg {

    /**
     * 接收者openId
     */
    private String touser;
    /**
     * 模板ID
     */
    private String template_id;

    /**
     * 消息data
     */
    private TreeMap<String, TreeMap<String, String>> data;


    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public TreeMap<String, TreeMap<String, String>> getData() {
        return data;
    }

    public void setData(TreeMap<String, TreeMap<String, String>> data) {
        this.data = data;
    }
}
