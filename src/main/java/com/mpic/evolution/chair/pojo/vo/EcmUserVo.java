package com.mpic.evolution.chair.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mpic.evolution.chair.pojo.entity.EcmUser;

import lombok.Data;

/**
 * @author cxd
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EcmUserVo extends EcmUser {
	//图片验证码
	private String confirmCode;
	//确认密码
	private String confirmPwd;
	//手机验证码
	private String phoneConfirmCode;
	//用户的身份识别符
	private String token;
	//图片验证码key
	private String imageCodeKey;
	//用户打开微信小程序时的唯一身份标识
	private String openid;

	private int userFlow;
	
	//用户新密码
	private String newPwd;

	 /**
     * 	邀请码
     */
    private String inviteCode;
	/**
	 * 总流量
	 */
	private Integer totalFlow;

	private Boolean logoChange;

}
