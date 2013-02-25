package com.tfc.word.auto.collect.repository.convertor;

public class StringHelper {
	public static String unqualify(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf(".");
		return (loc < 0) ? qualifiedName : qualifiedName.substring(loc + 1);
	}

	public static String qualifier(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf(".");
		return (loc < 0) ? "" : qualifiedName.substring(0, loc);
	}

	public static boolean isNotEmpty(String string) {
		return string != null && string.length() > 0;
	}
}
