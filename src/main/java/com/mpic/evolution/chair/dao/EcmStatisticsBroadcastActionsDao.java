package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmStatisticsBroadcastActions;

public interface EcmStatisticsBroadcastActionsDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmStatisticsBroadcastActions record);

    int insertSelective(EcmStatisticsBroadcastActions record);

    EcmStatisticsBroadcastActions selectByPrimaryKey(Integer pkId);

    int updateByPrimaryKeySelective(EcmStatisticsBroadcastActions record);

    int updateByPrimaryKey(EcmStatisticsBroadcastActions record);
}