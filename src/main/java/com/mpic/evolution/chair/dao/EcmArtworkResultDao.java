package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkResult;

public interface EcmArtworkResultDao {
    int deleteByPrimaryKey(Integer pkArtworkResultId);

    int insert(EcmArtworkResult record);

    int insertSelective(EcmArtworkResult record);

    EcmArtworkResult selectByPrimaryKey(Integer pkArtworkResultId);

    int updateByPrimaryKeySelective(EcmArtworkResult record);

    int updateByPrimaryKey(EcmArtworkResult record);
}