package com.tfc.webtest.content.function;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.tfc.webtest.content.FunctionContent;
import com.tfc.webtest.utils.ClassUtils;

public class FunctionFactory {
	private static Map<String, FunctionContent> funcs;

	public static FunctionContent findFunc(String funcName) {
		if (funcs == null) {
			funcs = new HashMap<String, FunctionContent>();
			scanDefault();
		}
		if (StringUtils.isEmpty(funcName)) {
			return null;
		}
		return funcs.get(funcName.toUpperCase());
	}

	/**
	 * 将支持的所有的默认执行器都扫描进来
	 */
	private static void scanDefault() {
		Set<Class<?>> cls = ClassUtils.getClasses(FunctionContent.class.getPackage());
		if (cls != null && cls.size() > 0) {
			for (Class<?> c : cls) {
				try {
					if (FunctionContent.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
						FunctionContent f = (FunctionContent) c.newInstance();
						funcs.put(f.getFuncName().toUpperCase(), f);
					}
				} catch (Exception e) {
					System.err.println("加载操作[" + c.getName() + "]失败~");
				}
			}
		}
	}
}
