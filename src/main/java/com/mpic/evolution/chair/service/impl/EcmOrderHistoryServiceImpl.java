package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.dao.EcmGoodsDao;
import com.mpic.evolution.chair.dao.EcmOrderDao;
import com.mpic.evolution.chair.dao.EcmOrderHistoryDao;
import com.mpic.evolution.chair.pojo.entity.EcmGoods;
import com.mpic.evolution.chair.pojo.entity.EcmOrderHistory;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;
import com.mpic.evolution.chair.service.EcmOrderHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author by cxd
 * @Classname EcmOrderHistoryServiceImpl
 * @Description TODO
 * @Date 2021/3/10 10:21
 */
@Service
public class EcmOrderHistoryServiceImpl implements EcmOrderHistoryService {

    final
    EcmOrderHistoryDao ecmOrderHistoryDao;
    EcmOrderDao ecmOrderDao;
    EcmGoodsDao ecmGoodsDao;

    public EcmOrderHistoryServiceImpl(EcmOrderHistoryDao ecmOrderHistoryDao, EcmOrderDao ecmOrderDao, EcmGoodsDao ecmGoodsDao) {
        this.ecmOrderHistoryDao = ecmOrderHistoryDao;
        this.ecmOrderDao = ecmOrderDao;
        this.ecmGoodsDao = ecmGoodsDao;
    }

    @Override
    public void insertOrderHistory(String code ,String total) {
        EcmOrderVO ecmOrderVO = ecmOrderDao.selectByOrderCode(code);
        EcmGoods ecmGoods = ecmGoodsDao.selectByPrimaryKey(ecmOrderVO.getFkGoodsId());
        EcmOrderHistory ecmOrderHistory = new EcmOrderHistory();
        BeanUtils.copyProperties(ecmOrderVO,ecmOrderHistory);
        BeanUtils.copyProperties( ecmGoods,ecmOrderHistory);
        ecmOrderHistory.setFkOrderId(ecmOrderVO.getPkOrderId());
        ecmOrderHistory.setPayOrderPrice(BigDecimal.valueOf( Long.parseLong(total) / 100 ));
        ecmOrderHistory.setCreateTime(new Date());
        ecmOrderHistoryDao.insertSelective(ecmOrderHistory);

    }
}
