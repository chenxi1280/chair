package com.mpic.evolution.chair.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;

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
	
	/**
	 * @author SJ
	 *	 发送短信验证码
	 * @throws TencentCloudSDKException 
	 */
	SendStatus sendSMS(String code, String[] phoneNumbers) throws TencentCloudSDKException;
}
