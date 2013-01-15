/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.abiz.search.spell;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.focustech.abiz.search.BaseTest;
import com.tfc.word.spell.CnAndPingyinAPI;

/**
 * CnAndPingyinAPITest.java
 * 
 * @author taofucheng
 */
public class CnAndPingyinAPITest extends BaseTest {

    @Test
    public void testToPingyin() {
        Assert.assertTrue(contains("tongzhi", CnAndPingyinAPI.toPingyin("同志")));
        Assert.assertTrue(contains("yueqi", CnAndPingyinAPI.toPingyin("乐器")));
        System.err.println(Arrays.toString(CnAndPingyinAPI.toPingyin("明天和我说观后感")));
    }

    @Test
    public void testToCn() {
        Assert.assertTrue(contains("通知字", CnAndPingyinAPI.toCn("TongZhi字")));
        Assert.assertTrue(contains("同志字", CnAndPingyinAPI.toCn("TongZhi字")));
        Assert.assertTrue(contains("统治字", CnAndPingyinAPI.toCn("TongZhi字")));
    }

    @Test
    public void testGetSamePinyinWords() {
        Assert.assertTrue(contains("钢铁ya", CnAndPingyinAPI.getSamePinyinWords("港铁ya")));
        Assert.assertTrue(contains("一唱百和", CnAndPingyinAPI.getSamePinyinWords("一chang百和")));
        // Assert.assertTrue(contains("统治字", CnAndPingyinAPI.getSamePinyinWords("TongZhi字")));
        // Assert.assertTrue(contains("统治字", CnAndPingyinAPI.getSamePinyinWords("TongZhi字")));
        // System.err.println(Arrays.toString(CnAndPingyinAPI.getSamePinyinWords("一chang百和")));
    }
}
