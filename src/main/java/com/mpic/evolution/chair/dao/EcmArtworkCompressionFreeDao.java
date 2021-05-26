package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkCompressionFree;

public interface EcmArtworkCompressionFreeDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmArtworkCompressionFree record);

    int insertSelective(EcmArtworkCompressionFree record);

    EcmArtworkCompressionFree selectByPrimaryKey(Integer pkId);

    int updateByPrimaryKeySelective(EcmArtworkCompressionFree record);

    int updateByPrimaryKey(EcmArtworkCompressionFree record);
}