package com.mpic.evolution.chair.pojo.vo;

import lombok.Data;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-28 9:59:43 
* 获取微信登录用户openid的基本信息
*/
@Data
public class WxLoginVo {
	
	private final String appid = "wxa001a9842ad0f851";
	
	private final String secret = "335ecf3ab57b04828d9003bf144f1369";
	
	private String code;
}
