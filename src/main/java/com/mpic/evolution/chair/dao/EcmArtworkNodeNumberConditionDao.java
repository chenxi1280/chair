package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeNumberCondition;

public interface EcmArtworkNodeNumberConditionDao {
    int deleteByPrimaryKey(Integer pkDetailid);

    int insert(EcmArtworkNodeNumberCondition record);

    int insertSelective(EcmArtworkNodeNumberCondition record);

    EcmArtworkNodeNumberCondition selectByPrimaryKey(Integer pkDetailid);

    int updateByPrimaryKeySelective(EcmArtworkNodeNumberCondition record);

    int updateByPrimaryKey(EcmArtworkNodeNumberCondition record);
}