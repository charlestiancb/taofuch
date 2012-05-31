package com.cloudtech.ebusi.service.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.cloudtech.ebusi.dao.ResourceDao;

//1 加载资源与权限的对应关系  
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	private ResourceDao resourceDao;
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	public ResourceDao getresourceDao() {
		return resourceDao;
	}

	public void setresourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		if (resourceMap == null) {
			loadResourceDefine();
		}
		return resourceMap.isEmpty() ? null : resourceMap.values().iterator().next();
	}

	public boolean supports(Class<?> clazz) {
		// 都支持！
		return true;
	}

	// 加载所有资源与权限的关系
	private void loadResourceDefine() {
		if (resourceMap == null) {
			resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
			Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
			// 以权限名封装为Spring的security Object
			ConfigAttribute configAttribute = new SecurityConfig("admin/auth");
			configAttributes.add(configAttribute);
			resourceMap.put("^/.*", configAttributes);
		}
	}

	// 返回所请求资源所需要的权限
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		System.out.println("requestUrl is " + requestUrl);
		if (resourceMap == null) {
			loadResourceDefine();
		}
		if (resourceMap != null && !resourceMap.isEmpty()) {
			for (String url : resourceMap.keySet()) {
				if (Pattern.matches(url, requestUrl)) {
					return resourceMap.get(url);
				}
			}
		}
		return null;
	}
}