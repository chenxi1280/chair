package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeEnding;

public interface EcmArtworkNodeEndingDao {
    int deleteByPrimaryKey(Integer pkEndingId);

    int insert(EcmArtworkNodeEnding record);

    int insertSelective(EcmArtworkNodeEnding record);

    EcmArtworkNodeEnding selectByPrimaryKey(Integer pkEndingId);

    int updateByPrimaryKeySelective(EcmArtworkNodeEnding record);

    int updateByPrimaryKey(EcmArtworkNodeEnding record);
}