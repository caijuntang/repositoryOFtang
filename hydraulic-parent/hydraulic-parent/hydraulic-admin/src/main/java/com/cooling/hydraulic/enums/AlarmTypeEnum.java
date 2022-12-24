package com.cooling.hydraulic.enums;

public enum AlarmTypeEnum {
    /* 旧邮箱修改邮箱 */
    WATERLINE("waterLine", "水位告警"),

    /* 通过邮箱修改密码 */
    PUMP("pump", "泵机告警");

    private final String value;
    private final String name;

    AlarmTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
