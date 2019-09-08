package com.origin.generation.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期时间通用管理类
 * @author alex
 * @date 2019-09-07
 */
public class DateUtils {

    /**
     * <p>标题: getCurDate<p>
     * <p>说明: <br>获取当前时间</P>
     *
     * @return 当前时间
     */
    public static Date getCurDate() {
        return new Date();
    }

    /**
     * <p>标题: getCurDateStr<p>
     * <p>说明: TODO<br>获取当前时间（YYYY-MM-DD HH：MI：SS.mmm）</P>
     *
     * @return 当前时间
     */
    public static String getCurDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
        return sdf.format(getCurDate());
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param strFormat
     * @return
     */
    public static String formatDate(Date date, String strFormat) {
        SimpleDateFormat sdf;
        if (strFormat == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat(strFormat);
        }
        return sdf.format(date);
    }

    /**
     * <p>标题: getCurDateStr<p>
     * <p>说明: <br>按指定格式获取当前时间<br>例：<br>yyyy<br>yyyy-MM-dd<br>yyyy-MM-dd HH:mm:ss</P>
     *
     * @param dateFormat
     * @return 当前时间
     */
    public static String getCurDateStr(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(getCurDate());
    }

    /**
     * <p>标题: compare<p>
     * <p>说明: <br>比较两个时间大小</P>
     *
     * @param date1
     * @param date2
     * @return date1大于date2 返回1，date1等于date2 返回0, date1少于date2 返回-1
     */
    public static int compare(Date date1, Date date2) {
        return date1.compareTo(date2);
    }

    /**
     * <p>标题: daysOfTwo<p>
     * <p>说明: <br>获取两个日期相差的天数</P>
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daysOfTwo(Date date1, Date date2) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date1);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(date2);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

    /**
     * 获取一个时间的秒数
     *
     * @param date
     * @return
     */
    public static Long getTimeMillis(Date date) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        return aCalendar.getTimeInMillis();
    }

    /**
     * <p>标题: daysOfTwo<p>
     * <p>说明: <br>获取两个日期相差的分钟数</P>
     *
     * @param date1 开始时间
     * @param date2 结整时间
     * @return
     */
    public static int minuteOfTwo(Date date1, Date date2) {
        Long time1 = date1.getTime();
        Long time2 = date2.getTime();
        Long ei = time2 - time1;
        return (int) (ei / (1000 * 60));
    }

    /**
     * <p>标题: getAfterDate<p>
     * <p>说明: TODO<br>获取当前时间几天后的日期</P>
     *
     * @param day 天数
     * @return
     */
    public static Date getAfterDate(int day) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, +day);
        return now.getTime();
    }

    /**
     * <p>标题: getAfterDate<p>
     * <p>说明: TODO<br>获取当前时间几天后的日期</P>
     *
     * @param day 天数
     * @return
     */
    public static Date getAfterDate(Date date, int day) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        aCalendar.add(Calendar.DATE, +day);
        return aCalendar.getTime();
    }

    /**
     * <p>标题: getBeforDate<p>
     * <p>说明: TODO<br>獲取指定日期前幾天的日期</P>
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getBeforDate(Date date, int day) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        aCalendar.add(Calendar.DATE, -day);
        return aCalendar.getTime();
    }

    /**
     * <p>标题: getAfterDate<p>
     * <p>说明: TODO<br>获取当前时间几个月后的日期</P>
     *
     * @param date 天数
     * @return
     */
    public static Date getAfterMonthDate(Date date, int month) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        aCalendar.add(Calendar.MONTH, +month);
        return aCalendar.getTime();
    }

    /**
     * 获取当前年的共有多少周
     *
     * @param year
     * @return
     */
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

        return getWeekOfYear(c.getTime());
    }

    /**
     * <p>标题: getWeekOfYear<p>
     * <p>说明: TODO<br>获取当前时间为第几周</P>
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//设置每周第一天为星期天
        //cal.set(Calendar.WEEK_OF_YEAR, 53);//设置为53周
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * <p>标题: getDayOfWeek<p>
     * <p>说明: TODO<br>獲取當前時間是星期幾</P>
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//设置每周第一天为星期天
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    // 获取某年的第几周的开始日期
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    // 获取某年的第几周的结束日期
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    // 获取当前时间所在周的开始日期
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    // 获取当前时间所在周的结束日期
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    /**
     * 获取二个日期间共有几周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDaysOfWeek(Date date1, Date date2) {
        int weeks = 0;
        int week1 = getWeekOfYear(date1);
        int week2 = getWeekOfYear(date2);
        if (week2 < week1) {
            Integer year1 = Integer.parseInt(formatDate(date1, "yyyy"));
            Integer maxWeeNum = getMaxWeekNumOfYear(year1);
            weeks = (maxWeeNum - week1 + 1) + week2;
        } else {
            weeks = week2 - week1 + 1;
        }
        return weeks;
    }

    /**
     * <p>标题: getAfterDateStr<p>
     * <p>说明: TODO<br>获取当前时间几天后的日期</P>
     *
     * @param day        天数
     * @param dateFormat 输出格式
     * @return 默认返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getAfterDateStr(int day, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat == null ? "yyyy-MM-dd HH:mm:ss"
                : dateFormat);
        return sdf.format(getAfterDate(day));
    }

    /**
     * <p>标题: getDayOfWeekByString<p>
     * <p>说明: TODO<br>根據傳入的日期字符串獲取是星期幾</P>
     *
     * @param str yyyy-mm-dd 格式的字符串
     * @return 一周當中的星期幾 , 星期天返回1 , 星期一返回2 , 星期六返回7
     */
    public static int getDayOfWeekByString(String str) {
        int week = -1;
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = myFormatter.parse(str);
            week = getDayOfWeek(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return week;
    }

    /**
     * <p>标题: getDateByString<p>
     * <p>说明: TODO<br>根據傳入的日期字符串和格式串返回Date</P>
     *
     * @param str    yyyy-mm-dd 格式的字符串
     * @param format yyyy-mm-dd 格式的字符串
     * @return Date
     */
    public static Date getDateByString(String str, String format) {
        Date date = null;
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat(format);
            date = myFormatter.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * <p>标题: getAfterDateStr<p>
     * <p>说明: TODO<br>根據傳入的日期和一個整數,獲取日期后多少天的字串</P>
     *
     * @param date   yyyy-mm-dd 格式的字符串
     * @param count  日期后多少天
     * @param format 格式字符串
     * @return String 日期后多少天的字符串形式
     */
    public static String getAfterDateStr(Date date, int count, String format) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        aCalendar.add(Calendar.DATE, +count);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(aCalendar.getTime());
    }

    /**
     * <p>标题: getWeekOfMonth<p>
     * <p>说明: TODO<br>获取当前时间为當月第几周</P>
     *
     * @param date
     * @return
     */
    public static int getWeekOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//设置每周第一天为星期天
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * <p>标题: parse<p>
     * <p>说明: TODO<br>按format格式將str轉換成Date</P>
     *
     * @param str
     * @param format
     * @return
     */
    public static Date parse(String str, String format) {
        Date date = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static boolean isLessCurData(Date date, int minute) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        aCalendar.add(Calendar.MINUTE, +minute);
        return aCalendar.getTime().after(new Date());
    }
}
