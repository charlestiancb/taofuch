/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.remote.xmlrpc.server;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.common.TypeConverterFactory;
import org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.util.ReflectionUtil;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * XmlRpcServletServerFactoryBean.java
 * 
 * @author taofucheng
 */
@SuppressWarnings("rawtypes")
public class XmlRpcServletServerFactoryBean extends ApplicationObjectSupport implements FactoryBean, InitializingBean {
	private XmlRpcServletServer server;
	private Map<String, String> serverProperties;
	private boolean detectServersInAncestorContexts = false;
	private AbstractReflectiveHandlerMapping.AuthenticationHandler authenticationHandler;
	private RequestProcessorFactoryFactory requestProcessorFactoryFactory;
	private TypeConverterFactory typeConverterFactory;
	private CommonPropertyHandlerMapping handlerMapping;
	protected Log log = LogFactory.getLog(XmlRpcServletServerFactoryBean.class);

	public Object getObject() throws Exception {
		return server;
	}

	public Class<?> getObjectType() {
		return XmlRpcServletServer.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		server = new XmlRpcServletServer();
		initServerProperties();
		server.setHandlerMapping(newXmlRpcHandlerMapping());
	}

	protected void initServerProperties() {
		if (null != serverProperties) {
			Set<String> keys = serverProperties.keySet();
			for (String key : keys) {
				String value = serverProperties.get(key);
				try {
					if (!ReflectionUtil.setProperty(this, key, value)
							&& !ReflectionUtil.setProperty(server, key, value)
							&& !ReflectionUtil.setProperty(server.getConfig(), key, value)) {
						throw new BeanInitializationException("key:" + key + ";value:" + value + " is wrong property!");
					}
				} catch (IllegalAccessException e) {
					log.error(e);
					throw new BeanInitializationException("key:" + key + ";value:" + value + " is wrong property!");
				} catch (InvocationTargetException e) {
					log.error(e);
					throw new BeanInitializationException("key:" + key + ";value:" + value + " is wrong property!");
				}
			}
		}
	}

	protected XmlRpcHandlerMapping newXmlRpcHandlerMapping() throws XmlRpcException {
		CommonPropertyHandlerMapping mapping = null;
		if (getHandlerMapping() != null) {
			mapping = handlerMapping;
		} else {
			mapping = new SpringPropertyHandlerMapping();
		}
		mapping.setAuthenticationHandler(authenticationHandler);
		if (requestProcessorFactoryFactory != null) {
			mapping.setRequestProcessorFactoryFactory(requestProcessorFactoryFactory);
		}
		if (typeConverterFactory != null) {
			mapping.setTypeConverterFactory(typeConverterFactory);
		} else {
			mapping.setTypeConverterFactory(server.getTypeConverterFactory());
		}
		mapping.setVoidMethodEnabled(server.getConfig().isEnabledForExtensions());
		mapping.addHandler(detectServersInAncestorContexts, getApplicationContext());
		return mapping;
	}

	public Map<String, String> getServerProperties() {
		return serverProperties;
	}

	public void setServerProperties(Map<String, String> serverProperties) {
		this.serverProperties = serverProperties;
	}

	public AbstractReflectiveHandlerMapping.AuthenticationHandler getAuthenticationHandler() {
		return authenticationHandler;
	}

	public void setAuthenticationHandler(AbstractReflectiveHandlerMapping.AuthenticationHandler authenticationHandler) {
		this.authenticationHandler = authenticationHandler;
	}

	public RequestProcessorFactoryFactory getRequestProcessorFactoryFactory() {
		return requestProcessorFactoryFactory;
	}

	public void setRequestProcessorFactoryFactory(RequestProcessorFactoryFactory requestProcessorFactoryFactory) {
		this.requestProcessorFactoryFactory = requestProcessorFactoryFactory;
	}

	public TypeConverterFactory getTypeConverterFactory() {
		return typeConverterFactory;
	}

	public void setTypeConverterFactory(TypeConverterFactory typeConverterFactory) {
		this.typeConverterFactory = typeConverterFactory;
	}

	public boolean isDetectServersInAncestorContexts() {
		return detectServersInAncestorContexts;
	}

	public void setDetectServersInAncestorContexts(boolean detectServersInAncestorContexts) {
		this.detectServersInAncestorContexts = detectServersInAncestorContexts;
	}

	public CommonPropertyHandlerMapping getHandlerMapping() {
		return handlerMapping;
	}

	public void setHandlerMapping(CommonPropertyHandlerMapping handlerMapping) {
		this.handlerMapping = handlerMapping;
	}
}
