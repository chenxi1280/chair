package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author by cxd
 * @Classname EcmPaySerice
 * @Description TODO
 * @Date 2021/3/9 9:20
 */
public interface EcmPayService {
    /**
     * @param: [ecmOrderVO]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2021/3/9
     * 描述 : 微信下单接口
     *       成功: status 200  msg "success”   date: 微信二维码
     *       失败: status 500  msg "error“
     */
    ResponseDTO wxPayQueryOrder(EcmOrderVO ecmOrderVO);

    /**
     * @param: [request, response]
     * @return: void
     * @author: cxd
     * @Date: 2021/3/10
     * 描述 : 微信回调
     */
    void wxPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception;


}
