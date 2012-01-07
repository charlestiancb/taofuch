/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.tfc.evolve.common.utils.DateUtils;
import com.tfc.evolve.common.utils.StringUtils;

/**
 * StringUtils对应的单元测试类
 * 
 * @author taofucheng
 */
public class StringUtilsTest {

    @Test
    public void testTruncateComma() {
        Assert.assertEquals(StringUtils.truncateComma(null), null);
        Assert.assertEquals(StringUtils.truncateComma(""), "");
        Assert.assertEquals(StringUtils.truncateComma(" "), " ");
        Assert.assertEquals(StringUtils.truncateComma("sdf"), "sdf");
        Assert.assertEquals(StringUtils.truncateComma("sdf,"), "sdf");
        Assert.assertEquals(StringUtils.truncateComma("string1,string2,"), "string1,string2");
    }

    @Test
    public void testTrimEndChar() {
        // 如果字符串是空或末尾没有要求的字符串，不管要求替换成什么，都是返回原来的值。
        Assert.assertEquals(StringUtils.trimEndChar(null, null), null);
        Assert.assertEquals(StringUtils.trimEndChar(null, ""), null);
        Assert.assertEquals(StringUtils.trimEndChar(null, ","), null);
        Assert.assertEquals(StringUtils.trimEndChar("", null), "");
        Assert.assertEquals(StringUtils.trimEndChar("s1,s2,", null), "s1,s2,");// 如果指定的去除符为空，则返回原值。
        Assert.assertEquals(StringUtils.trimEndChar("s1,s2,", ""), "s1,s2,");// 如果指定的去除符为空，则返回原值。
        Assert.assertEquals(StringUtils.trimEndChar("", ""), "");
        Assert.assertEquals(StringUtils.trimEndChar("", ","), "");
        Assert.assertEquals(StringUtils.trimEndChar(" ", null), " ");
        Assert.assertEquals(StringUtils.trimEndChar(" ", ""), " ");
        Assert.assertEquals(StringUtils.trimEndChar(" ", ","), " ");
        // 正常替换
        Assert.assertEquals(StringUtils.trimEndChar(" ", " "), "");// 去除最后一个空格
        Assert.assertEquals(StringUtils.trimEndChar("  ", " "), " ");// 去除最后一个空格
        Assert.assertEquals(StringUtils.trimEndChar("string1,string2,", ","), "string1,string2");
        Assert.assertEquals(StringUtils.trimEndChar("string1,string2", "string2"), "string1,");// 这个工具也可以替换一个字符串
    }

    @Test
    public void testSortStringArray() {
        Assert.assertEquals(StringUtils.sortStringArray(null), "");
        Assert.assertEquals(StringUtils.sortStringArray(new String[]{}), "");
        Assert.assertEquals(StringUtils.sortStringArray(new String[]{}), "");
        Assert.assertEquals(StringUtils.sortStringArray(new String[]{"2", "5", "3", "1", "4"}), "1,2,3,4,5");
        Assert.assertEquals(StringUtils.sortStringArray(new String[]{"cc", "abc", "dd", "ee", "abd"}),
                "abc,abd,cc,dd,ee");
    }

    @Test
    public void testConcatElements() {
        // 测试方法：concatElements(String... str)
        Assert.assertEquals(StringUtils.concatElements((String[]) null), "");
        Assert.assertEquals(StringUtils.concatElements(""), "");
        Assert.assertEquals(StringUtils.concatElements(new String[]{}), "");
        Assert.assertEquals(StringUtils.concatElements("123", "456", "789"), "123,456,789");
        // 测试方法：concatElements(String[] str, String chr)
        Assert.assertEquals(StringUtils.concatElements(null, null), "");
        Assert.assertEquals(StringUtils.concatElements(null, ""), "");
        Assert.assertEquals(StringUtils.concatElements(null, "anySymbol"), "");
        Assert.assertEquals(StringUtils.concatElements(new String[]{}, null), "");
        Assert.assertEquals(StringUtils.concatElements(new String[]{"123", "456", "789"}, null), "");
        Assert.assertEquals(StringUtils.concatElements(new String[]{"123", "456", "789"}, ""), "");
        Assert.assertEquals(StringUtils.concatElements(new String[]{}, ""), "");
        Assert.assertEquals(StringUtils.concatElements(new String[]{}, "anySymbol"), "");
        Assert.assertEquals(StringUtils.concatElements(new String[]{}), "");
        Assert.assertEquals(StringUtils.concatElements(new String[]{"123", "456", "789"}, ";"), "123;456;789");
        Assert.assertEquals(StringUtils.concatElements(new String[]{"123", "", "789"}, ";"), "123;789");// 注意，中间不会多出一个分号！
    }

    @Test
    public void testSplit() {
        Assert.assertEquals(StringUtils.split(null, null).length, 1);
        Assert.assertEquals(StringUtils.split(null, null)[0], "");
        Assert.assertEquals(StringUtils.split("ssss,dddd", null).length, 1);
        Assert.assertEquals(StringUtils.split("ssss,dddd", "").length, 1);
        Assert.assertEquals(StringUtils.split("ssss,dddd", null)[0], "ssss,dddd");
        Assert.assertEquals(StringUtils.split("ssss,dddd", ";").length, 1);
        Assert.assertEquals(StringUtils.split("ssss,dddd", ",").length, 2);
        Assert.assertEquals(StringUtils.split("ssss,,dddd", ",").length, 3);
        Assert.assertEquals(StringUtils.split("ssss,,dddd", ",")[1], "");
        Assert.assertEquals(StringUtils.split("ssss,,dddd,,,", ",").length, 6);
        Assert.assertEquals(StringUtils.split("ssss,,dddd,,,", ",")[3], "");
        Assert.assertEquals(StringUtils.split("ssss,,dddd,,,", ",")[4], "");
        Assert.assertEquals(StringUtils.split("ssss,,dddd,,,", ",")[5], "");
        Assert.assertEquals(StringUtils.split("ssss;;dddd;;;", ";").length, 6);
    }

    @Test
    public void testRemoveElements() {
        // 测试方法：removeElements(String str, String remove)
        Assert.assertNull(StringUtils.removeElements(null, ""));
        Assert.assertNull(StringUtils.removeElements(null, "ss"));
        Assert.assertEquals(StringUtils.removeElements("", null), "");
        Assert.assertEquals(StringUtils.removeElements("ss", null), "ss");
        Assert.assertEquals(StringUtils.removeElements("ss", ""), "ss");
        Assert.assertEquals(StringUtils.removeElements("ss,dd", "d"), "ss,dd");
        Assert.assertEquals(StringUtils.removeElements("ss,dd", "dd"), "ss");
        Assert.assertEquals(StringUtils.removeElements("ss,ss,dd,ss", "ss"), "dd");
        Assert.assertEquals(StringUtils.removeElements("ss,ss,dd,ss,ff;ss", "ss"), "dd,ff;ss");
        Assert.assertEquals(StringUtils.removeElements("ss,ss,dd,ss,ff;ss", "ss,dd"), "ff;ss");
        // 测试方法：removeElements(String str, String remove, String split)
        Assert.assertNull(StringUtils.removeElements(null, "", null));
        Assert.assertNull(StringUtils.removeElements(null, "", ""));
        Assert.assertNull(StringUtils.removeElements(null, "ss", null));
        Assert.assertNull(StringUtils.removeElements(null, "ss", "sd"));
        Assert.assertEquals(StringUtils.removeElements("ss", "", null), "ss");
        Assert.assertEquals(StringUtils.removeElements("ss,dd", "ss", null), "ss,dd");
        Assert.assertEquals(StringUtils.removeElements("ss,dd", "ss", ";"), "ss,dd");
        Assert.assertEquals(StringUtils.removeElements("ss,dd", "ss", ","), "dd");
        Assert.assertEquals(StringUtils.removeElements("ss,ss,dd,ss,ff;ss", "ss", ","), "dd,ff;ss");
        Assert.assertEquals(StringUtils.removeElements("ss,ss,dd,ss,ff;ss", "ss,dd", ","), "ff;ss");
        Assert.assertEquals(StringUtils.removeElements("ss,ss,dd,ss,ff;ss", "ss,dd", ";"), "ss,ss,dd,ss,ff,ss");
        Assert.assertEquals(StringUtils.removeElements("ss,ss,dd,ss,ff;ss", "ss;dd", ";"), "ss,ss,dd,ss,ff");
        // 测试方法：removeElements(String[] str, String[] remove)
        Assert.assertNull(StringUtils.removeElements(null, new String[]{}));
        Assert.assertNull(StringUtils.removeElements(null, new String[]{"ss"}));
        Assert.assertEquals(StringUtils.removeElements(new String[]{}, null).length, 0);
        Assert.assertEquals(StringUtils.removeElements(new String[]{""}, null).length, 1);
        Assert.assertEquals(StringUtils.removeElements(new String[]{"ss"}, null)[0], "ss");
        Assert.assertEquals(StringUtils.removeElements(new String[]{""}, new String[]{}).length, 1);
        Assert.assertEquals(
                StringUtils.removeElements(new String[]{"aa", "bb", "cc", "aa", "ee"}, new String[]{"a"}).length, 5);
        Assert.assertEquals(
                StringUtils.removeElements(new String[]{"aa", "bb", "cc", "aa", "ee"}, new String[]{"aa"}).length, 3);
    }

    @Test
    public void testToUpperForWord() {
        Assert.assertNull(StringUtils.toUpperForWord(null));
        Assert.assertEquals(StringUtils.toUpperForWord(""), "");
        Assert.assertEquals(StringUtils.toUpperForWord("word"), "Word");
        Assert.assertEquals(StringUtils.toUpperForWord("word这test"), "Word这test");
        Assert.assertEquals(StringUtils.toUpperForWord("word Test"), "Word test");
        Assert.assertEquals(StringUtils.toUpperForWord("wOrD"), "Word");
    }

    @Test
    public void testToUpperForName() {
        Assert.assertNull(StringUtils.toUpperForName(null));
        Assert.assertEquals(StringUtils.toUpperForName(""), "");
        Assert.assertEquals(StringUtils.toUpperForName("word test"), "Word Test");
        Assert.assertEquals(StringUtils.toUpperForName(" word test "), "Word Test");
        Assert.assertEquals(StringUtils.toUpperForName("word这test"), "Word Test");
        Assert.assertEquals(StringUtils.toUpperForName("word汉字test"), "Word Test");
        Assert.assertEquals(StringUtils.toUpperForName("wOrd tEsT"), "Word Test");
    }

    @Test
    public void testIsContained() {
        // 测试方法：isContained(String regex, String str)
        Assert.assertFalse(StringUtils.isContained(null, ""));
        Assert.assertTrue(StringUtils.isContained("", ""));
        Assert.assertFalse(StringUtils.isContained("sd", ""));
        Assert.assertFalse(StringUtils.isContained("s", "sd"));
        Assert.assertTrue(StringUtils.isContained("s", "s,d"));
        Assert.assertFalse(StringUtils.isContained("s", " s,d"));
        Assert.assertFalse(StringUtils.isContained("", "s,d,"));
        Assert.assertTrue(StringUtils.isContained("", "s,,d"));
        // 测试方法：isContained(String regex, String str, String split)
        Assert.assertFalse(StringUtils.isContained(null, "", null));
        Assert.assertFalse(StringUtils.isContained("", null, ""));
        Assert.assertFalse(StringUtils.isContained(null, "", ""));
        Assert.assertFalse(StringUtils.isContained(null, "", "anySymbol"));
        Assert.assertTrue(StringUtils.isContained("", "", null));
        Assert.assertTrue(StringUtils.isContained("", "", ""));
        Assert.assertTrue(StringUtils.isContained("", "", "anySymbol"));
        Assert.assertFalse(StringUtils.isContained("sd", "", "anySymbol"));
        Assert.assertFalse(StringUtils.isContained("s", "sd", "anySymbol"));
        Assert.assertTrue(StringUtils.isContained("s", "s,d", ","));
        Assert.assertFalse(StringUtils.isContained("s", "s,d", ";"));// 分隔符不对
        Assert.assertFalse(StringUtils.isContained("s", " s,d", ","));// “s”前面有空格，用任何字符都没有用
        Assert.assertFalse(StringUtils.isContained("s", " s,d", "anySymbol"));
        Assert.assertFalse(StringUtils.isContained("", "s,d,", "anySymbol"));
        Assert.assertTrue(StringUtils.isContained("", "s,,d", ","));
        Assert.assertFalse(StringUtils.isContained("", "s,,d", ";"));
        Assert.assertTrue(StringUtils.isContained("s,s", "s,s;s,d", ";"));
        Assert.assertFalse(StringUtils.isContained("s,s", "s,s;s,d", ","));
        // 测试方法：isContained(String regex, String[] str)
        Assert.assertFalse(StringUtils.isContained(null, new String[]{}));
        Assert.assertFalse(StringUtils.isContained(null, new String[]{"anyContent"}));
        Assert.assertFalse(StringUtils.isContained("", new String[]{}));
        Assert.assertTrue(StringUtils.isContained("", new String[]{""}));
        Assert.assertFalse(StringUtils.isContained("", new String[]{"anyContent"}));
        Assert.assertFalse(StringUtils.isContained("sd", new String[]{""}));
        Assert.assertFalse(StringUtils.isContained("s", new String[]{"sd"}));
        Assert.assertTrue(StringUtils.isContained("s", new String[]{"s", "d"}));
        Assert.assertFalse(StringUtils.isContained("s", new String[]{" s", "d"}));
        Assert.assertTrue(StringUtils.isContained("", new String[]{"s", "", "d"}));
    }

    @Test
    public void testIsNumber() {
        // 测试方法：isNumber(String val)
        Assert.assertFalse(StringUtils.isNumber(null));
        Assert.assertFalse(StringUtils.isNumber(""));
        Assert.assertFalse(StringUtils.isNumber(" "));
        Assert.assertFalse(StringUtils.isNumber("a"));
        Assert.assertFalse(StringUtils.isNumber("1/2"));
        Assert.assertTrue(StringUtils.isNumber("0"));
        Assert.assertTrue(StringUtils.isNumber("0.0"));
        Assert.assertTrue(StringUtils.isNumber("0.01"));
        Assert.assertTrue(StringUtils.isNumber("-0.0"));
        Assert.assertTrue(StringUtils.isNumber("-0.01"));
        Assert.assertTrue(StringUtils.isNumber("1"));
        Assert.assertTrue(StringUtils.isNumber("-1"));
        Assert.assertTrue(StringUtils.isNumber("1.0"));
        Assert.assertTrue(StringUtils.isNumber("-1.0"));
        Assert.assertFalse(StringUtils.isNumber("-.0"));
        Assert.assertFalse(StringUtils.isNumber("-11.022.0"));
        // 测试方法：isNumber(String val, boolean beNull)
        Assert.assertFalse(StringUtils.isNumber(null, false));
        Assert.assertFalse(StringUtils.isNumber(null, true));// 注意null与""值时的区别。
        Assert.assertFalse(StringUtils.isNumber("", false));
        Assert.assertTrue(StringUtils.isNumber("", true));
        Assert.assertFalse(StringUtils.isNumber(" ", true));
        Assert.assertFalse(StringUtils.isNumber("a", true));
        Assert.assertFalse(StringUtils.isNumber("1/2", true));
        Assert.assertTrue(StringUtils.isNumber("0", true));
        Assert.assertTrue(StringUtils.isNumber("0.0", true));
        Assert.assertTrue(StringUtils.isNumber("0.01", true));
        Assert.assertTrue(StringUtils.isNumber("-0.0", true));
        Assert.assertTrue(StringUtils.isNumber("-0.01", true));
        Assert.assertTrue(StringUtils.isNumber("1", true));
        Assert.assertTrue(StringUtils.isNumber("-1", true));
        Assert.assertTrue(StringUtils.isNumber("1.0", true));
        Assert.assertTrue(StringUtils.isNumber("-1.0", true));

        Assert.assertFalse(StringUtils.isNumber(" ", false));
        Assert.assertFalse(StringUtils.isNumber("a", false));
        Assert.assertFalse(StringUtils.isNumber("1/2", false));
        Assert.assertTrue(StringUtils.isNumber("0", false));
        Assert.assertTrue(StringUtils.isNumber("0.0", false));
        Assert.assertTrue(StringUtils.isNumber("0.01", false));
        Assert.assertTrue(StringUtils.isNumber("-0.0", false));
        Assert.assertTrue(StringUtils.isNumber("-0.01", false));
        Assert.assertTrue(StringUtils.isNumber("1", false));
        Assert.assertTrue(StringUtils.isNumber("-1", false));
        Assert.assertTrue(StringUtils.isNumber("1.0", false));
        Assert.assertTrue(StringUtils.isNumber("-1.0", false));
    }

    @Test
    public void testIsInteger() {
        // 测试方法：isInteger(String val)
        Assert.assertFalse(StringUtils.isInteger(null));
        Assert.assertFalse(StringUtils.isInteger(""));
        Assert.assertFalse(StringUtils.isInteger(" "));
        Assert.assertFalse(StringUtils.isInteger("a"));
        Assert.assertFalse(StringUtils.isInteger("1/2"));
        Assert.assertTrue(StringUtils.isInteger("0"));
        Assert.assertFalse(StringUtils.isInteger("0.0"));
        Assert.assertFalse(StringUtils.isInteger("0.01"));
        Assert.assertFalse(StringUtils.isInteger("-0.0"));
        Assert.assertFalse(StringUtils.isInteger("-0.01"));
        Assert.assertTrue(StringUtils.isInteger("1"));
        Assert.assertTrue(StringUtils.isInteger("-1"));
        Assert.assertFalse(StringUtils.isInteger("1.0"));
        Assert.assertFalse(StringUtils.isInteger("-1.0"));
        // 测试方法：isInteger(String val, boolean beNull)
        Assert.assertFalse(StringUtils.isInteger(null, false));
        Assert.assertFalse(StringUtils.isInteger(null, true));// 注意null与""值时的区别。
        Assert.assertFalse(StringUtils.isInteger("", false));
        Assert.assertTrue(StringUtils.isInteger("", true));
        Assert.assertFalse(StringUtils.isInteger(" ", true));
        Assert.assertFalse(StringUtils.isInteger("a", true));
        Assert.assertFalse(StringUtils.isInteger("1/2", true));
        Assert.assertTrue(StringUtils.isInteger("0", true));
        Assert.assertFalse(StringUtils.isInteger("0.0", true));
        Assert.assertFalse(StringUtils.isInteger("0.01", true));
        Assert.assertFalse(StringUtils.isInteger("-0.0", true));
        Assert.assertFalse(StringUtils.isInteger("-0.01", true));
        Assert.assertTrue(StringUtils.isInteger("1", true));
        Assert.assertTrue(StringUtils.isInteger("-1", true));
        Assert.assertFalse(StringUtils.isInteger("1.0", true));
        Assert.assertFalse(StringUtils.isInteger("-1.0", true));

        Assert.assertFalse(StringUtils.isInteger(" ", false));
        Assert.assertFalse(StringUtils.isInteger("a", false));
        Assert.assertFalse(StringUtils.isInteger("1/2", false));
        Assert.assertTrue(StringUtils.isInteger("0", false));
        Assert.assertFalse(StringUtils.isInteger("0.0", false));
        Assert.assertFalse(StringUtils.isInteger("0.01", false));
        Assert.assertFalse(StringUtils.isInteger("-0.0", false));
        Assert.assertFalse(StringUtils.isInteger("-0.01", false));
        Assert.assertTrue(StringUtils.isInteger("1", false));
        Assert.assertTrue(StringUtils.isInteger("-1", false));
        Assert.assertFalse(StringUtils.isInteger("1.0", false));
        Assert.assertFalse(StringUtils.isInteger("-1.0", false));
    }

    @Test
    public void testRemoveDubAndEmpty() {
        String str = "1,2,3,2,,45";
        Assert.assertEquals("1,2,3,45", StringUtils.removeDubAndEmpty(str));
    }

    @Test
    public void testTrimToEmpty() {
        Assert.assertEquals("", StringUtils.trimToEmpty(null));
        Assert.assertEquals("", StringUtils.trimToEmpty(""));
        Assert.assertEquals("", StringUtils.trimToEmpty("     "));
        Assert.assertEquals("abc", StringUtils.trimToEmpty("abc"));
        Assert.assertEquals("abc", StringUtils.trimToEmpty("    abc    "));
        Assert.assertEquals("abc", StringUtils.trimToEmpty("    abc　"));
    }

    @Test
    public void testSimpleFormatText() {
        Assert.assertEquals("a<br/>b<br>ddd<br/>", StringUtils.simpleFormatText("a\nb<br>ddd\n"));
    }

    @Test
    public void testSubstringBefore() {
        String str = "A,B,,";
        Assert.assertEquals("", StringUtils.substringBefore("", 1));
        Assert.assertEquals(null, StringUtils.substringBefore(null, 1));
        Assert.assertEquals(str, StringUtils.substringBefore(str, 0));
        Assert.assertEquals(str, StringUtils.substringBefore(str, -1));// 小于0的都是如此
        Assert.assertEquals("A", StringUtils.substringBefore(str, 1));
        Assert.assertEquals("A,B", StringUtils.substringBefore(str, 2));
        Assert.assertEquals("A,B,", StringUtils.substringBefore(str, 3));
        Assert.assertEquals("A,B,,", StringUtils.substringBefore(str, 4));
        Assert.assertEquals("A,B,,", StringUtils.substringBefore(str, 5));// 大于4的都是如此
        Assert.assertEquals("A,B,C", StringUtils.substringBefore("A,B,C", 5));// 大于4的都是如此
    }

    @Test
    public void testFillString() {
        Assert.assertEquals("1100000000", StringUtils.fillString("11", "0", 8));
    }

    @Test
    public void testParse2Array() {
        Assert.assertNull(StringUtils.parse2Array(null));
        Assert.assertEquals("", StringUtils.parse2Array("")[0]);
        Assert.assertEquals("2011-01-10 12:00:21",
                StringUtils.parse2Array(DateUtils.formatString2Date("2011-01-10 12:00:21"))[0]);
        Assert.assertEquals("2011-01-10 00:00:00",
                StringUtils.parse2Array(DateUtils.formatString2Date("2011-01-10"))[0]);
        Assert.assertEquals("", StringUtils.parse2Array(new String[]{"", "1"})[0]);
        Assert.assertEquals("1", StringUtils.parse2Array(new String[]{"1", "2"})[0]);
        Assert.assertEquals("1", StringUtils.parse2Array(new Long[]{1L, 2L})[0]);
        Assert.assertEquals("1", StringUtils.parse2Array(new int[]{1, 2})[0]);
        Assert.assertNotNull(StringUtils.parse2Array(new char[]{1, 2}));
        Assert.assertEquals("1", StringUtils.parse2Array(new short[]{1, 2})[0]);
        Assert.assertEquals("1", StringUtils.parse2Array(new byte[]{1, 2})[0]);
        Assert.assertEquals("1", StringUtils.parse2Array(new long[]{1, 2})[0]);
        Assert.assertEquals("1.0", StringUtils.parse2Array(new float[]{1, 2})[0]);
        Assert.assertEquals("1.0", StringUtils.parse2Array(new double[]{1, 2})[0]);
        Assert.assertEquals("true", StringUtils.parse2Array(new boolean[]{true, false})[0]);
        List<Long> list = new ArrayList<Long>();
        list.add(1L);
        list.add(2L);
        Assert.assertEquals("1", StringUtils.parse2Array(list)[0]);
        Assert.assertNull(StringUtils.parse2Array(new HashMap<Object, Object>()));
    }

    @Test
    public void testGetLongArr() {
        Assert.assertNull(StringUtils.getLongArr(new HashMap<String, String>().get("1")));
        Assert.assertNull(StringUtils.getLongArr(new String[]{}));
        Assert.assertNull(StringUtils.getLongArr(new String[]{"", ""}));
        Assert.assertNull(StringUtils.getLongArr(new String[]{"a", "b1"}));
        Assert.assertEquals(1, StringUtils.getLongArr(new String[]{"1", ""}).length);
        Assert.assertEquals(1L, StringUtils.getLongArr(new String[]{"1", ""})[0].longValue());
    }

    @Test
    public void testFillStringBefore() {
        Assert.assertEquals("aa", StringUtils.fillStringBefore("aa", '0', 1));
        Assert.assertEquals("aa", StringUtils.fillStringBefore("aa", '0', 2));
        Assert.assertEquals("0aa", StringUtils.fillStringBefore("aa", '0', 3));
        Assert.assertEquals("0", StringUtils.fillStringBefore("", '0', 1));
        Assert.assertEquals("0", StringUtils.fillStringBefore(null, '0', 1));
    }

    @Test
    public void testIsContinuousNumberOrLetter() {
        Assert.assertFalse(StringUtils.isContinuousNumberOrLetter("123456a"));
        Assert.assertTrue(StringUtils.isContinuousNumberOrLetter("123456"));
        Assert.assertTrue(StringUtils.isContinuousNumberOrLetter("123456789"));
        Assert.assertTrue(StringUtils.isContinuousNumberOrLetter("abcdefg"));
        Assert.assertTrue(StringUtils.isContinuousNumberOrLetter("ABCDEFG"));
        Assert.assertFalse(StringUtils.isContinuousNumberOrLetter("ABCDEFGh"));
        Assert.assertFalse(StringUtils.isContinuousNumberOrLetter("1234560"));
        Assert.assertFalse(StringUtils.isContinuousNumberOrLetter("#123456a"));
        Assert.assertFalse(StringUtils.isContinuousNumberOrLetter("a123456"));

    }
}
