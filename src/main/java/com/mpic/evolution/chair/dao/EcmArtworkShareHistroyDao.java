package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkShareHistroy;

public interface EcmArtworkShareHistroyDao {
    int deleteByPrimaryKey(Integer pkSharedId);

    int insert(EcmArtworkShareHistroy record);

    int insertSelective(EcmArtworkShareHistroy record);

    EcmArtworkShareHistroy selectByPrimaryKey(Integer pkSharedId);

    int updateByPrimaryKeySelective(EcmArtworkShareHistroy record);

    int updateByPrimaryKey(EcmArtworkShareHistroy record);
}