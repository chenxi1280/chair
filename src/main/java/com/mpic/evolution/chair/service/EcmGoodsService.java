package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmGoods;
import com.mpic.evolution.chair.pojo.query.EcmGoodsQuery;
import com.mpic.evolution.chair.pojo.vo.EcmGoodsVO;

/**
 * @author by cxd
 * @Classname EcmGoodsService
 * @Description TODO
 * @Date 2021/3/8 18:09
 */
public interface EcmGoodsService {
    ResponseDTO getGoodsAll(EcmGoodsQuery ecmGoodsQuery);

    EcmGoods getGoodsVOByPkId(Integer id);


}
