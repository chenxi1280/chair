package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkCompressionFree;

public interface EcmArtworkCompressionFreeDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmArtworkCompressionFree record);

    int insertSelective(EcmArtworkCompressionFree record);

    EcmArtworkCompressionFree selectByPrimaryKey(Integer pkId);

    EcmArtworkCompressionFree selectByRecord(EcmArtworkCompressionFree record);

    int updateByPrimaryKeySelective(EcmArtworkCompressionFree record);

    int updateByPrimaryKey(EcmArtworkCompressionFree record);
}