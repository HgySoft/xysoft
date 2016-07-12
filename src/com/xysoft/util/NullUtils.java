package com.xysoft.util;

public class NullUtils {
	/**
	 * 判断是否为空.
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} return false;
	}
	
	public static boolean isNotEmpty(String str) {
		if (str != null && !"".equals(str)) {
			return true;
		} return false;
	}
}
