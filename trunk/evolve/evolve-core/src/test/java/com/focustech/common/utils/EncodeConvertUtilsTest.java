/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import junit.framework.Assert;

import org.junit.Test;

import com.tfc.evolve.common.utils.EncodeConvertUtils;

/**
 * EncodeConvertUtilsTest.java
 * 
 * @author taofucheng
 */
public class EncodeConvertUtilsTest {

    @Test
    public void testUtf8ToGbk() throws UnsupportedEncodingException {
        Assert.assertEquals("", EncodeConvertUtils.utf8ToGbk(""));
        Assert.assertEquals(null, EncodeConvertUtils.utf8ToGbk(null));
        Assert.assertEquals("中文", EncodeConvertUtils.utf8ToGbk("中文"));
        Assert.assertEquals("中and文", EncodeConvertUtils.utf8ToGbk("中and文"));
        Assert.assertEquals("여보和당신", EncodeConvertUtils.utf8ToGbk("여보和당신"));
        Assert.assertEquals("あう", EncodeConvertUtils.utf8ToGbk("あう"));
        System.err.println(URLEncoder.encode("中文", "GBK"));
    }

}
