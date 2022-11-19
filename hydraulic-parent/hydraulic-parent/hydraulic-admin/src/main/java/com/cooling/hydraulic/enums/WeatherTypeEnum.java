package com.cooling.hydraulic.enums;

public enum WeatherTypeEnum {
    /* 旧邮箱修改邮箱 */
    NOW("now", "实时天气"),

    /* 通过邮箱修改密码 */
    All("all", "未来几日天气");

    private final String value;
    private final String name;

    WeatherTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }
}
