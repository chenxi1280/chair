package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmUser;

import lombok.Data;

@Data
public class EcmUserVo extends EcmUser {
	private String confirmCode;//图片验证码
	
	private String confirmPwd;//确认密码
	
	private String phoneConfirmCode;//手机验证码
	
	private String token;//用户的身份识别符
	
	private String emailType;//邮箱验证类型 fogetPwd忘记密码 verification邮箱验证
}
