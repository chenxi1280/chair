package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmUser;

import lombok.Data;

@Data
public class EcmUserVo extends EcmUser {
	private String confirmCode;
	
	private String confirmPwd;
	
	private String phoneConfirmCode;
	
	private String token;
	
	private String emailType;//fogetPwd忘记密码 verification邮箱验证
}
