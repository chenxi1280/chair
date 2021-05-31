package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmWorkOrderVO;

/**
 * @author by cxd
 * @Classname EcmWorkOrderService
 * @Description TODO
 * @Date 2021/5/31 13:26
 */
public interface EcmWorkOrderService {
    ResponseDTO saveWorkOrder(EcmWorkOrderVO ecmWorkOrderVO);
}
