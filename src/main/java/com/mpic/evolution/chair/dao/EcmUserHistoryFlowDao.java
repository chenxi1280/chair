package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmUserHistoryFlow;

public interface EcmUserHistoryFlowDao {
    int deleteByPrimaryKey(Integer flowHistoryId);

    int insert(EcmUserHistoryFlow record);

    int insertSelective(EcmUserHistoryFlow record);

    EcmUserHistoryFlow selectByPrimaryKey(Integer flowHistoryId);

    int updateByPrimaryKeySelective(EcmUserHistoryFlow record);

    int updateByPrimaryKey(EcmUserHistoryFlow record);
}