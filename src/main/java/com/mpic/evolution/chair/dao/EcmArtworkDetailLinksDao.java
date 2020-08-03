package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkDetailLinks;

public interface EcmArtworkDetailLinksDao {
    int deleteByPrimaryKey(Integer pkResultId);

    int insert(EcmArtworkDetailLinks record);

    int insertSelective(EcmArtworkDetailLinks record);

    EcmArtworkDetailLinks selectByPrimaryKey(Integer pkResultId);

    int updateByPrimaryKeySelective(EcmArtworkDetailLinks record);

    int updateByPrimaryKey(EcmArtworkDetailLinks record);
}