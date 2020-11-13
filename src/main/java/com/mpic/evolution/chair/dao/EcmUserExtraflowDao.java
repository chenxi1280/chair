package com.mpic.evolution.chair.dao;

import java.util.List;

import com.mpic.evolution.chair.pojo.entity.EcmUserExtraflow;
import com.mpic.evolution.chair.pojo.query.EcmUserExtraflowQuery;

public interface EcmUserExtraflowDao {
    int deleteByPrimaryKey(Integer extraflowId);

    int insert(EcmUserExtraflow record);

    int insertSelective(EcmUserExtraflow record);

    EcmUserExtraflow selectByPrimaryKey(Integer extraflowId);
    
    List<EcmUserExtraflow> selectByExtraflow(EcmUserExtraflowQuery record);
    
    int updateByPrimaryKeySelective(EcmUserExtraflow record);

    int updateByPrimaryKey(EcmUserExtraflow record);
}