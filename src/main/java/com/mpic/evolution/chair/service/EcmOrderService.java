package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;

/**
 * @author by cxd
 * @Classname EcmOrderService
 * @Description TODO
 * @Date 2021/3/8 20:12
 */
public interface EcmOrderService {
    /**
     * @param: [ecmOrderVO]
     * @return: com.mpic.evolution.chair.pojo.vo.EcmOrderVO
     * @author: cxd
     * @Date: 2021/3/10
     * 描述 : 购买商品 生成订单
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */

    EcmOrderVO buyGoods(EcmOrderVO ecmOrderVO);

    /**
     * @param: [ecmOrderVO]
     * @return: boolean
     * @author: cxd
     * @Date: 2021/3/10
     * 描述 : 更新支付订单结果
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    boolean updateOrderByPay(EcmOrderVO ecmOrderVO);

    /**
     * @param: [ecmOrderVO]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2021/3/10
     * 描述 : 查询订单结果
     */
    ResponseDTO queryOrderResult(EcmOrderVO ecmOrderVO);
}
