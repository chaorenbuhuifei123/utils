package mc.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author machao
 * @CreateDate 2019/9/18 10:44
 * @Version 1.0
 * @Describe
 */
public class DateTimeUtil {

    public static final String COMPLETE_DATE_TIME_FORMAT = "yyyyMMdd HH:mm:ss";

    public static final String COMPLETE_DATE_TIME_FORMAT_2 = "yyyy/MM/dd HH:mm:ss";

    public static final String COMPLETE_DATE_TIME_FORMAT_3 = "yyyy-MM-dd HH:mm:ss";

    public static final String COMPLETE_DATE_FORMAT = "yyyyMMdd";

    public static final String COMPLETE_DATE_FORMAT_2 = "yyyy/MM/dd";

    public static final String COMPLETE_TIME_FORMAT = "HH:mm:ss";

    public static final String COMPLETE_CHINESE_DATE_FORMAT = "yyyy年MM月dd日";

    public static final String SIMPLE_CHINESE_DATE_FORMAT = "MM月dd日";

    /**
     * 获取时间戳
     *
     * @param time   时间字符串
     * @param format 日期格式
     * @return
     */
    public static Long getTimeStamp(String time, String format) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(format)).
                toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取时间戳
     *
     * @param localDateTime 时间
     * @return
     */
    public static Long getTimeStamp(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 将日期转换为规定格式的字符串时间
     *
     * @param localDate 日期
     * @param format    日期格式
     * @return
     */
    public static String localDateToStr(LocalDate localDate, String format) {
        return localDate.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 将日期时间转换为规定格式的字符串日期时间
     *
     * @param localDateTime 日期时间
     * @param format        日期时间格式
     * @return
     */
    public static String localDateTimeToStr(LocalDateTime localDateTime, String format) {
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 将老的日期格式转换为新的日期格式输出
     *
     * @param date      日期
     * @param oldFormat 老的日期格式
     * @param newFormat 新的日期格式
     * @return
     */
    public static String formatDateConvert(String date, String oldFormat, String newFormat) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(oldFormat)).format(DateTimeFormatter.ofPattern(newFormat));
    }

    /**
     * 将Date类型转为LocalDate
     *
     * @param date Date日期类型
     * @return {LocalDate}
     */
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }
}
