package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;

/**
 * @author by cxd
 * @Classname MerchantService
 * @Description TODO
 * @Date 2021/3/1 9:29
 */
public interface MerchantService {
    ResponseDTO getMerchantUrl(EcmArtworkVo ecmArtworkVo);
}
