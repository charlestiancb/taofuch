package com.cloudtech.site.popocapp.utils;

import java.util.ArrayList;
import java.util.List;

public class StackUtils {
	private static ThreadLocal<List<Object>> elements = new ThreadLocal<List<Object>>();

	private StackUtils() {
	}

	public static void pushAsHtml(String msg) {
		push("<p>" + msg + "</p>");
	}

	public static void push(Object obj) {
		List<Object> objs = (List<Object>) elements.get();
		if (objs == null) {
			objs = new ArrayList<Object>();
		}
		objs.add(obj);
		elements.set(objs);
	}

	public static List<Object> getFullStack() {
		List<Object> result = elements.get();
		if (result == null) {
			result = new ArrayList<Object>();
		}
		return result;
	}
}
