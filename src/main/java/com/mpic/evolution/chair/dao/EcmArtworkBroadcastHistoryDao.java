package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHistory;

import java.util.List;

public interface EcmArtworkBroadcastHistoryDao {
    int deleteByPrimaryKey(Integer pkBroadcastId);

    int insert(EcmArtworkBroadcastHistory record);

    int insertSelective(EcmArtworkBroadcastHistory record);

    EcmArtworkBroadcastHistory selectByPrimaryKey(Integer pkBroadcastId);
    // TODO 补充完整
    List<EcmArtworkBroadcastHistory> selectByRecord(EcmArtworkBroadcastHistory record);

    int updateByPrimaryKeySelective(EcmArtworkBroadcastHistory record);

    int updateByPrimaryKey(EcmArtworkBroadcastHistory record);
}