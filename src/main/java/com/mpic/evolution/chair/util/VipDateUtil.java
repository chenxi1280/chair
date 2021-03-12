package com.mpic.evolution.chair.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.mpic.evolution.chair.common.constant.CommonField.DOUBLE_HALF;

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
		//若 different为0说明充值会员的时间和当前时间是同一个月
		if (difference == 0) {
			return sDate.plusMonths(1);
		}
		//若 different不为0说明充值会员的时间和当前时间不是同一个月 所以累加相差得月份找到当前月得到期时间
		return sDate.plusMonths(difference);
	}

	/**
	 * LocalDateTime 转数据库dateTime类型
	 * @param param
	 */
	public static Date formatToDate(LocalDateTime param){
		//date 转 LocalDateTime方法
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = param.atZone(zoneId);
		Date date = Date.from(zdt.toInstant());
		return date;
	}

	/**
	 *  数据库dateTime类型转LocalDateTime
	 * @param param
	 */
	public static LocalDateTime formatToLocalDateTime(Date param){
		//date 转 LocalDateTime方法
		Instant instant = param.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
		return localDateTime;
	}

	public static void main(String[] args) {
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.of(2020, 10, 28, 9, 24, 0);
		//test date 与 LocalDateTime之间的切换
		/*Date date = new Date();
		LocalDateTime now = LocalDateTime.now();
		Date date1 = VipDateUtil.formatToDate(now);
		System.out.println(date1);
		LocalDateTime localDateTime1 = VipDateUtil.formatToLocalDateTime(date);
		System.out.println(localDateTime1);*/
		LocalDateTime endDate = VipDateUtil.getEndDate(VipDateUtil.formatToDate(localDateTime));
		System.out.println(endDate);
//		String date = VipDateUtil.getEndDateByMonth(localDateTime);
//		String date1 = VipDateUtil.getEndDateBySeason(localDateTime);
//		String date2 = VipDateUtil.getEndDateByYear(localDateTime);
		if (-1 <= -DOUBLE_HALF) {
			System.out.println(true);
		}
	}

}
