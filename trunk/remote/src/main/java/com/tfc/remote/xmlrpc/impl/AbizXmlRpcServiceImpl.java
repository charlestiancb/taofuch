/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.remote.xmlrpc.impl;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import com.tfc.remote.xmlrpc.AbizXmlRpcService;

/**
 * 通过xmlrpc调用服务器方法类，此类线程不安全，在多线程情况下请不要使用singleton实例
 * 
 * @author taofucheng
 */
public class AbizXmlRpcServiceImpl implements AbizXmlRpcService {

    private XmlRpcClient rpclient;
    private int maxTry = 3;
    private int timeout = 30000;
    private String url = "http://localhost/xmlrpc";
    private String encoding = "UTF-8";
    private Map<String, String> cfgProps = new HashMap<String, String>();

    /** 初始化xmlrpc对象 */
    public void init() {
        if (null == rpclient) {
            synchronized (m_lock) {
                    try {
                        XmlRpcClientConfigImpl cfg = new XmlRpcClientConfigImpl();
                        cfg.setConnectionTimeout(getTimeout());// 30 sec
                        cfg.setEnabledForExtensions(true);// 允许使用XML-RPC扩展APACHE协议
                        cfg.setContentLengthOptional(false);// 可以不传送ContentLength
                        cfg.setEncoding(getEncoding());
                        cfg.setServerURL(new URL(getUrl()));
                    if (cfgProps != null && !cfgProps.isEmpty()) {
                            for (String key : cfgProps.keySet()) {
                                try {
                                    PropertyUtils.setProperty(cfg, key, cfgProps.get(key));
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        rpclient = new XmlRpcClient();
                        rpclient.setConfig(cfg);
                        rpclient.setTransportFactory(new XmlRpcCommonsTransportFactory(rpclient));
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    /**
     * 调用xmlrpc服务器对应服务方法
     * 
     * @param method 服务器应用对应方法
     * @param params 方法参数
     * @return 返回服务器方法返回信息
     */
    public Object call(String method, Object[] params) throws Exception {
        Object mOutput = null;
            boolean run = false;
            Throwable runE = null;
            for (int i = 0; i < maxTry; i++) {
                try {
                    mOutput = rpclient.execute(method, params);
                    run = true;
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    runE = e;
                }
                Thread.sleep(1000);
            }
        if (!run) {
                throw new Exception("重复请求“" + maxTry + "”次后没有成功。", runE);
            }
        return mOutput;
    }

    // --------------------------------------------------------------------------------------------------------------
    public int getMaxTry() {
        return maxTry;
    }

    public void setMaxTry(int maxTry) {
        this.maxTry = maxTry;
    }

    public XmlRpcClient getRpclient() {
        return rpclient;
    }

    public void setRpclient(XmlRpcClient rpclient) {
        this.rpclient = rpclient;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Map<String, String> getCfgProps() {
        return cfgProps;
    }

    public void setCfgProps(Map<String, String> cfgProps) {
        this.cfgProps = cfgProps;
    }
}
