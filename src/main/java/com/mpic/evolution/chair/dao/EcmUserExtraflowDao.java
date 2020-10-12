package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmUserExtraflow;

public interface EcmUserExtraflowDao {
    int deleteByPrimaryKey(Integer extraflowId);

    int insert(EcmUserExtraflow record);

    int insertSelective(EcmUserExtraflow record);

    EcmUserExtraflow selectByPrimaryKey(Integer extraflowId);

    int updateByPrimaryKeySelective(EcmUserExtraflow record);

    int updateByPrimaryKey(EcmUserExtraflow record);
}