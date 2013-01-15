package com.tfc.word.spell.convert;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

import com.tfc.word.spell.dic.SingleCnSpellDic;

/**
 * 将每个汉字转化为全拼,码表利用输入法的码表导出，汉字信息非常全，平时见到的汉字 转拼音的API只能处理几千个一级汉字，很多字都没有法查到，尤其是我自己的姓
 * 都查不到所以只好自己做一个汉字转拼音的API，该API除可以处理99％以上的汉字， 而且可以返回多音字的读音
 */
public class SingleCnToSpell {
    /**
     * 将类似“215-214”转换成“字”。
     * 
     * @param ascii
     * @return
     */
    public static String getCnByAscii(String ascii) {
        if (StringUtils.isBlank(ascii) || ascii.split("-").length != 2) {
            return ascii;
        }
        try {
            String[] ss = ascii.split("-");
            int i = Integer.parseInt(StringUtils.trimToEmpty(ss[0]));
            int j = Integer.parseInt(StringUtils.trimToEmpty(ss[1]));
            return new String(new byte[]{(byte) i, (byte) j}, "GBK");
        }
        catch (UnsupportedEncodingException e) {
            return ascii;
        }
    }

    /**
     * 获得单个汉字的Ascii，并用"-"连接成一个字符串
     * 
     * @param cn char 汉字字符
     * @return string 错误返回 空字符串,否则返回ascii
     */
    public static String getCnAscii(char cn) {
        try {
            byte[] bytes = (String.valueOf(cn)).getBytes("GBK");// 使用GBK编码，这样可以确保这是转成bytes时长度为2
            if (bytes == null || bytes.length > 2 || bytes.length <= 0) { // 错误
                return "";
            }
            if (bytes.length == 1) { // 英文字符
                return new String(bytes, "GBK");
            }
            if (bytes.length == 2) { // 中文字符
                int hightByte = 256 + bytes[0];
                int lowByte = 256 + bytes[1];
                String ascii = hightByte + "-" + lowByte;
                return ascii;
            }
        }
        catch (Exception e) {
        }
        return ""; // 错误
    }

    /**
     * 根据ASCII码连接成的字符串到SpellMap中查找对应的拼音。即：获取对应的拼音！！
     * 
     * @param ascii 字符对应的ASCII连接的字符串
     * @return String 拼音,首先判断是否是中文如果是英文直接返回字符，如果是中文返回拼音, 否则到SpellMap中查找,如果没有找到拼音,则返回null,如果找到则返回拼音.
     */
    public static String getSpellByAscii(String ascii) {
        if (ascii.indexOf("-") > -1) {
            return SingleCnSpellDic.spellMap().get(ascii);
        }
        else {
            return ascii;
        }
    }

    /**
     * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换
     * 
     * @param cnStr String字符串
     * @return String 转换成全拼后的字符串
     */
    public static String getFullSpell(String cnStr) {
        if (null == cnStr || "".equals(cnStr.trim())) {
            return cnStr;
        }
        char[] chars = cnStr.toCharArray();
        String[] result = null;
        for (int i = 0, Len = chars.length; i < Len; i++) {
            String ascii = getCnAscii(chars[i]);
            if (ascii.length() == 0) { // 取ascii时出错
                result = CnToSpellHelper.appendToArray(chars[i], result);
            }
            else {
                String spell = getSpellByAscii(ascii);
                if (StringUtils.isEmpty(spell)) {
                    result = CnToSpellHelper.appendToArray(chars[i], result);
                }
                else {
                    result = CnToSpellHelper.interbreed(result, spell.split(","));
                } // end of if spell == null
            } // end of if ascii <= -20400
        } // end of for
        return CnToSpellHelper.toString(result);
    }

    /**
     * 获取汉语字符串的声母组合，每个汉字取拼音的第一个字符组成的一个字符串
     * 
     * @param cnStr 汉字的字符串
     * @return 每个汉字拼音的第一个字母所组成的汉字
     */
    public static String getFirstSpell(String cnStr) {
        if (null == cnStr || "".equals(cnStr.trim())) {
            return cnStr;
        }

        char[] chars = cnStr.toCharArray();
        String[] result = null;
        for (int i = 0, Len = chars.length; i < Len; i++) {
            String ascii = getCnAscii(chars[i]);
            if (ascii.length() == 1) { // 取ascii时出错
                result = CnToSpellHelper.appendToArray(chars[i], result);
            }
            else {
                String tmp = getSpellByAscii(ascii);
                if (StringUtils.isEmpty(tmp)) {
                    result = CnToSpellHelper.appendToArray(chars[i], result);
                }
                else {
                    String[] spells = tmp.split(",");
                    for (int j = 0; j < spells.length; j++) {
                        String s = StringUtils.trimToEmpty(spells[j]);
                        if (StringUtils.isNotBlank(s)) {
                            spells[j] = s.substring(0, 1);
                        }
                    }
                    result = CnToSpellHelper.interbreed(result, spells);
                } // end of if spell == null
            } // end of if ascii <= -20400
        } // end of for
        return CnToSpellHelper.toString(result);
    }

    public static void main(String[] args) {
        String str = null;
        str = "合";
        System.out.println("Spell=" + SingleCnToSpell.getFullSpell(str));
        System.err.println(getCnAscii(str.charAt(0)));
    }
}
