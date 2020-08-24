package com.mpic.evolution.chair.pojo.vo;

import java.util.List;

import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;

import lombok.Data;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-21 17:56:08 
*/
@Data
public class EcmInnerMessageVo extends EcmInnerMessage {
	/**
	 * 	前端传回的信息id list
	 */
	private List<Integer> messageIds;
}
