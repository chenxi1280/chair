package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmOrderHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface EcmOrderHistoryDao {
    int deleteByPrimaryKey(Integer pkOrderHistoryId);

    int insert(EcmOrderHistory record);

    int insertSelective(EcmOrderHistory record);

    EcmOrderHistory selectByPrimaryKey(Integer pkOrderHistoryId);

    int updateByPrimaryKeySelective(EcmOrderHistory record);

    int updateByPrimaryKey(EcmOrderHistory record);
}
