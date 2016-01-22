package com.znb.java.example;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author zhangnaibin@xiaomi.com
 * @time 2016-01-20 下午8:59
 *
 * <dependency>
    <groupId>org.jruby</groupId>
    <artifactId>jruby-complete</artifactId>
    <version>1.6.5</version>
 * </dependency>
 *
 */
public class DateTimeExample {
    public static void main(String[] args) {
        DateTime dateTime = new DateTime();
        dateTime.toString("yyyy-MM-dd");

        DateTime dateTime2 = new DateTime();
        dateTime2 = dateTime2.plusDays(2); // 后两天，改为-2为提前两天
        dateTime2 = dateTime2.plusHours(2); // 后两个小时，改为-2为前两个小时
        dateTime2 = dateTime2.plusYears(2); // 两年后，改为-2为两年前
        dateTime2 = dateTime2.minusDays(2); // 2天前
        Calendar cal = dateTime2.toCalendar(new Locale("zh", "ZH")); // 转为Calendar格式
        Date date = dateTime.toDate(); // 转为java的Date格式
        System.out.println(dateTime2.toString("yy/MM/dd HH:mm:ss EE"));
    }
}
