package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;

/**
 * @author by cxd
 * @Classname EcmPaySerice
 * @Description TODO
 * @Date 2021/3/9 9:20
 */
public interface EcmPaySerice {
    ResponseDTO wxPayQueryOrder(EcmOrderVO ecmOrderVO);
}
