/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.focustech.common.codec.digester;

import junit.framework.Assert;

import org.junit.Test;

import com.tfc.evolve.common.codec.digester.DigesterUtils;
import com.tfc.evolve.common.codec.digester.Digester.DIGESTER_TYPE;

/**
 * DigesterUtilsTest.java
 * 
 * @author taofucheng
 */
public class DigesterUtilsTest {

    @Test
    public void testDigest() {
        String digest = DigesterUtils.digest("adfs,add,1234.ad", DIGESTER_TYPE.MD5);
        Assert.assertNotNull(digest);
        digest = DigesterUtils.digest("adfs,add,1234.ad", DIGESTER_TYPE.SHA256);
        Assert.assertNotNull(digest);

        byte[] digestb = DigesterUtils.digest("adfs,add,1234.ad".getBytes(), DIGESTER_TYPE.MD5);
        Assert.assertNotNull(digestb);
        digestb = DigesterUtils.digest("adfs,add,1234.ad".getBytes(), DIGESTER_TYPE.SHA256);
        Assert.assertNotNull(digestb);
    }

    @Test
    public void testMatches() {
        String digest = DigesterUtils.digest(new String("adfs,add,1234.ad"), DIGESTER_TYPE.MD5);
        Assert.assertTrue(DigesterUtils.matches("adfs,add,1234.ad", digest.trim(), DIGESTER_TYPE.MD5));
        digest = DigesterUtils.digest(new String("adfs,add,1234.ad"), DIGESTER_TYPE.SHA256);
        Assert.assertTrue(DigesterUtils.matches("adfs,add,1234.ad", digest.trim(), DIGESTER_TYPE.SHA256));

        byte[] digestb = DigesterUtils.digest("adfs,add,1234.ad".getBytes(), DIGESTER_TYPE.MD5);
        Assert.assertTrue(DigesterUtils.matches("adfs,add,1234.ad".getBytes(), digestb, DIGESTER_TYPE.MD5));
        digestb = DigesterUtils.digest("adfs,add,1234.ad".getBytes(), DIGESTER_TYPE.SHA256);
        Assert.assertTrue(DigesterUtils.matches("adfs,add,1234.ad".getBytes(), digestb, DIGESTER_TYPE.SHA256));
    }

}
