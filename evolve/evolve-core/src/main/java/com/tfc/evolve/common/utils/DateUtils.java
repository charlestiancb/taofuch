package com.tfc.evolve.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <li>日期工具类</li>
 * <p>
 * 继承自org.apache.commons.lang.time.DateUtils，提供常用时间处理方法
 * <p>
 * 继承可能不是一个很好的模式，如有条件，请使用包装模式修改
 * 
 * @see org.apache.commons.lang.time.DateUtils
 * @author taofucheng
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
    public static final String ORACLE_DATE_TIME_24_PATTERN = "yyyy-mm-dd hh24:mi:ss";
    public static final String DATE_TIME_WITH_SEPARATION_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_WITH_SEPARATION_PATTERN = "yyyy-MM-dd";
    /** 日期格式的所有形式。该数组中的元素位置不能改变！ */
    public static final String[] ALL_PATTERNS = {DATE_TIME_WITH_SEPARATION_PATTERN, DATE_WITH_SEPARATION_PATTERN};
    private static Pattern pDateFormat = Pattern
            .compile("^(\\d{4})[^\\d](\\d{1,2})[^\\d](\\d{1,2})( (\\d{1,2}):(\\d{1,2}):(\\d{1,2})){0,1}$");

    /**
     * 根据声明的所有时间格式，格式化时间
     * 
     * @param date 字符串时间
     * @return 格式化后的时间类型
     * @throws IllegalArgumentException 当转换失败时抛出该异常
     */
    public static Date parseDateWithAllDeclearedPatterns(String date) {
        try {
            return org.apache.commons.lang.time.DateUtils.parseDate(date, ALL_PATTERNS);
        }
        catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * 将Date转换为String
     * 
     * @param date 需要转换的时间
     * @param pattern 转换的时间格式
     * @param allowException 转换时间为空时是否报错。如果allowException为false，在转换时间为空时返回空字符串
     * @throws IllegalArgumentException 当需要转换的时间为空时抛出该异常
     */
    public static String parseDate2String(Date date, String pattern, boolean allowException) {
        if (null == date) {
            if (!allowException) {
                return "";
            }
            else {
                throw new IllegalArgumentException("the date must not be null!");
            }
        }
        else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        }
    }

    /**
     * 将Date转换为指定格式的String
     * 
     * @param date 需要转换的时间
     * @param pattern 转换的时间格式
     * @return 返回指定格式的字符串时间
     */
    public static String parseDate2String(Date date, String pattern) {
        // 如果传过来的值为空,默认不抛异常
        return parseDate2String(date, pattern, false);
    }

    /**
     * 将一个由字符串表示的时间,按照指定的格式输出.
     * 
     * @param date 由字符串表示的时间,不能为空.
     * @param pattern 时间格式
     * @return 返回指定的格式的字符串时间
     * @throws IllegalArgumentException 给定的参数date为一个非法的时间格式,无法转换为时间对象
     */
    public static String parseDate2String(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date d = null;
        try {
            d = sdf.parse(date);
        }
        catch (ParseException e) {
            throw new IllegalArgumentException("the date:" + date + " could not be parsed.", e);
        }
        return sdf.format(d);
    }

    /**
     * 将字符串格式的时间转换为对应的格式的日期！
     * 
     * @param date 字符串型的日期
     * @param pattern 对应的格式
     * @return 返回对应的日期
     */
    public static Date parse2Date(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date d = null;
        try {
            d = sdf.parse(date);
        }
        catch (ParseException e) {
            throw new IllegalArgumentException("the date:" + date + " could not be parsed.", e);
        }
        return d;
    }

    /**
     * 计算2个日期之间相差的时间（毫秒）
     * 
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 返回2个日期之间相差的时间（毫秒）
     */
    public static long getInterval(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long intervalDate = endTime - startTime;
        return intervalDate;
    }

    /**
     * 计算2个日期之间相差的天数
     * 
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 返回2个时间的天数差值
     */
    public static long getIntervalDay(Date startDate, Date endDate) {
        Date startTime = truncate(startDate, Calendar.DATE);
        Date endTime = truncate(endDate, Calendar.DATE);
        long l = endTime.getTime() - startTime.getTime();
        return l / 1000 / 60 / 60 / 24;
    }

    /**
     * 计算2个日期之间相差的时间（小时）
     * 
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 返回2个日期之间相差的时间（小时）
     */
    public static long getIntervalHour(Date startDate, Date endDate) {
        long l = getInterval(startDate, endDate);
        return l / 1000 / 60 / 60;
    }

    /**
     * 计算2个日期之间相差的月数
     * 
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 返回2个时间的月数差值
     */
    public static long getIntervalMonth(Date startDate, Date endDate) {
        int iMonth = 0;
        int flag = 0;
        Calendar objCalendarDate1 = Calendar.getInstance();
        objCalendarDate1.setTime(startDate);
        Calendar objCalendarDate2 = Calendar.getInstance();
        objCalendarDate2.setTime(endDate);
        if (objCalendarDate2.equals(objCalendarDate1)) {
            return 0;
        }
        if (objCalendarDate1.after(objCalendarDate2)) {
            Calendar temp = objCalendarDate1;
            objCalendarDate1 = objCalendarDate2;
            objCalendarDate2 = temp;
        }
        if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1.get(Calendar.DAY_OF_MONTH)) {
            flag = 1;
        }
        if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR)) {
            iMonth =
                    ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1.get(Calendar.YEAR)) * 12
                            + objCalendarDate2.get(Calendar.MONTH) - flag)
                            - objCalendarDate1.get(Calendar.MONTH);
        }
        else {
            iMonth = objCalendarDate2.get(Calendar.MONTH) - objCalendarDate1.get(Calendar.MONTH) - flag;
        }
        return iMonth;
    }

    /**
     * 将一个日期格式的字符串转换成对应格式的日期; 如果传入的是空字符串,则返回当前日期
     * 
     * @param strDateTime 日期格式的字符串
     * @return 返回转换后的日期
     */
    @SuppressWarnings("deprecation")
    public static Date formatString2Date(String strDateTime) {
        Date date = new Date();
        if ((strDateTime != null) && !"".equals(strDateTime.trim())) {
            Matcher m = pDateFormat.matcher(strDateTime);
            if (m.find()) {
                if (m.group(4) != null) {
                    date =
                            new Date(Integer.parseInt(m.group(1)) - 1900, Integer.parseInt(m.group(2)) - 1,
                                    Integer.parseInt(m.group(3)), Integer.parseInt(m.group(5)), Integer.parseInt(m
                                            .group(6)), Integer.parseInt(m.group(7)));
                }
                else {
                    date =
                            new Date(Integer.parseInt(m.group(1)) - 1900, Integer.parseInt(m.group(2)) - 1,
                                    Integer.parseInt(m.group(3)));
                }
            }
        }
        return date;
    }

    /**
     * 得到给定日期的加(正数)或减(负数)天数后的时间
     * 
     * @param time 给定日期
     * @param iDays 要加还是减多少天
     * @param format 返回日期的格式
     * @return 返回计算后的日期，并按指定的格式输出
     * @since vFrw1.01-release
     */
    public static String getDateOfDay(Date time, int iDays, String format) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.DATE, iDays);
        return parseDate2String(c.getTime(), format);
    }

    /**
     * 得到给定日期的加(正数)或减(负数)天数后的时间
     * 
     * @param time 给定日期
     * @param iDays 要加还是减多少天
     * @return 返回计算后的日期
     * @since vFrw1.01-release
     */
    public static Date getDateOfDay(Date time, int iDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.DATE, iDays);
        return c.getTime();
    }

    /**
     * @author zhouxiaohui
     * @param time 给定日期
     * @param iMonths 要加还是减多少月
     * @return 返回计算后的日期
     */

    public static Date getMonthOfMonth(Date time, int iMonths) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.MONTH, iMonths);
        return c.getTime();
    }

    /**
     * 检查输入的时间格式是否合法，即是否是日常生活中应该出现的格式。如果为空，则返回false。<br>
     * 验证的规则为：<br>
     * 1、时间格式不正确时，如：有非数字的情况、有负数、小数的情况等等，验证不通过，返回-1；<br>
     * 2、年月日时分秒等数字不符合正常的时间格式，即不可能出现的时间格式，如：2008-2-30或2008-10-1 24:00:00等等，则验证不通过，具体返回信息根据具体情况决定。<br>
     * 示例：<br>
     * checkDateIsValidate("abc")=-1;<br>
     * checkDateIsValidate("2008-10-1")=0;<br>
     * checkDateIsValidate("2008-10-01")=0;<br>
     * checkDateIsValidate("2008-10-1 12:00:00")=0;<br>
     * checkDateIsValidate("2008-10-1 23:59:59")=0;<br>
     * checkDateIsValidate(" ")=-1;<br>
     * checkDateIsValidate("")=-1;<br>
     * checkDateIsValidate(null)=-1;<br>
     * checkDateIsValidate(" 2008-10-1 23:59:59")=-1;（因为前面有空格）<br>
     * checkDateIsValidate("2008-13-1 23:59:59")=-2;<br>
     * checkDateIsValidate("2008-2-30")=-3;<br>
     * checkDateIsValidate("2008-10-1 24:01:00")=-4;<br>
     * checkDateIsValidate("2008-10-1 23:61:00")=-5;<br>
     * checkDateIsValidate("2008-10-1 23:01:61")=-6;<br>
     * 
     * @param date 字符串型的时间格式。只允许yyyy-MM-dd (HH:mm:ss)格式的，如2008-10-1或2008-10-1 12:00:00
     * @return 0：合法；-1：不是日期格式（包括年份不正确的情况）；-2：月份格式不正确；-3：日期格式不正确；-4：小时格式不正确；-5：分钟格式不正确；-6：秒格式不正确
     */
    public static int checkDateIsValidate(String date) {
        if (StringUtils.isEmpty(date)) {
            return -1;
        }
        int faildTimes = 0;
        for (String pattern : ALL_PATTERNS) {
            try {
                parseDate2String(date, pattern);
                break;
            }
            catch (IllegalArgumentException e) {
                ++faildTimes;
            }
        }
        // 如果默认的所有格式都无法对其进行转换，则说明其格式不正确！
        if (ALL_PATTERNS.length <= faildTimes) {
            return -1;
        }
        else {
            return checkDateIsValidate(date, ALL_PATTERNS[faildTimes]);
        }
    }

    /**
     * 检查输入的时间格式是否合法，即是否是日常生活中应该出现的格式。如果为空，则返回false。<br>
     * 验证的规则为：<br>
     * 1、时间格式不正确时，如：有非数字的情况、有负数、小数的情况等等，验证不通过，返回-1；<br>
     * 2、年月日时分秒等数字不符合正常的时间格式，即不可能出现的时间格式，如：2008-2-30或2008-10-1 24:00:00等等，则验证不通过，具体返回信息根据具体情况决定。<br>
     * 示例：<br>
     * checkDateIsValidate("abc",*)=-1;<br>
     * checkDateIsValidate("2008-10-1","yyyy-MM-dd")=0;<br>
     * checkDateIsValidate("2008-10-01","yyyy-MM-dd")=0;<br>
     * checkDateIsValidate("2008-10-1 12:00:00","yyyy-MM-dd HH:mm:ss")=0;<br>
     * checkDateIsValidate("2008-10-1 23:59:59","yyyy-MM-dd HH:mm:ss")=0;<br>
     * checkDateIsValidate("2008/10/1 23:01:01","yyyy-MM-dd HH:mm:ss")=-1;<br>
     * checkDateIsValidate(" ",*)=-1;<br>
     * checkDateIsValidate("",*)=-1;<br>
     * checkDateIsValidate(null,*)=-1;<br>
     * checkDateIsValidate(" 2008-10-1 23:59:59","yyyy-MM-dd HH:mm:ss")=-1;（因为前面有空格）<br>
     * checkDateIsValidate("2008-13-1 23:59:59","yyyy-MM-dd HH:mm:ss")=-2;<br>
     * checkDateIsValidate("2008-2-30","yyyy-MM-dd")=-3;<br>
     * checkDateIsValidate("2008-10-1 24:01:00","yyyy-MM-dd HH:mm:ss")=-4;<br>
     * checkDateIsValidate("2008-10-1 23:61:00","yyyy-MM-dd HH:mm:ss")=-5;<br>
     * checkDateIsValidate("2008-10-1 23:01:61","yyyy-MM-dd HH:mm:ss")=-6;<br>
     * 
     * @param date 字符串型的时间格式。与指定的format表现形式一致
     * @param format 日期格式。其格式要求必须是简单类型，如年月日（时分抄）<括号表示可有可无>，且年月日与时分抄之间必须以一个半角空格分开。
     * @return 0：合法；-1：不是日期格式（包括年份不正确的情况）；-2：月份格式不正确；-3：日期格式不正确；-4：小时格式不正确；-5：分钟格式不正确；-6：秒格式不正确
     */
    public static int checkDateIsValidate(String date, String format) {
        return checkDateIsValidate(date, format, false);
    }

    /**
     * 检查输入的时间格式是否合法，即是否是日常生活中应该出现的格式。如果为空，则返回false。<br>
     * 验证的规则为：<br>
     * 1、时间格式不正确时，如：有非数字的情况、有负数、小数的情况等等，验证不通过，返回-1；<br>
     * 2、年月日时分秒等数字不符合正常的时间格式，即不可能出现的时间格式，如：2008-2-30或2008-10-1 24:00:00等等，则验证不通过，具体返回信息根据具体情况决定。<br>
     * 示例：<br>
     * checkDateIsValidate("abc",*,*)=-1;<br>
     * checkDateIsValidate("2008-10-1","yyyy-MM-dd",*)=0;<br>
     * checkDateIsValidate("2008-10-01","yyyy-MM-dd",*)=0;<br>
     * checkDateIsValidate("2008-10-1 12:00:00","yyyy-MM-dd HH:mm:ss",*)=0;<br>
     * checkDateIsValidate("2008-10-1 23:59:59","yyyy-MM-dd HH:mm:ss",*)=0;<br>
     * checkDateIsValidate("2008/10/1 23:01:01","yyyy-MM-dd HH:mm:ss",*)=-1;<br>
     * checkDateIsValidate(" ",*,*)=-1;<br>
     * checkDateIsValidate("",*,true)=0;<br>
     * checkDateIsValidate(null,*,true)=0;<br>
     * checkDateIsValidate("",*,false)=-1;<br>
     * checkDateIsValidate(null,*,false)=-1;<br>
     * checkDateIsValidate(" 2008-10-1 23:59:59","yyyy-MM-dd HH:mm:ss",*)=-1;（因为前面有空格）<br>
     * checkDateIsValidate("2008-13-1 23:59:59","yyyy-MM-dd HH:mm:ss",*)=-2;<br>
     * checkDateIsValidate("2008-2-30","yyyy-MM-dd",*)=-3;<br>
     * checkDateIsValidate("2008-10-1 24:01:00","yyyy-MM-dd HH:mm:ss",*)=-4;<br>
     * checkDateIsValidate("2008-10-1 23:61:00","yyyy-MM-dd HH:mm:ss",*)=-5;<br>
     * checkDateIsValidate("2008-10-1 23:01:61","yyyy-MM-dd HH:mm:ss",*)=-6;<br>
     * 
     * @param date 字符串型的时间格式。与指定的format表现形式一致
     * @param format 日期格式。其格式要求必须是简单类型，如年月日（时分抄）<括号表示可有可无>，且年月日与时分抄之间必须以一个半角空格分开。
     * @param allowEmpty 是否允许为空。
     * @return 0：合法；-1：不是日期格式（包括年份不正确的情况）；-2：月份格式不正确；-3：日期格式不正确；-4：小时格式不正确；-5：分钟格式不正确；-6：秒格式不正确
     */
    public static int checkDateIsValidate(String date, String format, boolean allowEmpty) {
        if (StringUtils.isEmpty(StringUtils.trim(date))) {
            if (allowEmpty) {
                return 0;
            }
            else {
                return -1;
            }
        }
        try {
            // 这里排除一些最基本的错误，如时间时间中有非数字、小数的情况
            parseDate2String(date, format);
            // 下面开始进行规则判断
            Pattern dateFormat =
                    Pattern.compile("^(\\d{1,})[^\\d](\\d{1,})[^\\d](\\d{1,})( (\\d{1,}):(\\d{1,}):(\\d{1,}))?$");
            Matcher m = dateFormat.matcher(date);
            if (m.find()) {
                int year = Integer.parseInt(m.group(1));
                int month = Integer.parseInt(m.group(2));
                int day = Integer.parseInt(m.group(3));
                // 月份对应的最大日期数组
                int[] maxDays = {31, (year % 4 == 0 ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
                if (year < 1) {
                    return -1;
                }
                if ((month < 1) || (month > 12)) {
                    return -2;
                }
                if ((day < 1) || (day > maxDays[month - 1])) {
                    return -3;
                }
                // 时间存在
                if (m.group(4) != null) {
                    int hour = Integer.parseInt(m.group(5));
                    int minute = Integer.parseInt(m.group(6));
                    int second = Integer.parseInt(m.group(7));
                    if ((hour < 0) || (hour > 23)) {
                        return -4;
                    }
                    if ((minute < 0) || (minute > 59)) {
                        return -5;
                    }
                    if ((second < 0) || (second > 59)) {
                        return -6;
                    }
                }
                return 0;
            }
            else {
                return -1;
            }
        }
        catch (Exception e) {
            // 发生任何异常，表示失败
            return -1;
        }
    }

    /**
     * 获取时间的起始值，即：1900-01-01 00:00:00.
     * 
     * @return 返回1900-01-01 00:00:00的时间
     */
    public static Date getInitDatetime() {
        return formatString2Date("1900-01-01 00:00:00");
    }

    /**
     * 获取时间差别为0的时间，即1970-01-01 00:00:00
     * 
     * @return 返回1970-01-01 00:00:00的时间
     */
    public static Date getZeroTime() {
        return formatString2Date("1970-01-01 00:00:00");
    }

    /**
     * 获取当前时间<br/>
     * 本方法是为了适应以后可能出现的多时区需求
     * 
     * @param zone 时区
     * @param locale locale
     * @return
     */
    public static Date getCurrentTime(TimeZone zone, Locale locale) {
        Calendar cal = Calendar.getInstance(zone, locale);

        return cal.getTime();
    }

    /**
     * 获取默认当前时间
     * 
     * @return
     */
    public static Date getDefaultCurrentTime() {
        return getCurrentTime(TimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * 把日期精确到天 如2011-05-02 10:15:10 转化成2011-05-02
     * 
     * @author zhouxiaohui
     * @param date
     * @return
     */
    public static Date truncDate(Date date) {
        String dateString = parseDate2String(date, DATE_WITH_SEPARATION_PATTERN);
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_WITH_SEPARATION_PATTERN);
        try {
            return dateFormat.parse(dateString);
        }
        catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取指定时间的最后一刻，如：2011-12-12 23:59:59
     * 
     * @param date
     * @return
     */
    public static String getDateEnd(String date) {
        if (StringUtils.isEmpty(date))
            return null;
        return parseDate2String(formatString2Date(date), DATE_WITH_SEPARATION_PATTERN) + " 23:59:59";
    }

    /**
     * 获取指定时间的最后一刻，如：2011-12-12 23:59:59
     * 
     * @param date
     * @return
     */
    public static Date getDateEnd(Date date) {
        String d = parseDate2String(date, DATE_TIME_WITH_SEPARATION_PATTERN);
        return parse2Date(getDateEnd(d), DATE_TIME_WITH_SEPARATION_PATTERN);
    }

    /**
     * 获取指定时间的月份
     * 
     * @author wangliangliang
     * @param date 不能为null
     * @return
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取指定时间当天是几号
     * 
     * @author wangliangliang
     * @param date 不能为null
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定时间当前是几点（整点时间 24h）
     * 
     * @author wangliangliang
     * @param date 不能为null
     * @return
     */
    public static int getHourOfDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }
}
