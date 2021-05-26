package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmSubappUpdateHistory;

public interface EcmSubappUpdateHistoryDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmSubappUpdateHistory record);

    int insertSelective(EcmSubappUpdateHistory record);

    EcmSubappUpdateHistory selectByPrimaryKey(Integer pkId);

    EcmSubappUpdateHistory selectByRecord(EcmSubappUpdateHistory record);

    int updateByPrimaryKeySelective(EcmSubappUpdateHistory record);

    int updateByPrimaryKey(EcmSubappUpdateHistory record);
}