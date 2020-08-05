package com.mpic.evolution.chair.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;

/**
 * 
 * @author SJ
 */ 

public interface LoginService {
	/**
     * 	（微信网站PC扫码）
     *	@author SJ
     */
	ResponseDTO loginByWeiXin(String code);
	
	/**
	 * 
	 * @author SJ
	 *	 获取验证码
	 */ 
	DefaultKaptcha getConfirmCode();
}
