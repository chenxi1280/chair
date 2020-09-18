package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmInviteCode;

import lombok.Data;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-18 12:46:31 
*/

@Data
public class EcmInviteCodeVo extends EcmInviteCode {
	
	//图片验证码
	private String confirmCode;
	
	//图片验证码key
	private String imageCodeKey;
	
	private static final long serialVersionUID = 1L;

}
