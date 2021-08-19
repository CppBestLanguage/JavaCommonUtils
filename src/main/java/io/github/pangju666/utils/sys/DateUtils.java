package io.github.pangju666.utils.sys;

import org.apache.commons.lang3.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author 胖橘
 * @version 1.0 2020-8-20
 * @version 1.1 2020-8-28 新增根据日期字符串获取日期方法
 * @version 1.2 2020-9-25 新增获取当前系统日期字符串方法
 * @version 1.3 2020-9-10-31 继承org.apache.commons.lang3.time.DateUtils，精简部分已存在方法
 * @since 1.0
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public enum Base {
        /** 日 */
        DAY(1000 * 60 * 60 * 24),
        /** 小时 */
        HOUR(1000 * 60 * 60),
        /** 分钟 */
        MINUTE(1000 * 60),
        /** 秒 */
        SECOND(1000),
        /** 毫秒 */
        MILLISECOND(1);

        Base(int base) {
            this.base = base;
        }

        public int getBase() {
            return base;
        }

        private final int base;
    }

    /** 构造方法私有化，防止生成实例 */
    private DateUtils() {}

    /**
     * 获取当前系统日期字符串
     *
     * @return 当前系统时间字符串，格式为：yyyy-MM-dd
     * @since 1.2
     */
    public static String getCurrentDateStr() {
        return formatDate(null, DATE_FORMAT);
    }

    /**
     * 获取当前系统日期字符串
     *
     * @return 当前系统时间字符串，格式为：yyyy-MM-dd-HH:mm:ss
     * @since 1.2
     */
    public static String getCurrentDateTimeStr() {
        return formatDate(null, DATETIME_FORMAT);
    }

    /**
     * 使用自定义格式, 获取时间字符串
     *
     * @param date 日期，为空则使用当前日期
     * @return 格式化后的时间字符串
     * @since 1.0
     */
    public static String formatDate(Date date, final String pattern) {
        date = ObjectUtils.defaultIfNull(date, new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 日期转换为毫秒数
     *
     * @param date 日期
     * @return 当前日期总毫秒数
     * @since 1.3
     */
    public static long toMilliseconds(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    /**
     * 毫秒数转换为日期
     *
     * @param milliseconds 毫秒数
     * @return 当前毫秒数所表示的日期
     * @since 1.3
     */
    public static Date fromMillisecond(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return calendar.getTime();
    }

    /**
     * 计算两个日期之间的差值
     *
     * @param startDate 日期
     * @param endDate 另一日期
     * @return 相差结果，取毫秒
     * @since 1.3
     */
    public static long subTime(final Date startDate, final Date endDate) {
        return subTime(startDate, endDate, Base.MILLISECOND);
    }

    /**
     * 计算两个日期之间的差值
     *
     * @param startDate 日期
     * @param endDate 另一日期
     * @param base 结果类型，如天，小时，分钟等
     * @return 相差结果, 如果相等或起始日期大于结束日期则返回0
     * @since 1.3
     */
    public static long subTime(Date startDate, Date endDate, final Base base) {
        startDate = ObjectUtils.defaultIfNull(startDate, new Date());
        endDate = ObjectUtils.defaultIfNull(endDate, new Date());

        Calendar startCal = toCalendar(startDate);
        Calendar endCal = toCalendar(endDate);
        if (startCal.compareTo(endCal) >= 0) {
            return 0L;
        }

        long time = startDate.getTime();
        long anotherTime = endDate.getTime();
        return (time - anotherTime) / base.base;
    }

    /**
     * 判断是否是闰年
     *
     * @param date 日期
     * @return 布尔值
     * @since 1.3
     */
    public static boolean isLeapYear(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return (year % 10 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    /**
     * 判断是否是闰年
     *
     * @param str 字符串日期，格式为：yyyy-MM-dd
     * @return 布尔值
     * @since 1.3
     */
    public static boolean isLeapYear(final String str) throws ParseException {
        Date date = parseDate(str, DATE_FORMAT);
        return isLeapYear(date);
    }

    /**
     * 判断是否是闰年
     *
     * @param str 字符串日期
     * @param pattern 时间序列化格式字符串，如：yyyy-MM-dd
     * @return 布尔值
     * @since 1.3
     */
    public static boolean isLeapYear(final String str, final String pattern) throws ParseException {
        Date date = parseDate(str, pattern);
        return isLeapYear(date);
    }

    /**
     * 时间从0时0分0秒开始
     *
     * @param date 日期
     * @return 返回一天的开始时间
     * @since 1.3
     */
    public static Date getInitialDateTheDay(final Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        return instance.getTime();
    }

    /**
     * 时间从23时59分59秒开始
     *
     * @param date 日期
     * @return 返回一天的结束时间
     * @since 1.3
     */
    public static Date getEndDateTheDay(final Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        return instance.getTime();
    }

    public static String secondToVM(Integer second) {
        String dateTimes;
        long days = second / (60 * 60 * 24);
        long hours = (second % (60 * 60 * 24)) / (60 * 60);
        long minutes = (second % (60 * 60)) / 60;
        long seconds = second % 60;
        if (days > 0) {
            dateTimes = days + "天 " + hours + "时 " + minutes + "分 " + seconds + "秒";
        } else if (hours > 0) {
            dateTimes = hours + "时 " + minutes + "分 " + seconds + "秒";
        } else if (minutes > 0) {
            dateTimes = minutes + "分 " + seconds + "秒";
        } else {
            dateTimes = seconds + " 秒";
        }
        return dateTimes;
    }
}