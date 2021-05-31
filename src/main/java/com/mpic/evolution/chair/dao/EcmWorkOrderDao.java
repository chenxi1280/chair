package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmWorkOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface EcmWorkOrderDao {
    int deleteByPrimaryKey(Integer pkWorkOrderId);

    int insert(EcmWorkOrder record);

    int insertSelective(EcmWorkOrder record);

    EcmWorkOrder selectByPrimaryKey(Integer pkWorkOrderId);

    int updateByPrimaryKeySelective(EcmWorkOrder record);

    int updateByPrimaryKey(EcmWorkOrder record);
}
