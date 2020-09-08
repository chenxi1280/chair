package com.mpic.evolution.chair.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mpic.evolution.chair.pojo.entity.EcmUser;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EcmUserVo extends EcmUser {

	private String confirmCode;//图片验证码
	
	private String confirmPwd;//确认密码
	
	private String phoneConfirmCode;//手机验证码
	
	private String token;//用户的身份识别符
	
	private String imageCodeKey;//图片验证码key
	
	private String openid;//用户打开微信小程序时的唯一身份标识

	private int userFlow;
	/**
	 * 总流量
	 */
	private Integer totalFlow;

}
