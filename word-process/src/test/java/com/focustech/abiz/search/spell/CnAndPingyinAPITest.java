/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.abiz.search.spell;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.focustech.abiz.search.BaseTest;
import com.tfc.word.spell.PinyinAPI;

/**
 * CnAndPingyinAPITest.java
 * 
 * @author taofucheng
 */
public class CnAndPingyinAPITest extends BaseTest {

	@Test
	public void testToPingyin() {
		Assert.assertTrue(contains("tongzhi", PinyinAPI.toPinyin("同志")));
		Assert.assertTrue(contains("yueqi", PinyinAPI.toPinyin("乐器")));
		System.err.println(Arrays.toString(PinyinAPI.toPinyin("明天和我说观后感")));
	}

	@Test
	public void testToFirstLetter() {
		System.out.println(Arrays.toString(PinyinAPI.toFirstLetter("明天和我说观后感")));
		System.out.println(Arrays.toString(PinyinAPI.toFirstLetter("一了百了")));
		System.out.println(Arrays.toString(PinyinAPI.toFirstLetter("乐器")));
		System.out.println(Arrays.toString(PinyinAPI.toFirstLetter("我们一起一唱百和")));
	}

	@Test
	public void testToCn() {
		Assert.assertTrue(contains("通知字", PinyinAPI.toCn("TongZhi字")));
		Assert.assertTrue(contains("同志字", PinyinAPI.toCn("TongZhi字")));
		Assert.assertTrue(contains("统治字", PinyinAPI.toCn("TongZhi字")));
		System.out.println(Arrays.toString(PinyinAPI.toCn("yiLiaoBaiLiao")));
	}

	@Test
	public void testGetSamePinyinWords() {
		Assert.assertTrue(contains("钢铁ya", PinyinAPI.getSamePinyinWords("港铁ya")));
		Assert.assertTrue(contains("一唱百和", PinyinAPI.getSamePinyinWords("一chang百和")));
		// Assert.assertTrue(contains("统治字",
		// CnAndPingyinAPI.getSamePinyinWords("TongZhi字")));
		// Assert.assertTrue(contains("统治字",
		// CnAndPingyinAPI.getSamePinyinWords("TongZhi字")));
		// System.err.println(Arrays.toString(CnAndPingyinAPI.getSamePinyinWords("一chang百和")));
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(PinyinAPI.toPinyinSplit(">起重de设备111")));
	}
}
