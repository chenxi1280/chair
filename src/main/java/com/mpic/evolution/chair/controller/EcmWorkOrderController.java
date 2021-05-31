package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmWorkOrder;
import com.mpic.evolution.chair.pojo.vo.EcmWorkOrderVO;
import com.mpic.evolution.chair.service.EcmWorkOrderService;
import com.mpic.evolution.chair.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mpic.evolution.chair.common.returnvo.ErrorEnum.ERR_613;

/**
 * @author by cxd
 * @Classname EcmWorkOrderController
 * @Description TODO
 * @Date 2021/5/31 13:25
 */
@RestController
@RequestMapping("workOrder")
public class EcmWorkOrderController extends BaseController{
    final
    RedisUtil redisUtil;
    EcmWorkOrderService ecmWorkOrderService;


    public EcmWorkOrderController(EcmWorkOrderService ecmWorkOrderService, RedisUtil redisUtil) {
        this.ecmWorkOrderService = ecmWorkOrderService;
        this.redisUtil = redisUtil;
    }

    @RequestMapping("saveWorkOrder")
    public ResponseDTO saveWorkOrder(@RequestBody EcmWorkOrderVO ecmWorkOrderVO){
        ecmWorkOrderVO.setFkUserId(getUserIdByHandToken());

//        String key = "chair-WorkOrder-EcmWorkOrderController-saveWorkOrder-" + getUserIdByHandToken() ;
//        if (redisUtil.hasKey(key) ){
//            return ResponseDTO.fail(ERR_613.getText(),ERR_613.getValue());
//        }
//        redisUtil.set(key,key, 60 * 60);
        return ecmWorkOrderService.saveWorkOrder(ecmWorkOrderVO);
    }


}
