/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.common.utils;

import junit.framework.Assert;

import org.junit.Test;

import com.tfc.evolve.common.utils.ClassUtils;
import com.tfc.evolve.common.utils.zip.ZipCompressor;

/**
 * ClassUtilsTest.java
 * 
 * @author taofucheng
 */
public class ClassUtilsTest {

    @Test
    public void testIsPrimitiveArray() {
        Assert.assertFalse(ClassUtils.isPrimitiveArray(1));
        Assert.assertTrue(ClassUtils.isPrimitiveArray(new int[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isPrimitiveArray(new long[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isPrimitiveArray(new char[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isPrimitiveArray(new byte[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isPrimitiveArray(new float[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isPrimitiveArray(new double[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isPrimitiveArray(new short[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isPrimitiveArray(new boolean[]{true, false, false, true}));
    }

    @Test
    public void testIsIntPrimitiveArray() {
        Assert.assertFalse(ClassUtils.isIntPrimitiveArray(1));
        Assert.assertTrue(ClassUtils.isIntPrimitiveArray(new int[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isIntPrimitiveArray(new long[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isIntPrimitiveArray(new char[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isIntPrimitiveArray(new byte[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isIntPrimitiveArray(new float[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isIntPrimitiveArray(new double[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isIntPrimitiveArray(new short[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isIntPrimitiveArray(new boolean[]{true, false, false, true}));
    }

    @Test
    public void testIsFloatPrimitiveArray() {
        Assert.assertFalse(ClassUtils.isFloatPrimitiveArray(1));
        Assert.assertFalse(ClassUtils.isFloatPrimitiveArray(new int[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isFloatPrimitiveArray(new long[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isFloatPrimitiveArray(new char[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isFloatPrimitiveArray(new byte[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isFloatPrimitiveArray(new float[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isFloatPrimitiveArray(new double[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isFloatPrimitiveArray(new short[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isFloatPrimitiveArray(new boolean[]{true, false, false, true}));
    }

    @Test
    public void testIsBooleanPrimitiveArray() {
        Assert.assertFalse(ClassUtils.isBooleanPrimitiveArray(1));
        Assert.assertFalse(ClassUtils.isBooleanPrimitiveArray(new int[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isBooleanPrimitiveArray(new long[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isBooleanPrimitiveArray(new char[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isBooleanPrimitiveArray(new byte[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isBooleanPrimitiveArray(new float[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isBooleanPrimitiveArray(new double[]{1, 2, 3}));
        Assert.assertFalse(ClassUtils.isBooleanPrimitiveArray(new short[]{1, 2, 3}));
        Assert.assertTrue(ClassUtils.isBooleanPrimitiveArray(new boolean[]{true, false, false, true}));
    }

    @Test
    public void testGetFieldByName() {
        Assert.assertNotNull(ClassUtils.getFieldByName(ZipCompressor.class, "encodeing"));
        Assert.assertNotNull(ClassUtils.getFieldByName(ZipCompressor.class, "mCompressedFile"));
        Assert.assertNull(ClassUtils.getFieldByName(ZipCompressor.class, "hehe"));
    }

}
