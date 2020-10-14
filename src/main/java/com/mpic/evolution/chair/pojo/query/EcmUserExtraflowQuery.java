package com.mpic.evolution.chair.pojo.query;

import com.mpic.evolution.chair.pojo.entity.EcmUserExtraflow;

import lombok.Data;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-10-13 14:01:32 
*/

@Data
public class EcmUserExtraflowQuery extends EcmUserExtraflow {
	/**
	 * 	当前时间点
	 */
	private String currentDateTime;
	
	private static final long serialVersionUID = 1L;

}
