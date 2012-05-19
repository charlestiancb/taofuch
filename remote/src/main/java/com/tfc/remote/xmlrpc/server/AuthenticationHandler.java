/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.remote.xmlrpc.server;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * AuthenticationHandler.java
 * 
 * @author taofucheng
 */
public class AuthenticationHandler implements
		org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping.AuthenticationHandler {
	private String userName;
	private String password;

	public boolean isAuthorized(XmlRpcRequest pRequest) throws XmlRpcException {
		XmlRpcClientConfigImpl config = (XmlRpcClientConfigImpl) pRequest.getConfig();
		return getUserName().equals(config.getBasicUserName()) && getPassword().equals(config.getBasicPassword());
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
