package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.common.exception.TokenException;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmGoods;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;
import com.mpic.evolution.chair.service.EcmOrderService;
import com.mpic.evolution.chair.service.EcmPaySerice;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;

import static com.mpic.evolution.chair.common.returnvo.ErrorEnum.ERR_603;

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
    EcmPaySerice ecmPaySerice;


    public EcmOrderController(EcmOrderService ecmOrderService, EcmPaySerice ecmPaySerice) {
        this.ecmOrderService = ecmOrderService;
        this.ecmPaySerice = ecmPaySerice;
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
        ecmPaySerice.wxPayQueryOrder(ecmGoods);


        return  ecmPaySerice.wxPayQueryOrder(ecmGoods);
    }





}
