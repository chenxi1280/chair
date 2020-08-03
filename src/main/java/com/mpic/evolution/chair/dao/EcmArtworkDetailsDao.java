package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkDetails;

public interface EcmArtworkDetailsDao {
    int deleteByPrimaryKey(Integer pkDetailId);

    int insert(EcmArtworkDetails record);

    int insertSelective(EcmArtworkDetails record);

    EcmArtworkDetails selectByPrimaryKey(Integer pkDetailId);

    int updateByPrimaryKeySelective(EcmArtworkDetails record);

    int updateByPrimaryKey(EcmArtworkDetails record);
}