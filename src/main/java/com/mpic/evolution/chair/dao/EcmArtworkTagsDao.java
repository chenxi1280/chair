package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkTags;

public interface EcmArtworkTagsDao {
    int deleteByPrimaryKey(Integer pkTagId);

    int insert(EcmArtworkTags record);

    int insertSelective(EcmArtworkTags record);

    EcmArtworkTags selectByPrimaryKey(Integer pkTagId);

    int updateByPrimaryKeySelective(EcmArtworkTags record);

    int updateByPrimaryKey(EcmArtworkTags record);
}