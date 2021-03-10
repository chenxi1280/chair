package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmOrder;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;
import org.springframework.stereotype.Repository;

@Repository
public interface EcmOrderDao {
    int deleteByPrimaryKey(Integer pkOrderId);

    int insert(EcmOrder record);

    int insertSelective(EcmOrder record);

    EcmOrder selectByPrimaryKey(Integer pkOrderId);

    int updateByPrimaryKeySelective(EcmOrder record);

    int updateByPrimaryKey(EcmOrder record);

    int updateWxPayOrderByCode(EcmOrderVO ecmOrderVO);

    /**
     * @param: [code] 订单code
     * @return: com.mpic.evolution.chair.pojo.vo.EcmOrderVO
     * @author: cxd
     * @Date: 2021/3/10
     * 描述 : 订单code 获取 订单信息
     */
    EcmOrderVO selectByOrderCode(String code);

}
