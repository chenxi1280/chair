package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmGoods;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;

/**
 * @author by cxd
 * @Classname EcmOrderService
 * @Description TODO
 * @Date 2021/3/8 20:12
 */
public interface EcmOrderService {
    EcmOrderVO buyGoods(EcmOrderVO ecmOrderVO);

    boolean updateOrderByPay(EcmOrderVO ecmOrderVO);
}
