package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeItemRadios;

public interface EcmArtworkNodeItemRadiosDao {
    int deleteByPrimaryKey(Integer pkItemRadioId);

    int insert(EcmArtworkNodeItemRadios record);

    int insertSelective(EcmArtworkNodeItemRadios record);

    EcmArtworkNodeItemRadios selectByPrimaryKey(Integer pkItemRadioId);

    int updateByPrimaryKeySelective(EcmArtworkNodeItemRadios record);

    int updateByPrimaryKey(EcmArtworkNodeItemRadios record);
}