package com.mpic.evolution.chair.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
	
	/**
	 * 获取会员截止时间
	 * @param startDate endDate
	 */
	public static LocalDateTime getEndDate(Date startDate){
		//date 转 LocalDateTime方法
		LocalDateTime now = LocalDateTime.now();
		Instant instant = startDate.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime sDate = instant.atZone(zoneId).toLocalDateTime();
		int startMonth = sDate.getMonthValue();
		int currentMonth = now.getMonthValue();
		int difference = currentMonth - startMonth;
		if (difference == 0) {
			return sDate.plusMonths(1);
		}
		return sDate.plusMonths(difference);
	}
	
	public static void main(String[] args) {
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.of(2020, 10, 28, 9, 24, 0);
//		String date = VipDateUtil.getEndDateByMonth(localDateTime);
//		String date1 = VipDateUtil.getEndDateBySeason(localDateTime);
//		String date2 = VipDateUtil.getEndDateByYear(localDateTime);
		//LocalDateTime 转 date的方法
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		LocalDateTime endDate = VipDateUtil.getEndDate(Date.from(zdt.toInstant()));
		String eString = endDate.format(pattern);
		System.out.println(eString);
//		System.out.println(date);
//		System.out.println(date1);
//		System.out.println(date2);
		if (-1 <= -0.5) {
			System.out.println(true);
		}
	}

}
