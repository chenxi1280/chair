package com.mpic.evolution.chair.pojo.dto;

import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;
import com.mpic.evolution.chair.pojo.entity.EcmTemplate;

import lombok.Data;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-20 9:55:57 
*/
@Data
public class EcmInnerMessageDto extends EcmInnerMessage {
	
	/**
     * 	模板内容
     */
    private EcmTemplate ecmTemplate;
}
