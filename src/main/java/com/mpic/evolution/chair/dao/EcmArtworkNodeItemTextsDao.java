package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeItemTexts;

public interface EcmArtworkNodeItemTextsDao {
    int deleteByPrimaryKey(Integer pkNodeItemTextId);

    int insert(EcmArtworkNodeItemTexts record);

    int insertSelective(EcmArtworkNodeItemTexts record);

    EcmArtworkNodeItemTexts selectByPrimaryKey(Integer pkNodeItemTextId);

    int updateByPrimaryKeySelective(EcmArtworkNodeItemTexts record);

    int updateByPrimaryKey(EcmArtworkNodeItemTexts record);
}