package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-4 10:29:34 
*/
public interface WxPersonalCenterService {

	ResponseDTO getWxUserArtWorks(EcmArtWorkQuery ecmArtWorkQuery);
	
	Integer getEcmArtworkRootNode(Integer pkArtworkId);
}
