package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.service.EcmOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author by cxd
 * @Classname EcmOrderController
 * @Description TODO
 * @Date 2021/3/8 20:11
 */
@Controller
@RequestMapping("order")
public class EcmOrderController {

    final
    EcmOrderService ecmOrderService;


    public EcmOrderController(EcmOrderService ecmOrderService) {
        this.ecmOrderService = ecmOrderService;
    }

    






}
