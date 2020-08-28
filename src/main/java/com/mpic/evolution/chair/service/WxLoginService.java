package com.mpic.evolution.chair.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-27 9:49:00 
*/
public interface WxLoginService {
	/**
	 * 
	 * @author SJ
	 *	 获取验证码
	 */ 
	DefaultKaptcha getConfirmCode();

	boolean savaUser(EcmUser user, EcmUserVo ecmUserVo);

}
