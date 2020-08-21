package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;

import lombok.Data;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-20 17:14:08 
*/
@Data
public class EcmInnerMessageVo extends EcmInnerMessage {
	/**
	 *	 已读数量
	 */
	private Integer readNum;
	
	private Integer unReadNum;
	
}
