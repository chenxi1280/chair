package com.mpic.evolution.chair.service;

/**
 * @author by cxd
 * @Classname EcmOrderHistoryService
 * @Description TODO
 * @Date 2021/3/10 10:21
 */
public interface EcmOrderHistoryService {
    /**
     * @param: [out_trade_no] 订单code  支付订单的实际金额
     * @return: void
     * @author: cxd
     * @Date: 2021/3/10
     * 描述 : 完成对订单的存档
     */
    void insertOrderHistory(String code, String total);
}
