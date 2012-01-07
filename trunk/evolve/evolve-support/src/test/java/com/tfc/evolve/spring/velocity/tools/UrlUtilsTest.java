package com.tfc.evolve.spring.velocity.tools;
/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */


import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * UrlUtilsTest.java
 * 
 * @author taofucheng
 */
public class UrlUtilsTest {
    private IMocksControl control = EasyMock.createControl();
    private HttpServletRequest request = null;

    @Before
    public void setUp() {
        request = control.createMock(HttpServletRequest.class);
    }

    @Test
    public void testGetRealIp() {
        // 只有x-forwarded-for值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for"))
                .andReturn("192.168.0.5,172.16.12.25,10.221.0.2,221.45.0.110,192.168.0.5").times(1);
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp(request), "221.45.0.110");
        control.verify();
        // 只有Proxy-Client-IP值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("Proxy-Client-IP")).andReturn("221.45.0.110").times(1);
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp(request), "221.45.0.110");
        control.verify();
        // 只有WL-Proxy-Client-IP值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("Proxy-Client-IP")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("WL-Proxy-Client-IP")).andReturn("221.45.0.110").times(1);
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp(request), "221.45.0.110");
        control.verify();
        // 只有remoteAddr值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("Proxy-Client-IP")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("WL-Proxy-Client-IP")).andReturn(null).times(1);
        EasyMock.expect(request.getRemoteAddr()).andReturn("221.45.0.110").times(1);
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp(request), "221.45.0.110");
        control.verify();
        // 没有任何值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("Proxy-Client-IP")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("WL-Proxy-Client-IP")).andReturn(null).times(1);
        EasyMock.expect(request.getRemoteAddr()).andReturn(null).times(1);
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp(request), null);
        control.verify();
        // 拥有所有值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for"))
                .andReturn("192.168.0.5,172.16.12.25,10.221.0.2,221.45.0.110,192.168.0.5").times(1);
        EasyMock.expect(request.getHeader("Proxy-Client-IP")).andReturn("221.45.0.111").anyTimes();
        EasyMock.expect(request.getHeader("WL-Proxy-Client-IP")).andReturn("221.45.0.112").anyTimes();
        EasyMock.expect(request.getRemoteAddr()).andReturn("221.45.0.113").anyTimes();
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp(request), "221.45.0.110");
        control.verify();
    }

    @Test
    public void testGetRealIp4SSO() {
        // 只有x-forwarded-for值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for"))
                .andReturn("192.168.0.5,172.16.12.25,10.221.0.2,221.45.0.110,192.168.0.5").times(1);
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp4SSO(request), "192.168.0.5,172.16.12.25,10.221.0.2,221.45.0.110");
        control.verify();
        // 只有Proxy-Client-IP值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("Proxy-Client-IP")).andReturn("221.45.0.110").times(1);
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp4SSO(request), "221.45.0.110");
        control.verify();
        // 只有WL-Proxy-Client-IP值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("Proxy-Client-IP")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("WL-Proxy-Client-IP")).andReturn("221.45.0.110").times(1);
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp4SSO(request), "221.45.0.110");
        control.verify();
        // 只有remoteAddr值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("Proxy-Client-IP")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("WL-Proxy-Client-IP")).andReturn(null).times(1);
        EasyMock.expect(request.getRemoteAddr()).andReturn("221.45.0.110").times(1);
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp4SSO(request), "221.45.0.110");
        control.verify();
        // 没有任何值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("Proxy-Client-IP")).andReturn(null).times(1);
        EasyMock.expect(request.getHeader("WL-Proxy-Client-IP")).andReturn(null).times(1);
        EasyMock.expect(request.getRemoteAddr()).andReturn(null).times(1);
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp4SSO(request), null);
        control.verify();
        // 拥有所有值
        control.reset();
        EasyMock.expect(request.getHeader("x-forwarded-for"))
                .andReturn("192.168.0.5,172.16.12.25,10.221.0.2,221.45.0.110,192.168.0.5").times(1);
        EasyMock.expect(request.getHeader("Proxy-Client-IP")).andReturn("221.45.0.111").anyTimes();
        EasyMock.expect(request.getHeader("WL-Proxy-Client-IP")).andReturn("221.45.0.112").anyTimes();
        EasyMock.expect(request.getRemoteAddr()).andReturn("221.45.0.113").anyTimes();
        control.replay();
        Assert.assertEquals(UrlUtils.getRealIp4SSO(request), "192.168.0.5,172.16.12.25,10.221.0.2,221.45.0.110");
        control.verify();
    }

    @Test
    public void testIsV4IPAddress() {
        Assert.assertFalse(UrlUtils.isV4IPAddress(null));
        Assert.assertFalse(UrlUtils.isV4IPAddress(""));
        Assert.assertFalse(UrlUtils.isV4IPAddress(" "));
        Assert.assertFalse(UrlUtils.isV4IPAddress("asdf"));
        Assert.assertFalse(UrlUtils.isV4IPAddress(" asdf "));
        Assert.assertFalse(UrlUtils.isV4IPAddress("192.168.1"));
        Assert.assertFalse(UrlUtils.isV4IPAddress("256.111.222.55"));
        Assert.assertFalse(UrlUtils.isV4IPAddress("221.22.33.111.22.44"));
        Assert.assertTrue(UrlUtils.isV4IPAddress("192.168.1.1"));
        Assert.assertTrue(UrlUtils.isV4IPAddress("221.45.0.110"));
    }

    @Test
    public void testGetRequestUrlByRequest() {
    }

    @Test
    public void testFillProtocol() {
        Assert.assertNull(UrlUtils.fillProtocol(null));
        Assert.assertEquals("", UrlUtils.fillProtocol(""));
        Assert.assertEquals("", UrlUtils.fillProtocol(" "));
        Assert.assertEquals("http://abiz.com", UrlUtils.fillProtocol("abiz.com"));
        Assert.assertEquals("http://abiz.com", UrlUtils.fillProtocol(" abiz.com "));
        Assert.assertEquals("http://abiz.com", UrlUtils.fillProtocol("http://abiz.com"));

    }

    @Test
    public void testEscape() {
        Assert.assertEquals("a%26b%22c%3Bd%25e%3Cf%3Eg%E5%AD%97", UrlUtils.escape("a&b\"c;d%e<f>g字"));
    }
}
