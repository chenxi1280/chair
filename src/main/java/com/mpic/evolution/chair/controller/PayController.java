package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.pojo.tencent.wx.pay.WxPayDTO;
import com.mpic.evolution.chair.service.EcmPaySerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author by cxd
 * @Classname PayController
 * @Description TODO
 * @Date 2021/3/9 16:53
 */
@Controller
public class PayController {
    final
    EcmPaySerice ecmPaySerice;

    public PayController(EcmPaySerice ecmPaySerice) {
        this.ecmPaySerice = ecmPaySerice;
    }

    @RequestMapping("callpay.action")
    public void wxPayAction(@RequestBody WxPayDTO wxPayDTO) {
        System.out.println("wx支付回调成功！！");
        System.out.println(wxPayDTO.toString());
//        ecmPaySerice.wxPayAction();


    }


}
