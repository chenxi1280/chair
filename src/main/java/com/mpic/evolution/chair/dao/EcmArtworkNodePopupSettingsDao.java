package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodePopupSettings;

public interface EcmArtworkNodePopupSettingsDao {
    int deleteByPrimaryKey(Integer pkNodePopupSettingsId);

    int insert(EcmArtworkNodePopupSettings record);

    int insertSelective(EcmArtworkNodePopupSettings record);

    EcmArtworkNodePopupSettings selectByPrimaryKey(Integer pkNodePopupSettingsId);

    int updateByPrimaryKeySelective(EcmArtworkNodePopupSettings record);

    int updateByPrimaryKey(EcmArtworkNodePopupSettings record);
}