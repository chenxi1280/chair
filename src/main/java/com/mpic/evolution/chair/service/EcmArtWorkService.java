package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;

/**
 * @author Administrator
 */
public interface EcmArtWorkService {

    /**
     *
     *
     * @param ecmArtWorkQuery 传入的 查询参数 查询参数可以有 用户id，用户名字（模糊），视频状态，类型（当前模糊）
     * @return ResponseDTO 中的data 包含 ArtWork的 条件查询 结果集
     */
    ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery);

    /**
     * 描述 :
     * @author: cxd
     * @Date: 2020/8/4
     * @param:
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     */
    ResponseDTO getArtWork(EcmArtWorkQuery ecmArtWorkQuery);
    
    /**
     * @author: SJ
     * @param ecmArtWorkQuery
     * @param code
     * @return
     */
	ResponseDTO modifyArtWorkStatus(EcmArtWorkQuery ecmArtWorkQuery,String code);
	
	/**
     * @author: SJ
     * @param ecmArtWorkQuery
     * @param code
     * @return
     */
	ResponseDTO modifyArtWork(EcmArtWorkQuery ecmArtWorkQuery, String code);
	
	/**
     * @author: SJ
     * @param ecmArtworkVo
     * @param code
     * @return
     */
	ResponseDTO addArtWorks(EcmArtworkVo ecmArtworkVo);
}
