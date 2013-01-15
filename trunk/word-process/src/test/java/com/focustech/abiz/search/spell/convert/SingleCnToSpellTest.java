/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.abiz.search.spell.convert;

import junit.framework.Assert;

import org.junit.Test;

import com.focustech.abiz.search.BaseTest;
import com.tfc.word.spell.convert.SingleCnToSpell;

/**
 * SingleCnToSpellTest.java
 * 
 * @author taofucheng
 */
public class SingleCnToSpellTest extends BaseTest {

    @Test
    public void testGetCnByAscii() {
        Assert.assertEquals("字", SingleCnToSpell.getCnByAscii("215-214"));
    }

    @Test
    public void testGetCnAscii() {
        Assert.assertEquals("215-214", SingleCnToSpell.getCnAscii('字'));
    }

}
