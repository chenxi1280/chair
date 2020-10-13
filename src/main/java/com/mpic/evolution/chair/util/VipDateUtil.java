package com.mpic.evolution.chair.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-10-12 17:04:57 
*/
public class VipDateUtil {
	
	public static LocalDateTime getEndDate(String time){
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		LocalDateTime parse = LocalDateTime.parse(time,pattern);
		LocalDateTime plusMonths = parse.plusMonths(1);
		System.out.println(plusMonths);
		return plusMonths;
	}
	
	public static void main(String[] args) {
		LocalDateTime date = VipDateUtil.getEndDate("2020-10-12 10:45:00");
		System.out.println(date);
	}

}
