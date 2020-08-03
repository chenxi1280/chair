package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHistory;

public interface EcmArtworkBroadcastHistoryDao {
    int deleteByPrimaryKey(Integer pkBroadcastId);

    int insert(EcmArtworkBroadcastHistory record);

    int insertSelective(EcmArtworkBroadcastHistory record);

    EcmArtworkBroadcastHistory selectByPrimaryKey(Integer pkBroadcastId);

    int updateByPrimaryKeySelective(EcmArtworkBroadcastHistory record);

    int updateByPrimaryKey(EcmArtworkBroadcastHistory record);
}