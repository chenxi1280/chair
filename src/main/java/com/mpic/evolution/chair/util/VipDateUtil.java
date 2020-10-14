package com.mpic.evolution.chair.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-10-12 17:04:57 
*/
public class VipDateUtil {
	/**
	 * 	按月获取截止日期
	 * @param time
	 * @return
	 */
	public static String getEndDateByMonth(LocalDateTime time){
		LocalDateTime plusMonths = time.plusMonths(1);
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		return pattern.format(plusMonths);
	}
	
	/**
	 * 	按季度获取截止日期
	 * @param time
	 * @return
	 */
	public static String getEndDateBySeason(LocalDateTime time){
		LocalDateTime plusMonths = time.plusMonths(3);
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		return pattern.format(plusMonths);
	}
	
	/**
	 * 	按年获取截止日期
	 * @param time
	 * @return
	 */
	public static String getEndDateByYear(LocalDateTime time){
		LocalDateTime plusMonths = time.plusYears(1);
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		return pattern.format(plusMonths);
	}
	
	public static void main(String[] args) {
		LocalDateTime localDateTime = LocalDateTime.of(2020, 10, 31, 9, 24, 0);
		String date = VipDateUtil.getEndDateByMonth(localDateTime);
		String date1 = VipDateUtil.getEndDateBySeason(localDateTime);
		String date2 = VipDateUtil.getEndDateByYear(localDateTime);
		System.out.println(date);
		System.out.println(date1);
		System.out.println(date2);
	}

}
