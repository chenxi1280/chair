package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
/**
 * @author SJ
 */
public interface SendSMSService {
	/**
	 * @author SJ
	 *	 发送短信验证码
	 */
	ResponseDTO sendSMS();
}
