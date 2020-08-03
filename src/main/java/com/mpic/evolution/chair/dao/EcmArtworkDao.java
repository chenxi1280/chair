package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtwork;

public interface EcmArtworkDao {
    int deleteByPrimaryKey(Integer pkArtworkId);

    int insert(EcmArtwork record);

    int insertSelective(EcmArtwork record);

    EcmArtwork selectByPrimaryKey(Integer pkArtworkId);

    int updateByPrimaryKeySelective(EcmArtwork record);

    int updateByPrimaryKey(EcmArtwork record);
}