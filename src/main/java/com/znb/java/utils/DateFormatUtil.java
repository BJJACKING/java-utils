package com.znb.java.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 线程安全的日期转换函数
 * @author zhangnaibin
 * @Date 2015-12-18 上午11:01
 */
public class DateFormatUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(DateFormatUtil.class);
    public static final String FORMAT1 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT2 = "yy/MM/dd HH:mm:ss";
    public static final String FORMAT3 = "yy/MM/dd HH:mm:ss pm";
    public static final String FORMAT4 = "yy-MM-dd HH:mm:ss";
    public static final String FORMAT5 = "yyyy-MM-dd";
    public static final String FORMAT_UTL = "http://java.sun.com/docs/books/tutorial/i18n/format/simpleDateFormat.html";

    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();

    public static DateFormat getDateFormat(String format) {
        DateFormat df = threadLocal.get();
        if (null == df) {
            df = new SimpleDateFormat(format);
            threadLocal.set(df);
        }
        return df;
    }

    /**
     * 将date转为固定的日期格式
     * @param date date对象
     * @param format 需要转换的时间格式
     * @return
     */
    public static String formatDate(Date date, String format) {
        return getDateFormat(format).format(date);
    }

    /**
     * 将时间转换为Date对象
     * @param strDate 时间string
     * @param format 时间格式
     * @return
     */
    public static Date parse(String strDate, String format) {
        try {
            return getDateFormat(format).parse(strDate);
        } catch (Exception e) {
            LOG.error("paser data exception, date:{} exception:{}", strDate, e);
            return new Date();
        }
    }

    /**
     * 获取结束时间,默认一天前
     * @param date
     * @return
     */
    public static String getEndTime(String date) {
        if (StringUtils.isEmpty(date)) {
            DateTime dt = new DateTime(System.currentTimeMillis());
            dt = dt.minusDays(1);
            date = dt.toString(FORMAT1);
        }
        return date;
    }

    /**
     * 获取开始时间,默认3个月前
     * @param date
     * @return
     */
    public static String getStartDate(String date) {
        if (StringUtils.isEmpty(date)) {
            DateTime dt = new DateTime(System.currentTimeMillis());
            dt = dt.minusMonths(1);
            date = dt.toString(FORMAT1);
        }
        return date;
    }

    public static void main(String[] args) {
        System.out.println(DateFormatUtil.parse("2016-12-04 12:24:35", DateFormatUtil.FORMAT4).getTime());
    }
}
