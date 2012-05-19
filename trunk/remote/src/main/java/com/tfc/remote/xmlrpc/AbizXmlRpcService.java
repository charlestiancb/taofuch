/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.remote.xmlrpc;

/**
 * <pre>
 * xmlrpc服务请求组件接口
 * </pre>
 * 
 * @author taofucheng
 */
public interface AbizXmlRpcService {

    Object m_lock = new Object();

    void init();

    /**
     * 调用xmlrpc服务器对应服务方法
     * 
     * @param method 服务器应用对应方法
     * @param params 方法参数
     * @return 返回服务器方法返回信息
     */
    Object call(String pMethod, Object[] pParams) throws Exception;

}
