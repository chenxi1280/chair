package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmUser;

public interface EcmUserDao {
    int deleteByPrimaryKey(Integer pkUserId);

    int insert(EcmUser record);

    int insertSelective(EcmUser record);

    EcmUser selectByPrimaryKey(Integer pkUserId);

    int updateByPrimaryKeySelective(EcmUser record);

    int updateByPrimaryKey(EcmUser record);
}