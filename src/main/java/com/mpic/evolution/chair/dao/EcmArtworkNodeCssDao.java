package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeCss;

public interface EcmArtworkNodeCssDao {
    int deleteByPrimaryKey(Integer pkNodeCssId);

    int insert(EcmArtworkNodeCss record);

    int insertSelective(EcmArtworkNodeCss record);

    EcmArtworkNodeCss selectByPrimaryKey(Integer pkNodeCssId);

    int updateByPrimaryKeySelective(EcmArtworkNodeCss record);

    int updateByPrimaryKey(EcmArtworkNodeCss record);
}