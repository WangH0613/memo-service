package cn.com.koriesh.memo.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateUtil {

    /**
     * 仅显示年月日，例如 2015-08-11.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 显示年月日时分秒，例如 2015-08-11 09:51:53.
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 仅显示时分秒，例如 09:51:53.
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 得到当前日期字符串
     *
     * @return String 日期字符串，例如2015-08-11
     * @since 1.0
     */
    public static String getDate() {
        return getDate(DATE_FORMAT);
    }

    /**
     * 得到当前时间字符串
     *
     * @return String 时间字符串，例如 09:51:53
     * @since 1.0
     */
    public static String getTime() {
        return formatDate(new Date(), TIME_FORMAT);
    }

    /**
     * 得到当前日期和时间字符串
     *
     * @return String 日期和时间字符串，例如 2015-08-11 09:51:53
     * @since 1.0
     */
    public static String getDateTime() {
        return formatDate(new Date(), DATE_TIME_FORMAT);
    }

    /**
     * 获取当前时间指定格式下的字符串
     *
     * @param pattern 转化后时间展示的格式，例如"yyyy-MM-dd"，"yyyy-MM-dd HH:mm:ss"等
     * @return String 格式转换之后的时间字符串.
     * @since 1.0
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 获取指定日期的字符串格式
     *
     * @param date    需要格式化的时间，不能为空
     * @param pattern 时间格式，例如"yyyy-MM-dd"，"yyyy-MM-dd HH:mm:ss"等
     * @return String 格式转换之后的时间字符串.
     * @since 1.0
     */
    public static String getDate(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 获取日期时间字符串，默认格式为（yyyy-MM-dd）
     *
     * @param date    需要转化的日期时间
     * @param pattern 时间格式，例如"yyyy-MM-dd" "HH:mm:ss" "E"等
     * @return String 格式转换后的时间字符串
     * @since 1.0
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, DATE_FORMAT);
        }
        return formatDate;
    }

    /**
     * 获取当前年份字符串
     *
     * @return String 当前年份字符串，例如 2015
     * @since 1.0
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 获取当前月份字符串
     *
     * @return String 当前月份字符串，例如 08
     * @since 1.0
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }


    public static synchronized Date datetimeStrToDate(String datetimeStr) {
        if (datetimeStr == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            return simpleDateFormat.parse(datetimeStr);
        } catch (ParseException pe) {
            log.error(pe.getMessage());
            return null;
        }
    }
}
