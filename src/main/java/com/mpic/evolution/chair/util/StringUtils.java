package com.mpic.evolution.chair.util;
/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-19 10:12:12 
*/
public class StringUtils {
	
	/**
	 * 	空串或者是值为"null"的字符串
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str.length() == 0 || str.equals("null")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 	空串或者是值为"null"的字符串
	 * @param str
	 * @return
	 */
	public static boolean isNullOrBlank(String str) {
		if (str == null || str.length()==0 ) {
			return true;
		}
		return false;
	}

}
