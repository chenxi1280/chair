package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.common.returnvo.ReturnVo;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;

/**
 * @author Administrator
 */
public interface EcmArtWorkService {

    /**
     *
     *
     * @param ecmArtWorkQuery 传入的 查询参数
     * @return ResponseDTO中的data 包含 ArtWork的 条件查询 结果集
     */
    ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery);


}
