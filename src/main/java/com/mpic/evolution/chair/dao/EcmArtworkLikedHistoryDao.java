package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkLikedHistory;

public interface EcmArtworkLikedHistoryDao {
    int deleteByPrimaryKey(Integer pkLikedId);

    int insert(EcmArtworkLikedHistory record);

    int insertSelective(EcmArtworkLikedHistory record);

    EcmArtworkLikedHistory selectByPrimaryKey(Integer pkLikedId);

    int updateByPrimaryKeySelective(EcmArtworkLikedHistory record);

    int updateByPrimaryKey(EcmArtworkLikedHistory record);
}