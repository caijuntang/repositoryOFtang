package com.cooling.hydraulic.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATETIME_FORMATTER =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 获取当前系统日期
     * @return
     */
    public static LocalDate getNowDate() {
        return LocalDate.now();
    }

    /**
     * 获取当前系统日期时间
     * @return
     */
    public static LocalDateTime getNowDateTime() {
        return LocalDateTime.now();
    }


    /**
     * 获取当前系统日期字符串
     * @return
     */
    public static String getNowDateString() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * 获取当前系统日期时间字符串
     * @return
     */
    public static String getNowDateTimeString() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }


    /**
     * 字符串转LocalDate
     * @param date
     * @return
     */
    public static LocalDate  string2LocalDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    /**
     * 字符串转LocalDateTime
     * @param dateTime
     * @return
     */
    public static LocalDateTime string2LocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATETIME_FORMATTER);
    }
}
