package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.dao.EcmOrderHistoryDao;
import com.mpic.evolution.chair.service.EcmOrderHistoryService;
import com.mpic.evolution.chair.service.EcmPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author by cxd
 * @Classname PayController
 * @Description TODO
 * @Date 2021/3/9 16:53
 */
@Controller
public class PayController {
    final
    EcmPayService ecmPayService;
    EcmOrderHistoryService ecmOrderHistoryService;

    public PayController(EcmPayService ecmPayService, EcmOrderHistoryService ecmOrderHistoryService) {
        this.ecmPayService = ecmPayService;
        this.ecmOrderHistoryService = ecmOrderHistoryService;
    }

    @RequestMapping("callpay.action")
    public void wxPayAction(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("wx支付回调成功！！");

        try {
            ecmPayService.wxPayNotify( request,  response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
