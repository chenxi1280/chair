package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmInviteCode;

public interface EcmInviteCodeDao {
    int deleteByPrimaryKey(Integer pkInviteId);

    int insert(EcmInviteCode record);

    int insertSelective(EcmInviteCode record);

    EcmInviteCode selectByPrimaryKey(Integer pkInviteId);
    
    EcmInviteCode selectByEcmInviteCode(EcmInviteCode record);

    int updateByPrimaryKeySelective(EcmInviteCode record);

    int updateByPrimaryKey(EcmInviteCode record);
}