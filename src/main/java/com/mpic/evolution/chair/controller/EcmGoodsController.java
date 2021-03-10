package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.common.exception.TokenException;
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
public class EcmGoodsController extends BaseController{
    final
    EcmGoodsService ecmGoodsService;

    public EcmGoodsController(EcmGoodsService ecmGoodsService) {
        this.ecmGoodsService = ecmGoodsService;
    }

    /**
     * @param: [ecmGoodsQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2021/3/9
     * 描述 : 获取商品信息
     *       成功: status 200  msg "success”   date: map（商品类别，List<EcmGoodsVO>）
     *       失败: status 500  msg "error“
     */
    @RequestMapping("getGoodsAll")
    @ResponseBody
    public ResponseDTO getGoodsAll(@RequestBody EcmGoodsQuery ecmGoodsQuery) {
        Integer userIdByHandToken = getUserIdByHandToken();
        if (userIdByHandToken == null ){
            throw new TokenException();
        }
        return  ecmGoodsService.getGoodsAll(ecmGoodsQuery);
    }

}
