package com.mpic.evolution.chair.controller;

import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.mpic.evolution.chair.pojo.tencent.wx.pay.WxPayDTO;
import com.mpic.evolution.chair.service.EcmPaySerice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

    @ResponseBody
    @RequestMapping("callpay.action")
    public void wxPayAction(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("wx支付回调成功！！");

        try {
            ecmPaySerice.wxPayNotify( request,  response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
