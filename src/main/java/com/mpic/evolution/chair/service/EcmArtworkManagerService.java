package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-4 10:14:17 
*/
public interface EcmArtworkManagerService {
	/**
     * @author: SJ
     * @param ecmArtWorkQuery
     * @param code
     * @return
     */
	ResponseDTO modifyArtWorkStatus(EcmArtworkVo ecmArtworkVo);

	/**
     * @author: SJ
     * @param ecmArtWorkQuery
     * @param code
     * @return
     */
	ResponseDTO modifyArtWork(EcmArtworkVo ecmArtworkVo);
	
	/**
     * @author: SJ
     * @param ecmArtworkVo
     * @param code
     * @return
     */
	ResponseDTO addArtWorks(EcmArtworkVo ecmArtworkVo);
	
}
