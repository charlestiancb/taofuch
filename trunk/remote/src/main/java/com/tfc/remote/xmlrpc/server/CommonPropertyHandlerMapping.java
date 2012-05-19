package com.tfc.remote.xmlrpc.server;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.springframework.context.ApplicationContext;

public abstract class CommonPropertyHandlerMapping extends PropertyHandlerMapping {
	/**
	 * 添加处理方式。
	 * 
	 * @param detect
	 *            是否检查
	 * @param context
	 *            环境上下文
	 * @throws XmlRpcException
	 */
	public abstract void addHandler(boolean detect, ApplicationContext context) throws XmlRpcException;
}
