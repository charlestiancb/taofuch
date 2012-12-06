package com.tfc.webtest.content;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.tfc.webtest.utils.ClassUtils;

public class ContentFactory {
	private static List<Content> types;
	private static TextContent textContent = new TextContent();

	/**
	 * 查找对应的实例
	 * 
	 * @param content
	 * @return
	 */
	public static Content findInstance(String content) {
		if (StringUtils.isEmpty(content)) {
			return null;
		}
		if (types == null) {
			types = new ArrayList<Content>();
			scanDefault();
		}
		for (Content type : types) {
			if (type.isBelongTo(content)) {
				return type;
			}
		}
		return textContent;
	}

	/**
	 * 将支持的所有的默认执行器都扫描进来
	 */
	private static void scanDefault() {
		Set<Class<?>> cls = ClassUtils.getClasses(Content.class.getPackage());
		if (cls != null && cls.size() > 0) {
			for (Class<?> c : cls) {
				try {
					if (c == textContent.getClass()) {
						// 文本内容的不能放到队列中，因为文本是包含函数等所有的内容
						continue;
					}
					if (Content.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
						Content o = (Content) c.newInstance();
						types.add(o);
					}
				} catch (Exception e) {
					System.err.println("加载操作[" + c.getName() + "]失败~");
				}
			}
		}
	}

	public static void main(String[] args) {
		findInstance("ddd");
	}
}
