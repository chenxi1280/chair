package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmCommmentThumbsupHistory;

public interface EcmCommmentThumbsupHistoryDao {
    int deleteByPrimaryKey(Integer pkThumbsupId);

    int insert(EcmCommmentThumbsupHistory record);

    int insertSelective(EcmCommmentThumbsupHistory record);

    EcmCommmentThumbsupHistory selectByPrimaryKey(Integer pkThumbsupId);

    int updateByPrimaryKeySelective(EcmCommmentThumbsupHistory record);

    int updateByPrimaryKey(EcmCommmentThumbsupHistory record);
}