/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.abiz.search.spell.convert;

import org.junit.Assert;
import org.junit.Test;

import com.focustech.abiz.search.BaseTest;
import com.tfc.word.spell.convert.CnToSpellHelper;

/**
 * CnToSpellHelperTest.java
 * 
 * @author taofucheng
 */
public class CnToSpellHelperTest extends BaseTest {

    @Test
    public void testInterbreed() {
        Assert.assertEquals(1, CnToSpellHelper.interbreed(new String[]{"he"}, new String[]{"ha"}).length);
        Assert.assertEquals(2, CnToSpellHelper.interbreed(new String[]{"he", "hei"}, new String[]{"ha"}).length);
        Assert.assertEquals(4, CnToSpellHelper.interbreed(new String[]{"ya", "ha"}, new String[]{"hei", "hen"}).length);
    }

    @Test
    public void testAppendToArray() {
        Assert.assertNull(CnToSpellHelper.appendToArray("", null));
        Assert.assertNull(CnToSpellHelper.appendToArray(null, null));
        Assert.assertEquals(1, CnToSpellHelper.appendToArray("had", null).length);
        Assert.assertEquals("had", CnToSpellHelper.appendToArray("had", null)[0]);
        Assert.assertEquals("hehad", CnToSpellHelper.appendToArray("had", new String[]{"he", "haha"})[0]);
    }

}
