package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.dao.EcmGoodsDao;
import com.mpic.evolution.chair.dao.EcmOrderDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmGoods;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;
import com.mpic.evolution.chair.service.EcmGoodsService;
import com.mpic.evolution.chair.service.EcmOrderService;
import com.mpic.evolution.chair.service.EcmPaySerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * @author by cxd
 * @Classname EcmOrderServiceImpl
 * @Description TODO
 * @Date 2021/3/8 20:12
 */
@Service
public class EcmOrderServiceImpl implements EcmOrderService {
    final
    EcmOrderDao ecmOrderDao;
    EcmPaySerice ecmPaySerice;
    EcmGoodsService ecmGoodsService;

    public EcmOrderServiceImpl(EcmGoodsService ecmGoodsService, EcmOrderDao ecmOrderDao, EcmPaySerice ecmPaySerice) {
        this.ecmGoodsService = ecmGoodsService;
        this.ecmOrderDao = ecmOrderDao;
        this.ecmPaySerice = ecmPaySerice;
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
}
