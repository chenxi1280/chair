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
	//会员的日期 超级会员日期 当月会员总 会员已使用 会员剩余流量 永久流量总 永久流量已使用 永久流量剩余
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
	/**
	 * 会员的日期
	 */
	private String vipStartDate;
	/**
	 * 会员的日期
	 */
	private String vipEndDate;
	/**
	 * 超级会员日期
	 */
	private String superVipStartDate;
	/**
	 * 超级会员日期
	 */
	private String superVipEndDate;
	/**
	 * 当月会员总流量
	 */
	private Integer currentMothVipTotalFlow;
	/**
	 * 当月会员已使用流量
	 */
	private Integer	currentMothVipUserdFlow;
	/**
	 * 当月会员已剩余流量
	 */
	private Integer	currentMothVipsurplusFlow;
	/**
	 * 总永久流量
	 */
	private Integer totalPermanentFlow;
	/**
	 * 已使用永久流量
	 */
	private Integer usedPermanentFlow;
	/**
	 * 剩余永久流量
	 */
	private Integer surplusPermanentFlow;

}
