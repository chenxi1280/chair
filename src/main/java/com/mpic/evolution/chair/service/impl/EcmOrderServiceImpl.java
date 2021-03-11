package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.dao.EcmOrderDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmGoods;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;
import com.mpic.evolution.chair.service.EcmGoodsService;
import com.mpic.evolution.chair.service.EcmOrderService;
import com.mpic.evolution.chair.service.vip.BeanConfig;
import com.mpic.evolution.chair.service.vip.PaymentVipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static com.mpic.evolution.chair.common.constant.CommonField.*;

/**
 * @author by cxd
 * @Classname EcmOrderServiceImpl
 * @Description TODO
 * @Date 2021/3/8 20:12
 */
@Service
public class EcmOrderServiceImpl implements EcmOrderService {

    @Resource
    private BeanConfig beanConfig;

    final
    EcmOrderDao ecmOrderDao;
    EcmGoodsService ecmGoodsService;

    public EcmOrderServiceImpl(EcmGoodsService ecmGoodsService, EcmOrderDao ecmOrderDao) {
        this.ecmGoodsService = ecmGoodsService;
        this.ecmOrderDao = ecmOrderDao;
    }

    @Override
    public EcmOrderVO buyGoods(EcmOrderVO ecmOrderVO) {
        EcmGoods goodsVO = ecmGoodsService.getGoodsVOByPkId(ecmOrderVO.getFkGoodsId());
        if (goodsVO == null) {
            return null;
        }
//        价格！！！ 先设置成0.01
//        ecmOrderVO.setOrderPrice(goodsVO.getGoodsPrice());
        ecmOrderVO.setOrderPrice(BigDecimal.valueOf(0.01));
        ecmOrderVO.setCreateTime(new Date());
        ecmOrderVO.setOrderState(0);

        ecmOrderVO.setGoodsName(goodsVO.getGoodsName());
        ecmOrderVO.setOrderCode(System.currentTimeMillis() + ecmOrderVO.getFkGoodsId() + goodsVO.getPkGoodsId()  + UUID.randomUUID().toString().split("-")[0]  );
        ecmOrderVO.setOrderGoodsNumber(1);

        ecmOrderDao.insertSelective(ecmOrderVO);

        return ecmOrderVO;
    }

    @Override
    public boolean updateOrderByPay(EcmOrderVO ecmOrderVO) {
        return 1 == ecmOrderDao.updateWxPayOrderByCode(ecmOrderVO);
    }

    @Override
    public ResponseDTO queryOrderResult(EcmOrderVO ecmOrderVO) {
        EcmOrderVO ecmOrder = ecmOrderDao.selectByOrderCode(ecmOrderVO.getOrderCode());
        if (ecmOrder == null ){
            return ResponseDTO.fail("请支付","",500,500);
        }
        if (INTEGER_TWO.equals(ecmOrder.getOrderState())){
            return ResponseDTO.ok("购买成功");
        }
        return ResponseDTO.fail("请等待","",470,470);
    }

    @Override
    public EcmOrderVO queryOrderInfo(String orderCode) {
        return ecmOrderDao.selectByOrderCode(orderCode);
    }

    @Override
    public void savaVipPaymentInfo(String orderCode) {
        EcmOrderVO ecmOrderVO = queryOrderInfo(orderCode);
        EcmGoods goods = ecmGoodsService.getGoodsVOByPkId(ecmOrderVO.getFkGoodsId());
        PaymentVipService updateVipDate = beanConfig.createQueryService(goods.getGoodsType());
        System.out.println(updateVipDate.toString());
        updateVipDate.operationRelateToPayment(goods.getGoodsActionNumber(),ecmOrderVO.getFkUserId(),goods.getGoodsName());
    }

}
