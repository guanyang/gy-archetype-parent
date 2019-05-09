package ${package}.util.convert;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期类型与字符串类型相互转换
 * 
 */
public class DateUtil {
    /** 记录日志 */
    private static final Logger  logger                   = LoggerFactory.getLogger(DateUtil.class);

    /** 24小时制 yyyy-MM-dd HH:mm:ss */
    public static final String   DATETIMEPATTERN24H       = "yyyy-MM-dd HH:mm:ss";
    /** 24小时制 yyyyMMddHHmmss */
    public static final String   DATETIMEPATTERN24HSTRING = "yyyyMMddHHmmss";
    /** Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of December in the year 2002 */
    public static final String   ISO_DATE_FORMAT          = "yyyyMMdd";
    /** 日期制 yyyy-MM-dd */
    public static final String   DATEFORMATTODATESTR      = "yyyy-MM-dd";

    /** Default lenient setting for getDate. */
    private static final boolean LENIENTDATE              = false;

    /** 一天的秒数 **/
    private static final int     DAYSERCOND               = 24 * 60 * 60;
    /** timestamp最小值 1970-01-01 00:00:00 **/
    private static final long    TIMESTAMP_RANGE_MIN      = -28800000L;
    /** timestamp最大值 2037-12-31 23:59:59 **/
    private static final long    TIMESTAMP_RANGE_MAX      = 2145887999000L;

    private DateUtil() {

    }

    /**
     * 功能描述: 验证时间戳是否在指定范围，避免保存到数据库报错<br>
     * timestamp最小值 1970-01-01 00:00:00，最大值 2037-12-31 23:59:59
     * 
     * @param longTime 待验证时间戳
     * @return true在指定范围，false不在
     */
    public static boolean validateTimestampRange(long longTime) {
        if (longTime >= TIMESTAMP_RANGE_MIN && longTime <= TIMESTAMP_RANGE_MAX) {
            return true;
        }
        return false;
    }
    
    /**
     * 功能描述: 获取当日剩余秒数
     * 
     * @return
     */
    public static int getDayRemaindSeconds() {
        Calendar current = Calendar.getInstance();
        current.set(Calendar.MILLISECOND, 0);
        long currentTimes = current.getTimeInMillis();
        current.set(Calendar.HOUR_OF_DAY, 23);
        current.set(Calendar.MINUTE, 59);
        current.set(Calendar.SECOND, 59);
        long endTimes = current.getTimeInMillis();
        return (int) ((endTimes - currentTimes) / 1000);
    }

    /**
     * 返回当前日期字符串
     * 
     * @param pattern 日期字符串样式
     * @return
     */
    public static String getCurrentDateString(String pattern) {
        return dateToString(getCurrentDateTime(), pattern);
    }

    /**
     * 字符串转换为日期java.util.Date
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @return
     */
    public static Date stringToDate(String dateString,
                                    String format) {
        return stringToDate(dateString, format, LENIENTDATE);
    }

    /**
     * 字符串转换为日期java.sql.Date
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @return 数据库中的Date类型，只有年月日
     */
    public static java.sql.Date stringToSqlDate(String dateString,
                                                String format) {
        return new java.sql.Date(stringToDate(dateString, format, LENIENTDATE).getTime());
    }

    /**
     * 字符串转换为日期java.sql.Timestamp
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @return 数据库中的Date类型，只有年月日
     */
    public static java.sql.Timestamp stringToSqlTimestamp(String dateString,
                                                          String format) {
        return new java.sql.Timestamp(stringToDate(dateString, format, LENIENTDATE).getTime());
    }

    /**
     * 字符串转换为日期java.util.Date
     * 
     * @param dateText 字符串
     * @param format 日期格式
     * @param lenient 日期越界标志
     * @return
     */
    public static Date stringToDate(String dateText,
                                    String format,
                                    boolean lenient) {
        if (dateText == null) {
            return null;
        }
        DateFormat df = null;

        try {
            if (format == null) {
                df = new SimpleDateFormat();
            } else {
                df = new SimpleDateFormat(format);
            }
            df.setLenient(lenient);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return df.parse(dateText);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 返回当前时间
     * 
     * @return 返回当前时间
     */
    public static Date getCurrentDateTime() {
        java.util.Calendar calNow = java.util.Calendar.getInstance();
        return calNow.getTime();

    }

    /**
     * 根据时间变量返回时间字符串
     * 
     * @return 返回时间字符串
     * @param pattern 时间字符串样式
     * @param date 时间变量
     */
    public static String dateToString(Object date,
                                      String pattern) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
            sfDate.setLenient(false);
            return sfDate.format(date);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取几天的秒数
     * 
     * @param day
     * @return
     */
    public static int transformDayToSecond(int day) {
        return day * DAYSERCOND;
    }

    /**
     * 取得当前年
     * 
     * @return
     */
    public static int getCurrentYear() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return Integer.parseInt(df.format(new Date()));
    }

    /**
     * 字符串转换成Timestamp类型 格式 :yyyy-MM-dd HH:mm:ss 张静萌
     * 
     * @param timeString
     * @return
     * @throws ParseException
     */
    public static Timestamp stringToTimestamp(String timeString) throws ParseException {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp result = null;

        if (timeString != null && !"".equals(timeString.trim())) {
            // 去掉尾部的.0
            int index = timeString.lastIndexOf('.');
            String tmp;
            if (index != -1) {
                tmp = timeString.substring(0, index);
            } else {
                tmp = timeString;
            }
            result = new Timestamp(dataFormat.parse(tmp).getTime());
        }
        return result;
    }

    /**
     * 
     * 功能描述: 获取本周周一到周日的日期
     * 
     * @return Date[]
     */
    public static Date[] getWeekDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        Date[] dates = new Date[7];
        for (int i = 0; i < 7; i++) {
            if (i == 6) {
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
            }
            dates[i] = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    /**
     * 
     * 功能描述: 根据周一的日期获取下周一到周日的日期
     * 
     * @return Date[]
     */
    public static Date[] getNextWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 7);
        Date[] dates = new Date[7];
        for (int i = 0; i < 7; i++) {
            dates[i] = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    /**
     * 
     * 功能描述: 根据日期获取对应的日期和星期几 2015-08-19 星期三
     * 
     * @param date
     * @return String
     */
    public static String getDayOfWeek(Date date) {
        Locale locale = new Locale("zh", "CN");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd E", locale);
        return format.format(date);
    }


    /**
     * 
     * @Title: dateAdd @Description: 日期date加amount天 @param date @param amount 可为负 @return Date @throws
     */
    public static Date dateAdd(Date date,
                               int amount) {
        return dateAdd(date, amount, Calendar.DAY_OF_MONTH);
    }

    /**
     * 日期加减
     * 
     * @param date
     * @param amount 可为负
     * @param type e.g. Calendar.DAY_OF_MONTH
     * @return
     */
    public static Date dateAdd(Date date,
                               int amount,
                               int type) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(type, amount);
        return cal.getTime();
    }

}