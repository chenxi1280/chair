package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkTagMap;

public interface EcmArtworkTagMapDao {
    int deleteByPrimaryKey(Integer pkMapId);

    int insert(EcmArtworkTagMap record);

    int insertSelective(EcmArtworkTagMap record);

    EcmArtworkTagMap selectByPrimaryKey(Integer pkMapId);

    int updateByPrimaryKeySelective(EcmArtworkTagMap record);

    int updateByPrimaryKey(EcmArtworkTagMap record);
}