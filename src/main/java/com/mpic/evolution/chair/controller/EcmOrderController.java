package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.common.exception.TokenException;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;
import com.mpic.evolution.chair.service.EcmOrderService;
import com.mpic.evolution.chair.service.EcmPayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author by cxd
 * @Classname EcmOrderController
 * @Description TODO
 * @Date 2021/3/8 20:11
 */
@Controller
@RequestMapping("order")
public class EcmOrderController extends BaseController{

    final
    EcmOrderService ecmOrderService;
    EcmPayService ecmPayService;


    public EcmOrderController(EcmOrderService ecmOrderService, EcmPayService ecmPayService) {
        this.ecmOrderService = ecmOrderService;
        this.ecmPayService = ecmPayService;
    }

    @RequestMapping("buyGoods")
    @ResponseBody
    public ResponseDTO buyGoods(@RequestBody EcmOrderVO ecmOrderVO) {
        Integer userIdByHandToken = getUserIdByHandToken();
        if (userIdByHandToken == null ){
            throw new TokenException();
        }
        ecmOrderVO.setFkUserId(userIdByHandToken);
        EcmOrderVO ecmGoods= ecmOrderService.buyGoods(ecmOrderVO);
        ecmPayService.wxPayQueryOrder(ecmGoods);


        return  ecmPayService.wxPayQueryOrder(ecmGoods);
    }





}
