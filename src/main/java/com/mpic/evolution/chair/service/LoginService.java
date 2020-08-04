package com.mpic.evolution.chair.service;

/**
 * 
 * @author SJ
 */ 

public interface LoginService {
	/**
	 * 
	 * @author SJ
	 *	 判断用户是否存在
	 */ 
	boolean isExsitWxUser(String openId,String unionId);
}
