package mc.utils;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * 时间处理帮助类
 * <p>
 * 建议使用org.apache.commons.lang.time.DateUtils
 *
 * @author machao
 * @date 2014/8/13.
 */
public class TimeUtil {
    public static String YEARMMDDHHMMSS_FORMAT = "yyyyMMddHHmmss";
    public static String YEARMMDD_HHMMSS_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String YEARMMDD_DATE_FORMAT = "yyyyMMdd";
    public static String YEAR_MM_DD_DATE_FORMAT = "yyyy-MM-dd";
    public static String TIME_CHINESE_FORMAT = "yyyy年MM月dd日";
    public static String TIME_HHMM_FORMAT = "HH:mm";

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat NEW_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat YEARMMDDHHMMSS = new SimpleDateFormat(YEARMMDDHHMMSS_FORMAT);
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat(YEARMMDD_DATE_FORMAT);
    public static final SimpleDateFormat TIME_CHINESE_DATE = new SimpleDateFormat(TIME_CHINESE_FORMAT);
    private static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);

    public static String getNewTime() {
        return getCurrentTimeInString(NEW_DATE_FORMAT);
    }

    public static String getNowString(String dateFormat) {
        return getFormatDateTime(new Date(), dateFormat);
    }

    /**
     * 日期格式相互转换
     *
     * @param date    字符串日期
     * @param fFormat 目前的日期格式
     * @param tFormat 要转换成的格式
     * @return 返回转换后的日期
     * @throws ParseException
     */
    public static String dateFormat(String date, String fFormat, String tFormat) {
        try {
            SimpleDateFormat fd = new SimpleDateFormat(fFormat);
            SimpleDateFormat td = new SimpleDateFormat(tFormat);
            Date d = fd.parse(date);
            return td.format(d);
        } catch (Exception e) {
            logger.error("出错啦!", e);
            return null;
        }
    }

    /**
     * 把日期类型的字符串，转换成日期类型
     *
     * @param dateStr
     * @return
     * @author machao
     * @Time 2014/8/13.
     */
    public static Date parseDate(String dateStr) {
        try {
            return DateUtils.parseDate(dateStr, new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyyMMddHHmmss", "yyyy/MM/dd", "yyyy年MM月dd日"});
        } catch (ParseException e) {
            logger.error("出错啦!", e);
            return null;
        }
    }


    /**
     * 把时间戳转成字符串
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * 把日期转成字符串
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String getTime(Date date, SimpleDateFormat dateFormat) {
        return dateFormat.format(date);
    }

    /**
     * 把时间戳转成字符串，用默认的格式
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 把日期转成字符串，用默认的格式
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {
        return getTime(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间，字符串格式，默认的格式
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * 获取当前时间，字符串形式
     *
     * @param dateFormat
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 获取某天的开始时间
     *
     * @param date 日期
     * @return 某天的开始时间
     */
    public static Date getBeginTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某天的结束时间
     *
     * @param date 日期
     * @return 某天的结束时间
     */
    public static Date getEndTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 偏移天
     *
     * @param date    日期
     * @param offsite 偏移天数，正数向未来偏移，负数向历史偏移
     * @return 偏移后的日期
     */
    public static Date offsiteDay(Date date, int offsite) {
        return offsiteDate(date, Calendar.DAY_OF_YEAR, offsite);
    }

    /**
     * 偏移周
     *
     * @param date    日期
     * @param offsite 偏移周数，正数向未来偏移，负数向历史偏移
     * @return 偏移后的日期
     */
    public static Date offsiteWeek(Date date, int offsite) {
        return offsiteDate(date, Calendar.WEEK_OF_YEAR, offsite);
    }

    /**
     * 偏移月
     *
     * @param date    日期
     * @param offsite 偏移月数，正数向未来偏移，负数向历史偏移
     * @return 偏移后的日期
     */
    public static Date offsiteMonth(Date date, int offsite) {
        return offsiteDate(date, Calendar.MONTH, offsite);
    }

    /**
     * 获取指定日期偏移指定时间后的时间
     *
     * @param date          基准日期
     * @param calendarField 偏移的粒度大小（小时、天、月等）使用Calendar中的常数
     * @param offsite       偏移量，正数为向后偏移，负数为向前偏移
     * @return 偏移后的日期
     */
    public static Date offsiteDate(Date date, int calendarField, int offsite) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarField, offsite);
        return cal.getTime();
    }

    /**
     * 获取今天日期
     *
     * @param timeStyle 定义时间的风格，如yyyyMMdd，yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String todayTime(String timeStyle) {
        String time = null;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat(timeStyle);
        time = fmt.format(now.getTime());
        return time;
    }


    /**
     * 获取昨天日期
     *
     * @param timeStyle 定义时间的风格，如yyyyMMdd，yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String yesterdayTime(String timeStyle) {
        String time = null;
        try {
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DATE, -1);
            SimpleDateFormat fmt = new SimpleDateFormat(timeStyle);
            time = fmt.format(now.getTime());
        } catch (Exception e) {
            logger.info("获取昨天的时间 time error:", e);
        }
        return time;
    }

    /**
     * 当年的前一年元旦
     */
    public static String getBeforeYear() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        return c.get(Calendar.YEAR) + "0101";
    }


    /**
     * 得到格式化后的日，格式为yyyyMMdd，如20060210
     *
     * @param currDate 要格式化的日期
     * @return String 返回格式化后的日，格式为yyyyMMdd，如20060210
     * @see #(java.util.Date, String )
     */
    public static String getFormatDay(java.util.Date currDate) {
        return getFormatDateTime(currDate, YEARMMDD_DATE_FORMAT);
    }

    /**
     * 根据格式得到格式化后的时间
     *
     * @param currDate 要格式化的时间
     * @param format   时间格式，如yyyy-MM-dd HH:mm:ss
     * @return String 返回格式化后的时间，如yyyy-MM-dd
     */
    public static String getFormatDateTime(java.util.Date currDate, String format) {
        if (currDate == null) {
            return "";
        }
        SimpleDateFormat dtFormatdB = null;
        try {
            dtFormatdB = new SimpleDateFormat(format);
            return dtFormatdB.format(currDate);
        } catch (Exception e) {
            dtFormatdB = new SimpleDateFormat(YEARMMDD_HHMMSS_FORMAT);
            try {
                return dtFormatdB.format(currDate);
            } catch (Exception ex) {
                logger.info("DateUtil.getFormatDateTime Exception", ex);
            }
        }
        return "";
    }

    /**
     * 根据格式得到格式化后的日期
     *
     * @param currDate 要格式化的日期
     * @param format   日期格式，如yyyy-MM-dd
     * @return Date 返回格式化后的日期，如yyyy-MM-dd
     */
    public static Date getFormatDate(String currDate, String format) {
        if (currDate == null) {
            return null;
        }
        SimpleDateFormat dtFormatdB = null;
        try {
            dtFormatdB = new SimpleDateFormat(format);
            return dtFormatdB.parse(currDate);
        } catch (Exception e) {
            logger.info("DateUtil.getFormatDate Exception", e);
            dtFormatdB = new SimpleDateFormat(YEARMMDD_HHMMSS_FORMAT);
            try {
                return dtFormatdB.parse(currDate);
            } catch (Exception ex) {
                logger.info("DateUtil.getFormatDate Exception", ex);
            }
        }
        return null;
    }

    /**
     * 获取当前时间
     *
     * @return返回短时间格式 yyyyMMddHHmmss
     */
    public static String getNowYDate() {
        Date currentTime = new Date();
        String dateString = YEARMMDDHHMMSS.format(currentTime);
        return dateString;
    }

    /**
     * 获得当前日期的整数型
     *
     * @return
     * @author Liu zhilai
     * Aug 25, 2014
     * String
     */
    public static int getDateInt() {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(sf.format(date));
    }


    /**
     * 计算两个日期时间相差小时
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long hourDiffer(Date startDate, Date endDate) {
        try {
            long nhour = 1000 * 60 * 60;
            long msecDiffer = startDate.getTime() - endDate.getTime();
            return msecDiffer / nhour;
        } catch (Exception e) {
            throw new RuntimeException("计算两个日期相差小时", e);
        }
    }

    /**
     * 计算两个日期时间相差分钟
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long minDiffer(Date startDate, Date endDate) {
        try {
            long nday = 1000 * 24 * 60 * 60;
            long nhour = 1000 * 60 * 60;
            long nmin = 1000 * 60;
            long msecDiffer = startDate.getTime() - endDate.getTime();
            return msecDiffer % nday % nhour / nmin;
        } catch (Exception e) {
            throw new RuntimeException("计算两个日期相差分钟", e);
        }
    }

    /**
     * 计算两个日期时间相差秒
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long secDiffer(Date startDate, Date endDate) {
        try {
            long nday = 1000 * 24 * 60 * 60;
            long nhour = 1000 * 60 * 60;
            long nmin = 1000 * 60;
            long nsec = 1000;
            long msecDiffer = startDate.getTime() - endDate.getTime();
            return msecDiffer % nday % nhour % nmin / nsec;
        } catch (Exception e) {
            throw new RuntimeException("计算两个日期相差秒", e);
        }
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startTime 较小的时间
     * @param endTime  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date startTime, Date endTime) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            startTime = sdf.parse(sdf.format(startTime));
            endTime = sdf.parse(sdf.format(endTime));
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            long time1 = cal.getTimeInMillis();
            cal.setTime(endTime);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            throw new RuntimeException("计算两个日期相差天数", e);
        }
    }

    /**
     * 计算两个日期相差周数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int weekOffset(Date startTime, Date endTime) {
        try {

            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayOfWeek == 0) {
                dayOfWeek = 7;
            }
            int dayOffset = 0;
            dayOffset = daysBetween(startTime, endTime);
            int weekOffset = dayOffset / 7;
            int index;
            if (dayOffset > 0) {
                index = (dayOffset % 7 + dayOfWeek > 7) ? 1 : 0;
            } else {
                index = (dayOfWeek + dayOffset % 7 < 1) ? -1 : 0;
            }
            weekOffset = weekOffset + index;
            return weekOffset;
        }catch (Exception e){
            throw new RuntimeException("计算两个日期相差周数", e);
        }
    }

    /**
     * 计算两个日期相差几年
     *
     * @param begin
     *            开始时间
     * @param end
     *            结束时间
     * @param justCareYear
     *            是否只关注年<br>
     *            （计算周岁传 false）
     */
    public static int spanOfYears(Date begin, Date end, boolean justCareYear) {
        Calendar calBegin = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        calBegin.setTime(begin);
        calEnd.setTime(end);
        int beginYear = calBegin.get(Calendar.YEAR);
        int endYear = calEnd.get(Calendar.YEAR);
        if (!justCareYear) {
            int beginMonth = calBegin.get(Calendar.MONTH);
            int endMonth = calEnd.get(Calendar.MONTH);
            if (endMonth < beginMonth) {
                return endYear - beginYear - 1;
            } else if (endMonth == beginMonth) {
                int beginDay = calBegin.get(Calendar.DAY_OF_MONTH);
                int endDay = calEnd.get(Calendar.DAY_OF_MONTH);
                if (endDay < beginDay) {
                    return endYear - beginYear - 1;
                }
            }
        }
        return endYear - beginYear;
    }

    /**
     * 判断指定时间是否在指定时间段之间
     * @param day 指定时间
     * @param beginDate 时间段：开始时间 格式：20190422
     * @param endDate 时间段：结束时间 格式：20190909
     * @return true /false
     */
    public static Boolean dayBetweenDay(LocalDate day, String beginDate, String endDate) {
        try {
            LocalDate beginDay = LocalDate.parse(beginDate, DateTimeFormatter.BASIC_ISO_DATE);
            LocalDate endDay = LocalDate.parse(endDate, DateTimeFormatter.BASIC_ISO_DATE);
            return day.isAfter(beginDay) && day.isBefore(endDay);
        } catch (Exception e) {
            throw new RuntimeException("判断指定时间是否在指定时间段之间出错", e);
        }
    }

    /**
     * 获取当前时间
     * @return
     */
    public static int getHour()
    {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * 获取今天的开始时间 00:00:00
     * @return
     */
    public static Calendar getTodayBegin(){
        Calendar day = Calendar.getInstance();
        day.set(Calendar.HOUR_OF_DAY,0);
        day.set(Calendar.MINUTE,0);
        day.set(Calendar.SECOND,0);
        return day;
    }

    /**
     * 获取今天的结束时间 23:59:59
     * @return
     */
    public static Calendar getTodayEnd(){
        Calendar day = Calendar.getInstance();
        day.set(Calendar.HOUR_OF_DAY,23);
        day.set(Calendar.MINUTE,59);
        day.set(Calendar.SECOND,59);
        return day;
    }

    /**
     * 获取当前月的最后一天 23：59：59
     * @return
     */
    public static Date getDateAtEndOfMonth(){
        LocalDate lastLocalDayofMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = lastLocalDayofMonth.atTime(23,59,59,999999999).atZone(zone).toInstant();
        Date dateOfEndMonth= Date.from(instant);
        logger.info("获取当前月的最后一天："+ getFormatDateTime(dateOfEndMonth,YEARMMDD_HHMMSS_FORMAT));
        return dateOfEndMonth;
    }

    /**
     * 获取时间转换为周几
     * @return
     */
    public static Integer getWeekOfDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int i = cal.get(Calendar.DAY_OF_WEEK)-1;
        return i;
    }

    /**
     * 获取日期之间的所有日期
     * @param begin_date yyyyMMdd 格式
     * @param end_date   yyyyMMdd 格式
     * @return
     */
    public static List<String> daysBetween(String begin_date, String end_date) {
        List<String> list = new ArrayList<>();
        if(StringUtil.isNotBlank(begin_date)&&StringUtil.isNotBlank(end_date)){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(YEARMMDD_DATE_FORMAT);
            LocalDate startLocalDate = LocalDate.parse(begin_date, dtf);
            LocalDate endLocalDate = LocalDate.parse(end_date,dtf);
            long distance = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
            if (distance < 0) {
                return list;
            }
            Stream.iterate(startLocalDate, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> {
                list.add(f.format(dtf));
            });
            return list;
        }
        return list;
    }

    /**
     * 获取当前时间是否在指定时间之内
     * @param nowDate
     * @param beginDate
     * @param endDate
     * @return
     */
    public static boolean betweenTime(Date nowDate, String beginDate, String endDate) {
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat(TimeUtil.TIME_HHMM_FORMAT);
        try {
            //格式化当前时间格式
            Date nowTime = df.parse(df.format(nowDate));
            //定义开始时间
            Date beginTime = df.parse(beginDate);
            //定义结束时间
            Date endTime = df.parse(endDate);
            //设置当前时间
            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);
            //设置开始时间
            Calendar begin = Calendar.getInstance();
            begin.setTime(beginTime);
            //设置结束时间
            Calendar end = Calendar.getInstance();
            end.setTime(endTime);
            //处于开始时间之后，和结束时间之前的判断
            if (date.after(begin) && date.before(end)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.info("判断失败");
        }
        return false;
    }
}
