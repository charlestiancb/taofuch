package com.focustech.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.tfc.evolve.common.utils.DateUtils;

/**
 * <li>时间工具单元测试</li>
 * 
 * @author taofucheng
 */
public class DateUtilsTest {
    @Test
    public void testParseDateWithAllDeclearedPatterns() {
        String str = null;
        Date date = null;
        Calendar c = Calendar.getInstance();
        // 年月日、时分秒测试
        str = "2008-12-10 13:12:11";
        date = DateUtils.parseDateWithAllDeclearedPatterns(str);
        c.setTime(date);
        Assert.assertTrue((c.get(Calendar.YEAR) == 2008) && (c.get(Calendar.MONTH) == 11)
                && (c.get(Calendar.DAY_OF_MONTH) == 10) && (c.get(Calendar.HOUR_OF_DAY) == 13)
                && (c.get(Calendar.MINUTE) == 12) && (c.get(Calendar.SECOND) == 11));
        // 年月日测试
        str = "2008-12-10";
        date = DateUtils.parseDateWithAllDeclearedPatterns(str);
        c.setTime(date);
        Assert.assertTrue((c.get(Calendar.YEAR) == 2008) && (c.get(Calendar.MONTH) == 11)
                && (c.get(Calendar.DAY_OF_MONTH) == 10) && (c.get(Calendar.HOUR_OF_DAY) == 0)
                && (c.get(Calendar.MINUTE) == 0) && (c.get(Calendar.SECOND) == 0));
        // 异常测试
        str = "abc";
        try {
            date = DateUtils.parseDateWithAllDeclearedPatterns(str);
            Assert.fail("there must throw an IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testParseDate2String() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //
        String formatStr = DateUtils.parseDate2String(date, DateUtils.DATE_TIME_WITH_SEPARATION_PATTERN, true);
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_TIME_WITH_SEPARATION_PATTERN);
        String targetStr = format.format(date);
        Assert.assertTrue(formatStr.equals(targetStr));
        //
        formatStr = DateUtils.parseDate2String(date, DateUtils.DATE_TIME_WITH_SEPARATION_PATTERN, false);
        format = new SimpleDateFormat(DateUtils.DATE_TIME_WITH_SEPARATION_PATTERN);
        targetStr = format.format(date);
        Assert.assertTrue(formatStr.equals(targetStr));
        //
        formatStr = DateUtils.parseDate2String(null, DateUtils.DATE_TIME_WITH_SEPARATION_PATTERN, false);
        Assert.assertTrue(formatStr.equals(""));
        //
        try {
            formatStr = DateUtils.parseDate2String(null, DateUtils.DATE_TIME_WITH_SEPARATION_PATTERN, true);
            Assert.fail("there must throw an IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
        //
        try {
            formatStr = DateUtils.parseDate2String(null, "abc", true);
            Assert.fail("there must throw an IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testParseStringDate2String() {
        String str = "2008-12-10 13:12:11";
        String date = DateUtils.parseDate2String(str, DateUtils.DATE_WITH_SEPARATION_PATTERN);
        Assert.assertTrue("2008-12-10".equals(date));
        //
        date = DateUtils.parseDate2String(str, DateUtils.DATE_TIME_WITH_SEPARATION_PATTERN);
        Assert.assertTrue("2008-12-10 13:12:11".equals(date));
        //
        try {
            DateUtils.parseDate2String("abc", DateUtils.DATE_TIME_WITH_SEPARATION_PATTERN);
            Assert.fail("there must throw an IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testGetInterval() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = null;
        Date end = null;
        try {
            end = sdf.parse("2010-01-01 12:00:00");
            start = sdf.parse("2010-01-01 11:59:59");
        }
        catch (ParseException e) {
            Assert.fail("convert date fail!!");
        }
        Assert.assertTrue(DateUtils.getInterval(start, start) == 0);
        Assert.assertTrue(DateUtils.getInterval(start, end) == 1000);
        Assert.assertTrue(DateUtils.getInterval(end, start) == -1000);
    }

    @Test
    public void testGetIntervalDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = null;
        Date end = null;
        try {
            end = sdf.parse("2010-01-02 12:10:00");// 时间不一致，表示只关心天数的不同
            start = sdf.parse("2010-01-01 12:00:00");
        }
        catch (ParseException e) {
            Assert.fail("convert date fail!!");
        }
        Assert.assertTrue(DateUtils.getIntervalDay(start, start) == 0);
        Assert.assertTrue(DateUtils.getIntervalDay(start, end) == 1);
        Assert.assertTrue(DateUtils.getIntervalDay(end, start) == -1);
    }

    @Test
    public void testGetIntervalMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = null;
        Date end = null;
        try {
            end = sdf.parse("2010-02-11 12:10:00");// 时间和日期的不同，表示只关心月份的变化。
            start = sdf.parse("2010-01-01 12:00:00");
        }
        catch (ParseException e) {
            Assert.fail("convert date fail!!");
        }
        Assert.assertTrue(DateUtils.getIntervalMonth(start, start) == 0);
        Assert.assertTrue(DateUtils.getIntervalMonth(start, end) == 1);
        Assert.assertTrue(DateUtils.getIntervalMonth(end, start) == 1);// 这是注意：开始时间与结束时间没有顺序区别
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testFormatString2Date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date flagDate = null;
        // 空字符串
        Assert.assertEquals(DateUtils.formatString2Date("").getTime(), DateUtils.formatString2Date("  ").getTime());
        Assert.assertEquals(DateUtils.formatString2Date("").getTime(), DateUtils.formatString2Date(null).getTime());
        Assert.assertTrue(DateUtils.formatString2Date("").getSeconds() == new Date().getSeconds());
        try {
            // 正常日期
            flagDate = sdf.parse("2010-01-01 00:00:00");
            Assert.assertEquals(DateUtils.formatString2Date("2010-01-01").getTime(), flagDate.getTime());
            //
            flagDate = sdf.parse("2010-01-01 12:00:00");
            Assert.assertEquals(DateUtils.formatString2Date("2010-01-01 12:00:00").getTime(), flagDate.getTime());
            // 注意：非正常的日期、时间格式也是可以的。
            flagDate = sdf.parse("2010-21-01 12:00:00");
            Assert.assertEquals(DateUtils.formatString2Date("2010-21-01 12:00:00").getTime(), flagDate.getTime());
        }
        catch (ParseException e) {
            Assert.fail("convert date fail!!");
        }
    }

    @Test
    public void testGetDateOfDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date flagDate = null;
        try {
            flagDate = sdf.parse("2010-01-10 12:00:00");
        }
        catch (ParseException e) {
            Assert.fail("convert date fail!!");
        }
        // 测试指定格式的
        Assert.assertEquals(DateUtils.getDateOfDay(flagDate, 25, "yyyy-MM-dd"), "2010-02-04");
        Assert.assertEquals(DateUtils.getDateOfDay(flagDate, -15, "yyyy-MM-dd"), "2009-12-26");
        // 测试没有格式的
        try {
            Assert.assertEquals(DateUtils.getDateOfDay(flagDate, 25).getTime(), sdf.parse("2010-02-04 12:00:00")
                    .getTime());
            Assert.assertEquals(DateUtils.getDateOfDay(flagDate, -15).getTime(), sdf.parse("2009-12-26 12:00:00")
                    .getTime());
        }
        catch (ParseException e) {
            Assert.fail("convert date fail!!");
        }

    }

    @Test
    public void testCheckDateIsValidate() {
        // 一个参数的方法
        Assert.assertEquals(DateUtils.checkDateIsValidate("abc"), -1);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1"), 0);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-01"), 0);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1 12:00:00"), 0);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1 23:59:59"), 0);
        Assert.assertEquals(DateUtils.checkDateIsValidate(" "), -1);
        Assert.assertEquals(DateUtils.checkDateIsValidate(""), -1);
        Assert.assertEquals(DateUtils.checkDateIsValidate(null), -1);
        Assert.assertEquals(DateUtils.checkDateIsValidate(" 2008-10-1 23:59:59"), -1);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-13-1 23:59:59"), -2);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-2-30"), -3);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1 24:01:00"), -4);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1 23:61:00"), -5);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1 23:01:61"), -6);
        // 两个参数的方法
        Assert.assertEquals(DateUtils.checkDateIsValidate("abc", "yyyy-MM-dd"), -1);// 正常的时间格式
        Assert.assertEquals(DateUtils.checkDateIsValidate("abc", "asddddd"), -1);// 随便乱写的时间格式
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1", "yyyy-MM-dd"), 0);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-01", "yyyy-MM-dd"), 0);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1 12:00:00", "yyyy-MM-dd HH:mm:ss"), 0);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1 23:59:59", "yyyy-MM-dd HH:mm:ss"), 0);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008/10/1 23:01:01", "yyyy-MM-dd HH:mm:ss"), -1);
        Assert.assertEquals(DateUtils.checkDateIsValidate(" ", "yyyy-MM-dd"), -1);// 正常的时间格式
        Assert.assertEquals(DateUtils.checkDateIsValidate(" ", "asddddd"), -1);// 随便乱写的时间格式
        Assert.assertEquals(DateUtils.checkDateIsValidate("", "yyyy-MM-dd"), -1);// 正常的时间格式
        Assert.assertEquals(DateUtils.checkDateIsValidate("", "asddddd"), -1);// 随便乱写的时间格式
        Assert.assertEquals(DateUtils.checkDateIsValidate(null, "yyyy-MM-dd"), -1);// 正常的时间格式
        Assert.assertEquals(DateUtils.checkDateIsValidate(null, "asddddd"), -1);// 随便乱写的时间格式
        Assert.assertEquals(DateUtils.checkDateIsValidate(" 2008-10-1 23:59:59", "yyyy-MM-dd HH:mm:ss"), -1);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-13-1 23:59:59", "yyyy-MM-dd HH:mm:ss"), -2);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-2-30", "yyyy-MM-dd"), -3);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1 24:01:00", "yyyy-MM-dd HH:mm:ss"), -4);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1 23:61:00", "yyyy-MM-dd HH:mm:ss"), -5);
        Assert.assertEquals(DateUtils.checkDateIsValidate("2008-10-1 23:01:61", "yyyy-MM-dd HH:mm:ss"), -6);
        // 三个参数的方法
        Assert.assertEquals(DateUtils.checkDateIsValidate(null, "yyyy-MM-dd", false), -1);
        Assert.assertEquals(DateUtils.checkDateIsValidate(null, "yyyy-MM-dd", true), 0);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testGetInitDatetime() {
        Assert.assertEquals(0, DateUtils.getInitDatetime().getYear());
        Assert.assertEquals(0, DateUtils.getInitDatetime().getMonth());
        Assert.assertEquals(1, DateUtils.getInitDatetime().getDate());
        Assert.assertEquals(0, DateUtils.getInitDatetime().getHours());
        Assert.assertEquals(0, DateUtils.getInitDatetime().getMinutes());
        Assert.assertEquals(0, DateUtils.getInitDatetime().getSeconds());
    }

    @Test
    public void testGetDateEnd() {
        Assert.assertEquals("2011-12-12 23:59:59", DateUtils.getDateEnd("2011-12-12"));
        Date d = DateUtils.parse2Date("2011-02-11 11:01", "yyyy-MM-dd HH:mm");
        Date d1 = DateUtils.getDateEnd(d);
        Assert.assertEquals("2011-02-11 23:59:59",
                DateUtils.parseDate2String(d1, DateUtils.DATE_TIME_WITH_SEPARATION_PATTERN));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testParse2Date() {
        Date d = DateUtils.parse2Date("2011-02-11 11:01", "yyyy-MM-dd HH:mm");
        Assert.assertEquals(2011, d.getYear() + 1900);
        Assert.assertEquals(2, d.getMonth() + 1);
        Assert.assertEquals(11, d.getDate());
        Assert.assertEquals(11, d.getHours());
        Assert.assertEquals(1, d.getMinutes());
        Assert.assertEquals(0, d.getSeconds());
    }

    /**
     * @author wangliangliang
     */
    @Test
    public void testGetMonth() {
        Calendar c = Calendar.getInstance();
        c.set(2011, 10, 11, 11, 11, 11);
        Assert.assertEquals("11", String.valueOf(DateUtils.getMonth(c.getTime())));
    }

    /**
     * @author wangliangliang
     */
    @Test
    public void testGetDayOfMonth() {
        Calendar c = Calendar.getInstance();
        c.set(2011, 10, 15, 11, 11, 11);
        Assert.assertEquals("15", String.valueOf(DateUtils.getDayOfMonth(c.getTime())));
    }

    /**
     * @author wangliangliang
     */
    @Test
    public void testGetHourOfDay() {
        Calendar c = Calendar.getInstance();
        c.set(2011, 10, 11, 12, 13, 11);
        Assert.assertEquals("12", String.valueOf(DateUtils.getHourOfDay(c.getTime())));
    }

    @Test
    public void testGetZeroTime() {
        Assert.assertEquals(DateUtils.parse2Date("1970-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime(), DateUtils
                .getZeroTime().getTime());
    }

    /**
     * @author zhouxiaohui
     */
    @Test
    public void testGetMonthOfMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date flagDate = null;
        try {
            flagDate = sdf.parse("2000-02-29 12:00:00");
        }
        catch (ParseException e) {
            Assert.fail("convert date fail!!");
        }
        try {
            Assert.assertEquals(DateUtils.getMonthOfMonth(flagDate, 12), sdf.parse("2001-02-28 12:00:00"));
            Assert.assertEquals(DateUtils.getMonthOfMonth(flagDate, -1), sdf.parse("2000-01-29 12:00:00"));
        }
        catch (ParseException e) {
            Assert.fail("convert date fail!!");
        }

    }
}
