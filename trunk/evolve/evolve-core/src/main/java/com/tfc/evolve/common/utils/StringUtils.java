package com.tfc.evolve.common.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * <li>字符串工具类</li>
 * <p>
 * 继承自org.apache.commons.lang.StringUtils，提供常用字符串处理方法
 * <p>
 * 继承可能不是一个很好的模式，如有条件，请使用包装模式修改
 * 
 * @see org.apache.commons.lang.StringUtils
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
    protected static final String FILE_NAME_ILLAGEL_STRING_REGEX = "[/\\\\:*?\"<>\\|]";

    /**
     * 去除指定字符串的最后一个英文逗号<br>
     * <I>例如：</I>如果对于字符串："string1,string2,"需要去除最后一个","，则调用该方法：truncateComma("string1,string2,")<BR>
     * <I>说明：</I>1、如果指定的字符串（string）为null或""，则依然返回null或""；如果替换失败，则返回原字符串. 另外，如果指定的字符在结尾处不存在，则返回原字符串 <BR>
     * 2、如果指定的字符串（string）的结尾没有逗号，则不做处理，直接返回。
     * 
     * @param str 指定的字符串
     * @return 去除最后一个逗号的字符串
     */
    public static String truncateComma(String str) {
        return trimEndChar(str, ",");
    }

    /**
     * 去除指定字符串的最后一个指定的字符<br>
     * <I>例如：</I>如果对于字符串："string1,string2,"需要去除最后一个","，则调用该方法：trimEndChar("string1,string2,",",")<BR>
     * 另外，该方法还支持去除字符串，如：trimEndChar("string1,string2,","string3")<BR>
     * <I>说明：</I>1、如果指定的字符串（string）为null或""，则依然返回null或""；如果替换失败，则返回原字符串. 另外，如果指定的字符在结尾处不存在，则返回原字符串 <BR>
     * 2、如果指定的字符串（string）的结尾没有逗号，则不做处理，直接返回。 <br>
     * 3、如果chr为空或null，则直接返回原值。
     * 
     * @param str 指定的字符串
     * @param chr 需要去除的字符
     * @return 去除最后一个指定的字符的字符串
     */
    public static String trimEndChar(String str, String chr) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(chr)) {
            return str;
        }
        if (str.endsWith(chr) && (str.lastIndexOf(chr) != -1)) {
            str = str.substring(0, str.lastIndexOf(chr));
        }
        return str;
    }

    /**
     * 将字符串数组排序后返回排序好的字符串，用“,”号隔开<BR>
     * 说明：升序排列。如果str为空或为null，则返回""。
     * 
     * @param str 预排序的字符串数组
     * @return 排序好的字符串
     */
    public static String sortStringArray(String[] str) {
        if ((null == str) || (str.length < 1)) {
            return "";
        }
        for (int i = 0; i < str.length; i++) {
            String role = str[i];
            for (int j = i + 1; j < str.length; j++) {
                if (role.compareToIgnoreCase(str[j]) > 0) {
                    str[i] = str[j];
                    str[j] = role;
                    role = str[i];
                }
            }
        }
        return concatElements(str);
    }

    /**
     * 将字符串数组连接成字符串，以逗号隔开,如果数组中有元素为null或""的，将会被过滤掉。如果拼接结果为空，则返回""。
     * 
     * @param str 字符串数组
     * @return 连接成的字符串
     */
    public static String concatElements(String... str) {
        return concatElements(str, ",");
    }

    /**
     * 将字符串数组连接成字符串,以指定的字符串隔开每个元素,如果数组中有元素为null或""的，将会被过滤掉 <br>
     * 注：如果两个参数为空或为null，则返回""。
     * 
     * @param str 字符串数组
     * @param chr 隔开字符串的字符
     * @return 连接成的字符串
     */
    public static String concatElements(String[] str, String chr) {
        if ((null == str) || (str.length <= 0) || isEmpty(chr)) {
            return "";
        }
        StringBuffer result = new StringBuffer("");
        for (String element : str) {
            element = trim(element);
            if (isNotEmpty(element)) {
                result.append(element);
                result.append(chr);
            }
        }
        return trimEndChar(result.toString(), chr);
    }

    /**
     * 将字符串以指定的字符分隔成为一个数组。与JDK自带的split的主要的表面区别在于：这个方法的参数与JDK自带的不同。<BR>
     * 其实，在分隔方面也是不同的。如：String s = "string1,string2,";JDK的split方法得到的数组长度为2，但是这个方法得到的长度是3，其中最后一个元素是空字符串。<BR>
     * 例如：String[] s=StringUtils.split("string1,string2,",","); 则：s.length=3,其中,s[2]=="";<BR>
     * String[] s=StringUtils.split(",,",","); 则：s.length=3,其中,每个元素都是空字符串，即""。<br>
     * String[] s=StringUtils.split(",sd,,,",","); 则：s.length=5,其中,只有s[1]=="sd"，其余的每个元素都是空字符串，即""。<br>
     * 其余情况与JDK一样<BR>
     * JDK中的split方法对末尾的所有指定分割符是不处理的，也就是直接截掉。这个方法是对末尾的指定分割符作相应的数组元素处理的：即认为有n个分割符就有n+1个元素。
     * 
     * @param str 需要分割的字符串。如果该值为空，则返回一个<code>new String[]{""}</code>
     * @param chr 指定分隔的标志。如果该值为空，则返回<code>new String[]{str}</code>
     * @return 返回分割后的数组
     */
    public static String[] split(String str, String chr) {
        if (isEmpty(str)) {
            return new String[]{""};
        }
        if (isEmpty(chr)) {
            return new String[]{str};
        }
        String[] result = null;
        if (str.endsWith(chr)) {
            String[] strs = str.split(chr);
            // 都是空的情况，如：“,,,,,”
            if (strs.length == 0) {
                result = new String[str.length() + 1];
                for (int i = 0; i < result.length; i++) {
                    result[i] = "";
                }
            }
            // 前面不为空，后面有多个空的情况,如“sss,sd,,,ss,,,,,”
            else if (str.endsWith(chr + chr)) {
                int idx = str.lastIndexOf(strs[strs.length - 1]) + strs[strs.length - 1].length();
                String newStr = str.substring(idx);
                result = new String[strs.length + newStr.length()];
                for (int i = 0; i < result.length; i++) {
                    if (i < strs.length) {
                        result[i] = strs[i];
                    }
                    else {
                        result[i] = "";
                    }
                }
            }
            // 其余情况
            else {
                result = new String[strs.length + 1];
                for (int i = 0; i < strs.length; i++) {
                    result[i] = strs[i];
                }
                result[strs.length] = "";
            }
        }
        else {
            result = str.split(chr);
        }
        return result;
    }

    /**
     * 去除字符串中指定的字符串，字符串中各符号以英文逗号隔开。例如：<br>
     * String s = "123,33,44,55";<BR>
     * removeElements(s,null) = "123,33,44,55"; <br>
     * removeElements(s,"") = "123,33,44,55"; <br>
     * removeElements(s,"123") = "33,44,55";<br>
     * removeElements(s,"123,55") = "33,44";<br>
     * removeElements(s,"123,44") = "33,55";<br>
     * removeElements(s,"123,4444") = "33,44,55";<br>
     * removeElements(s,"123,33,44,55") = ""<br>
     * removeElements("123,33,123,44,55","123") = "33,44,55"<br>
     * removeElements("",*) = ""(*表示任何字符串或null)<br>
     * removeElements(null,*) =null(*表示任何字符串或null)
     * 
     * @param str 源字符串
     * @param remove 要移除的字符串
     * @return 返回处理后的字符串
     */
    public static String removeElements(String str, String remove) {
        return removeElements(str, remove, ",");
    }

    /**
     * 去除字符串中指定的字符串，要求用户指定分割符。例如：<br>
     * String s = "123,33,44,55";<BR>
     * removeElements(s,null,",") = "123,33,44,55"; <br>
     * removeElements(s,"",",") = "123,33,44,55"; <br>
     * removeElements(s,"123",",") = "33,44,55";<br>
     * removeElements(s,"123,55",",") = "33,44";<br>
     * removeElements(s,"123,44",",") = "33,55";<br>
     * removeElements(s,"123,4444",",") = "33,44,55";<br>
     * removeElements(s,"123,33,44,55",",") = ""<br>
     * removeElements("123,33,123,44,55","123",",") = "33,44,55"<br>
     * removeElements("",*,*) = ""(*表示任何字符串或null)<br>
     * removeElements(null,*,*) =null(*表示任何字符串或null)<br>
     * removeElements("string1;string2,string5;string3;string4","string3",";") = "string1;string2,string5;string4";<br>
     * removeElements("string1;string2,string5;string3;string4","string2",";") =
     * "string1;string2,string5;string3;string4";
     * 
     * @param str 源字符串
     * @param remove 要移除的字符串
     * @param split 分割符
     * @return 返回处理后的字符串
     */
    public static String removeElements(String str, String remove, String split) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (isEmpty(split)) {
            return concatElements(removeElements(new String[]{str}, new String[]{remove}));
        }
        else {
            return concatElements(removeElements(str.split(split), remove.split(split)));
        }
    }

    /**
     * 移除数组中指定的数组元素，例如：<br>
     * String[] s = {"123","33","44","55"};<BR>
     * removeElements(s,null) = {"123","33","44","55"}; <br>
     * removeElements(s,{""}) = {"123","33","44","55"}; <br>
     * removeElements(s,{"123"}) = {"33","44","55"};<br>
     * removeElements(s,{"123","55"}) = {"33","44"};<br>
     * removeElements(s,{"123","44"}) = {"33","55"};<br>
     * removeElements(s,{"123","4444"}) = {"33","44","55"};<br>
     * removeElements(s,{"123","33","44","55") = {""}<br>
     * removeElements({"123","33","123","44","55","123") = {"33","44","55"}<br>
     * removeElements({""},*,*) = {""}(*表示任何字符串或null)<br>
     * removeElements(null,*,*) =null(*表示任何字符串或null)<br>
     * 
     * @param str 源数组
     * @param remove 要移除的数组元素
     * @return 返回处理后的数组
     */
    public static String[] removeElements(String[] str, String[] remove) {
        if ((str == null) || (remove == null) || (str.length == 0) || (remove.length == 0)) {
            return str;
        }
        for (String subString : remove) {
            for (int i = 0; i < str.length; i++) {
                if (subString.equals(str[i])) {
                    str[i] = null;
                }
            }
        }
        return concatElements(str).split(",");
    }

    /**
     * 单词的首字母都大写<BR>
     * toUpperForWord(null) = null;<BR>
     * toUpperForWord("") = "";<BR>
     * toUpperForWord(\w) = \w;（\w 指：除字母以外的字符，如空格，数字，汉字，各种符号等等）<br>
     * toUpperForWord("word") = "Word";<BR>
     * toUpperForWord(" word ") = "word";<BR>
     * toUpperForWord("word test") = "Word test";<br>
     * toUpperForWord("woRd Test") = "Word test";
     * 
     * @param str 需要处理的单词
     * @return 大写处理后的单词
     */
    public static String toUpperForWord(String str) {
        if (StringUtils.isNotEmpty(str)) {
            str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
        return str;
    }

    /**
     * 英文名称首字母大写<BR>
     * toUpperForName(null) = null;<BR>
     * toUpperForName("") = "";<BR>
     * toUpperForName(\w) = "";（\w 指：除字母以外的字符，如空格，数字，汉字，各种符号等等）<br>
     * toUpperForName("word") = "Word";<BR>
     * toUpperForName(" word ") = "Word";<BR>
     * toUpperForName("word test") = "Word Test";<br>
     * toUpperForName("woRd TeSt") = "Word Test";<br>
     * toUpperForName("woRd\wTeSt") = "Word Test";（\w 指：除字母以外的字符，如空格，数字，汉字，各种符号等等）<br>
     * 
     * @param name 英文名称
     * @return 大写后的英文名称
     */
    public static String toUpperForName(String name) {
        if (StringUtils.isEmpty(name)) {
            return name;
        }
        name = name.toLowerCase();
        String s1 = "";
        String str[] = name.split("([^a-zA-Z0-9']|[\u4E00-\u9FA5])+");
        for (int i = 0; i < str.length; i++) {
            s1 += toUpperForWord(str[i]) + " ";
        }
        return s1.trim();
    }

    /**
     * 判断字符串是否存在于指定的字符串型的字符串集里面（默认以英文逗号分隔）<br>
     * 
     * <pre>
     * isContained(null, *) = false;
     * isContained(&quot;&quot;, &quot;&quot;) = true;
     * isContained(&quot;&quot;, &quot;,,,&quot;) = false;
     * isContained(&quot;a&quot;, &quot;abc,aa&quot;) = false;
     * isContained(&quot;aa&quot;, &quot;abc,aa&quot;) = true;
     * isContained(&quot;aa&quot;, &quot;abc, aa&quot;) = false;
     * isContained(&quot;&quot;, &quot;abc,aa,&quot;) = false;
     * 注：*表示任意字符串或&quot;&quot;或null
     * </pre>
     * 
     * @param regex 查找的字符串
     * @param str 指定的字符串集（以英文逗号分隔）
     * @return 返回true或false
     */
    public static boolean isContained(String regex, String str) {
        return isContained(regex, str, ",");
    }

    /**
     * 判断字符串是否存在于指定的字符串型的字符串集里面（以指定的分隔符分隔）<br>
     * 
     * <pre>
     * isContained(null, *, *) = false;
     * isContained(&quot;&quot;, &quot;&quot;, *) = true;
     * isContained(&quot;&quot;, &quot;,,,&quot;, &quot;,&quot;) = false;
     * isContained(*, *, &quot;&quot;) = isContained(*, *, null) = isContained(*, *);
     * isContained(&quot;a&quot;, &quot;abc,aa&quot;, &quot;,&quot;) = false;
     * isContained(&quot;aa&quot;, &quot;abc,aa&quot;, &quot;,&quot;) = true;
     * isContained(&quot;aa&quot;, &quot;abc, aa&quot;, &quot;,&quot;) = false;
     * isContained(&quot;aa&quot;, &quot;abc,aa&quot;, &quot;;&quot;) = false;
     * isContained(&quot;&quot;, &quot;abc,aa,&quot;, &quot;,&quot;) = false;
     * 注：*表示任意字符串或&quot;&quot;或null
     * </pre>
     * 
     * @param regex 查找的字符串
     * @param str 指定的字符串集（以英文逗号分隔）
     * @param split 指定的分隔符，如果该参数为""或null，则字符串集将会以英文逗号进行分隔
     * @return 返回true或false
     */
    public static boolean isContained(String regex, String str, String split) {
        if (str == null) {
            return false;
        }
        if (isEmpty(split)) {
            return isContained(regex, str);
        }
        return isContained(regex, str.split(split));
    }

    /**
     * 判断字符串是否存在于指定的数组形式的字符串集里面<br>
     * 
     * <pre>
     * isContained(null, *) = false;
     * isContained(&quot;&quot;, {&quot;&quot;}) = true;
     * isContained(&quot;&quot;, {&quot;&quot;,&quot;&quot;,&quot;&quot;}) = true;
     * isContained(*, {}) = false;
     * isContained(&quot;a&quot;, {&quot;abc&quot;,&quot;aa&quot;}) = false;
     * isContained(&quot;aa&quot;, {&quot;abc&quot;,&quot;aa&quot;}) = true;
     * isContained(&quot;aa&quot;, {&quot;abc&quot;,&quot; aa&quot;}) = false;
     * 注：*表示任意字符串或&quot;&quot;或null
     * </pre>
     * 
     * @param regex 查找的字符串
     * @param str 指定的字符串集
     * @return 返回true或false
     */
    public static boolean isContained(String regex, String[] str) {
        if (regex == null) {
            return false;
        }
        if ((str == null) || (str.length == 0)) {
            return false;
        }
        for (String item : str) {
            if (regex.equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 不区分大小写的 @see isContained(String regex, String[] str)
     * 
     * @param regex
     * @param str
     * @return
     */
    public static boolean isContainedIgnoreCase(String regex, String[] str) {
        if (regex == null) {
            return false;
        }
        if ((str == null) || (str.length == 0)) {
            return false;
        }
        for (String item : str) {
            if (regex.equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是数字，如果是数字返回true如果不是数字或是空返回false isNumber("1",*) = true;<br>
     * isNumber("as") = false;<br>
     * isNumber("1 3") = false;<br>
     * isNumber("") = false;<br>
     * isNumber("1.0") = true;<br>
     * isNumber("-1.0") = true;<br>
     * isNumber("-1") = true;<br>
     * isNumber(null) = false;
     * 
     * @param val 指定的字符串
     * @return 返回true或false
     * @since vFrw1.01-release
     */
    public static boolean isNumber(String val) {
        return isNumber(val, false);
    }

    /**
     * 是否是数字和是否可以为空 如果是数字返回true不是数字返回false<br>
     * isNumber("1",*) = true;<br>
     * isNumber("as",*) = false;<br>
     * isNumber("1 3",*) = false;<br>
     * isNumber("",false) = false;<br>
     * isNumber("",true) = true;<br>
     * isNumber("1.0",*) = true;<br>
     * isNumber("-1.0",*) = true;<br>
     * isNumber("-1",*) = true;<br>
     * isNumber(null,*) = false;
     * 
     * @param val 指定的字符串
     * @param beNull 为""时返回的结果
     * @return 返回true或false
     * @since vFrw1.01-release
     */
    public static boolean isNumber(String val, boolean beNull) {
        if (val == null) {
            return false;
        }
        if (val.equals("")) {
            return beNull;
        }
        Pattern p = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        return p.matcher(val).matches();
    }

    /**
     * 是否是数字，如果是数字返回true如果不是数字或是空返回false isNumber("1",*) = true;<br>
     * isInteger("as") = false;<br>
     * isInteger("1 3") = false;<br>
     * isInteger("") = false;<br>
     * isNumber("-1") = true;<br>
     * isNumber("1.0") = false;<br>
     * isInteger(null) = false;
     * 
     * @param val 指定的字符串
     * @return 返回true或false
     * @since vFrw1.01-release
     */
    public static boolean isInteger(String val) {
        return isInteger(val, false);
    }

    /**
     * 是否是数字和是否可以为空 如果是数字返回true不是数字返回false<br>
     * isInteger("1",*) = true;<br>
     * isInteger("as",*) = false;<br>
     * isInteger("1 3",*) = false;<br>
     * isNumber("-1",*) = true;<br>
     * isNumber("1.0",*) = false;<br>
     * isInteger("",false) = false;<br>
     * isInteger("",true) = true;<br>
     * isInteger(null,*) = false;
     * 
     * @param val 指定的字符串
     * @param beNull 为""时返回的结果
     * @return 返回true或false
     * @since vFrw1.01-release
     */
    public static boolean isInteger(String val, boolean beNull) {
        if (val == null) {
            return false;
        }
        if (val.equals("")) {
            return beNull;
        }
        Pattern p = Pattern.compile("-?[0-9]+");
        return p.matcher(val).matches();
    }

    /**
     * 替换文件名中非法字符为指定的字符串。
     * 
     * @param fileName 指定的文件名
     * @param replaceStr 替换非法字符的字符串
     * @return 返回处理后的结果
     */
    public static String replaceFileIllegalStr(String fileName, String replaceStr) {
        if (isEmpty(fileName)) {
            return fileName;
        }
        if (isEmpty(replaceStr)) {
            replaceStr = "";
        }
        return fileName.replaceAll(FILE_NAME_ILLAGEL_STRING_REGEX, replaceStr);
    }

    /**
     * 用空格替换文件名中非法字符。
     * 
     * @param fileName 指定的文件名
     * @return 返回用""替换处理后的结果
     */
    public static String replaceFileIllegalStrWithWhitespace(String fileName) {
        return replaceFileIllegalStr(fileName, " ");
    }

    /**
     * 去除以“,”分隔的字符串为元素的重复项与空项，在整个过程中会将所有元素进行trim操作。如果对应的内容为空或少于2个元素，则直接返回原字符串！<br>
     * removeDubAndEmpty(null)== null;<br>
     * removeDubAndEmpty("")== "";<br>
     * removeDubAndEmpty("str")== "str";<br>
     * removeDubAndEmpty(" str ")== "str";<br>
     * removeDubAndEmpty(" str1,")== "str1";<br>
     * removeDubAndEmpty("str1,str2,str1,")== "str1,str2";<br>
     * removeDubAndEmpty("str1, ,str2")== "str1,str2";<br>
     * 
     * @param str
     * @return
     */
    public static String removeDubAndEmpty(String str) {
        if (isEmpty(str)) {
            return str;
        }
        String[] strs = str.split(",");
        if (strs.length == 1) {
            return str;
        }
        for (int i = 0; i < strs.length; i++) {
            strs[i] = trim(strs[i]);
        }
        Set<String> strSet = new LinkedHashSet<String>(Arrays.asList(strs));
        strSet.remove("");
        return concatElements(strSet.toArray(new String[strSet.size()]));
    }

    /**
     * 将前后空格、\t等char值小于32的字符全部去除，同时将前后中文空格（即：全角空格）也去除。
     * 
     * @param str 字符串
     * @return
     */
    public static String trimToEmpty(String str) {
        str = org.apache.commons.lang.StringUtils.trimToEmpty(str);
        str = str.startsWith("　") ? str.substring(1) : str;
        return str.endsWith("　") ? str.substring(0, str.length() - 1) : str;
    }

    /**
     * 将一段纯文本简单格式化成html可显示的样式，比如将\n转化成br等
     * 
     * @param str
     * @return
     */
    public static String simpleFormatText(String str) {
        String newStr = str.replaceAll("\n", "<br/>");
        newStr = newStr.replaceAll(" ", "&nbsp;");
        return newStr;
    }

    /**
     * 将指定位置的“,”之前的内容取出来。如果内容为空或指定的位置没有“,”，则返回原内容。<br>
     * 
     * <pre>
     * String str = &quot;A,B,,&quot;;
     * Assert.assertEquals(&quot;&quot;, StringUtils.substringBefore(&quot;&quot;, 1));// 任意的数字都是这个结果
     * Assert.assertEquals(null, StringUtils.substringBefore(null, 1));// 任意的数字都是这个结果
     * Assert.assertEquals(str, StringUtils.substringBefore(str, 0));// //任意的字符串都是这个结果
     * Assert.assertEquals(str, StringUtils.substringBefore(str, -1));// 小于0的都是如此，即：任意的字符串都是这个结果
     * Assert.assertEquals(&quot;A&quot;, StringUtils.substringBefore(str, 1));
     * Assert.assertEquals(&quot;A,B&quot;, StringUtils.substringBefore(str, 2));
     * Assert.assertEquals(&quot;A,B,&quot;, StringUtils.substringBefore(str, 3));
     * Assert.assertEquals(&quot;A,B,,&quot;, StringUtils.substringBefore(str, 4));
     * Assert.assertEquals(&quot;A,B,,&quot;, StringUtils.substringBefore(str, 5));// 大于4的都是如此
     * Assert.assertEquals(&quot;A,B,C&quot;, StringUtils.substringBefore(&quot;A,B,C&quot;, 5));// 大于4的都是如此
     * </pre>
     * 
     * @param str 指定的字符串。
     * @param idx 指定的位置，即第几个。
     * @return 返回处理的结果。
     */
    public static String substringBefore(String str, int idx) {
        if (StringUtils.isEmpty(str) || idx < 1) {
            return str;
        }
        String result = "";
        for (int i = 0; i < idx; i++) {
            if (StringUtils.isEmpty(str)) {
                return result;
            }
            int x = str.indexOf(",") + 1;
            x = x == 0 ? str.length() : x;
            result += str.substring(0, x);// 将前面的所有内容都获取
            str = str.substring(x);
        }
        return truncateComma(result);
    }

    /**
     * 在指定的字符串后面追加指定次数的指定字符串。
     * 
     * @param target
     * @param append
     * @param times
     * @return
     */
    public static String fillString(String target, String append, int times) {
        append = trimToEmpty(append);
        for (int i = 0; i < times; i++) {
            target += append;
        }
        return target;
    }

    /**
     * 处理sql查询时的特殊字符，转义字符是'/'
     * 
     * @author zhouxiaohui
     * @param sqlStr
     * @return
     */
    public static String filterSqlString(String sqlStr) {
        if (isEmpty(sqlStr)) {
            return sqlStr;
        }
        sqlStr = sqlStr.replace("'", "''");
        sqlStr = sqlStr.replace("/", "//");
        sqlStr = sqlStr.replaceAll("%", "/%");
        sqlStr = sqlStr.replaceAll("_", "/_");
        return sqlStr;
    }

    /**
     * 将转入的内容转换为字符串数组。
     * 
     * @param rangeFieldValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String[] parse2Array(Object rangeFieldValue) {
        if (rangeFieldValue == null) {
            return null;
        }
        // 分情况将正确的值进行相应的处理。
        if (rangeFieldValue.getClass().isArray()) {
            int len = Array.getLength(rangeFieldValue);
            String[] strs = new String[len];
            // 如果是数组，则将内容转换为String数组
            if (ClassUtils.isPrimitiveArray(rangeFieldValue)) {
                for (int i = 0; i < len; i++) {
                    strs[i] = String.valueOf(Array.get(rangeFieldValue, i));
                }
                return strs;
            }
            Object[] values = (Object[]) rangeFieldValue;
            for (int i = 0; i < values.length; i++) {
                strs[i] = String.valueOf(values[i]);
            }
            return strs;
        }
        else if (rangeFieldValue instanceof Map) {
            // Map类型的现在不作任何操作
            return null;
        }
        else if (rangeFieldValue instanceof Date) {
            // 日期类型的，将日期转换为指定的格式
            return new String[]{DateUtils.parseDate2String((Date) rangeFieldValue, "yyyy-MM-dd HH:mm:ss")};
        }
        else if (rangeFieldValue instanceof Collection<?>) {
            List<Object> values = (List<Object>) rangeFieldValue;
            // if(rangeFieldValue)
            String[] strs = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                strs[i] = String.valueOf(values.get(i));
            }
            // 队列
            return strs;
        }
        else {
            return new String[]{String.valueOf(rangeFieldValue)};
        }
    }

    /**
     * 将字符串数组转换为Long数组。字符串数组中一切不是数字的元素都将被过滤！
     * 
     * @param strIds 指定的字符串型的数字数组
     * @return 返回对应的数字
     */
    public static Long[] getLongArr(String... strIds) {
        if (strIds == null || strIds.length < 1) {
            return null;
        }
        List<Long> result = new LinkedList<Long>();
        for (String id : strIds) {
            id = StringUtils.trim(id);
            if (StringUtils.isNotEmpty(id) && StringUtils.isInteger(id)) {
                result.add(Long.parseLong(id));
            }
        }
        return result.isEmpty() ? null : result.toArray(new Long[0]);
    }

    /**
     * 增加指定的内容到字符串中，直到指定的长度为止
     * 
     * @param target 需要增加的字符串
     * @param append 增加的内容
     * @param length 增加之后的长度
     * @return 返回增加之后的内容
     */
    public static String fillStringBefore(String target, char append, int length) {
        if (length < 1 || trimToEmpty(target).length() >= length) {
            return target;
        }
        target = trimToEmpty(target);
        int forTimes = length - target.length();
        String tmp = "";
        for (int i = 0; i < forTimes; i++) {
            tmp += append;
        }
        return tmp + target;
    }

    /**
     * 检查输入串是否是连续的数字或英文字母
     * 
     * @param target 需要检查的字符串
     * @return true-是连续的数字或英文字母 false-不是连续的数字或英文字母
     */
    public static boolean isContinuousNumberOrLetter(String target) {
        for (int i = 0; i < target.length(); i++) {
            char charCode = target.charAt(i);
            // 如果不是数字或英文字母，直接返回假
            if (charCode < 48 || (charCode > 57 && charCode < 65) || (charCode > 90 && charCode < 97) || charCode > 122) {
                return false;
            }
            if (i > 0 && (charCode - target.charAt(i - 1)) != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较2个字符串值是否相同
     * 
     * @author zhouxiaohui
     * @param str1 字符串1
     * @param str2字符串2
     * @return(isEquals("", "") == true); (isEquals("", "1") == false); (isEquals("", null) == true);(isEquals("1", "1")
     *                      == true); (isEquals("1", "") == false); (isEquals(null, "") == true);
     */
    public static boolean isEquals(String str1, String str2) {
        if (org.apache.commons.lang.StringUtils.isEmpty(str1) && org.apache.commons.lang.StringUtils.isEmpty(str2)) {
            return true;
        }
        else if (org.apache.commons.lang.StringUtils.isNotEmpty(str1)
                && org.apache.commons.lang.StringUtils.isEmpty(str2)) {
            return str1.equals(str2);
        }
        else if (org.apache.commons.lang.StringUtils.isNotEmpty(str2)) {
            return str2.equals(str1);
        }
        return true;
    }

    /**
     * 截取受限制的字符串<br>
     * ("123.jpg",10) 结果123.jpg <br>
     * ("1234567890.jpg",10) 结果123456.jpg
     * 
     * @author zhouxiaohui
     * @param original 原始字符串
     * @param limitedLen 限制长度
     * @return 返回处理后的结果
     */
    public static String cutString(String original, int limitedLen) {
        if (isEmpty(original) || original.length() <= limitedLen) {
            return original;
        }
        int lastIndex = original.lastIndexOf(".");

        String endStr = original.substring(lastIndex);

        String newHeadStr = original.substring(0, limitedLen - endStr.length());
        return newHeadStr + endStr;

    }

}
