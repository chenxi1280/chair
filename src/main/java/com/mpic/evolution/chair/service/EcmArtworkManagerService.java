package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.pojo.vo.FreeAdVo;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-4 10:14:17 
*/
public interface EcmArtworkManagerService {
	/**
     * @author: SJ
     * @param ecmArtworkVo
     * @return
     */
	ResponseDTO modifyArtWorkStatus(EcmArtworkVo ecmArtworkVo);

	/**
     * @author: SJ
     * @param ecmArtworkVo
     * @return
     */
	ResponseDTO modifyArtWork(EcmArtworkVo ecmArtworkVo);
	
	/**
     * @author: SJ
     * @param ecmArtworkVo
     * @return
     */
	ResponseDTO addArtWorks(EcmArtworkVo ecmArtworkVo);

	/**
	 * @author: SJ
	 * @param freeAdVo
	 * @return
	 */
	ResponseDTO checkFreeAd(FreeAdVo freeAdVo);
}
