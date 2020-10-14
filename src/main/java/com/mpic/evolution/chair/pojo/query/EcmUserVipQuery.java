package com.mpic.evolution.chair.pojo.query;

import com.mpic.evolution.chair.pojo.entity.EcmUserVip;

import lombok.Data;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-10-13 13:02:27 
*/

@Data
public class EcmUserVipQuery extends EcmUserVip{
	/**
	 * 	当前时间
	 */
	private String currentDateTime;
	
	private static final long serialVersionUID = 1L;
}
