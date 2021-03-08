package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmGoodsQuery;
import com.mpic.evolution.chair.service.EcmGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author by cxd
 * @Classname EcmGoodsController
 * @Description TODO
 * @Date 2021/3/8 18:04
 */
@Controller
@RequestMapping("goods")
public class EcmGoodsController {
    final
    EcmGoodsService ecmGoodsService;

    public EcmGoodsController(EcmGoodsService ecmGoodsService) {
        this.ecmGoodsService = ecmGoodsService;
    }

    @RequestMapping("getGoodsAll")
    @ResponseBody
    public ResponseDTO getGoodsAll(@RequestBody EcmGoodsQuery ecmGoodsQuery) {
        return  ecmGoodsService.getGoodsAll(ecmGoodsQuery);
    }

}
