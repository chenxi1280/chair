package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface EcmOrderDao {
    int deleteByPrimaryKey(Integer pkOrderId);

    int insert(EcmOrder record);

    int insertSelective(EcmOrder record);

    EcmOrder selectByPrimaryKey(Integer pkOrderId);

    int updateByPrimaryKeySelective(EcmOrder record);

    int updateByPrimaryKey(EcmOrder record);
}
