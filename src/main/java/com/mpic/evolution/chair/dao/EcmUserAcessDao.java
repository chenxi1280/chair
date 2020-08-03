package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmUserAcess;

public interface EcmUserAcessDao {
    int deleteByPrimaryKey(Integer pkAcessId);

    int insert(EcmUserAcess record);

    int insertSelective(EcmUserAcess record);

    EcmUserAcess selectByPrimaryKey(Integer pkAcessId);

    int updateByPrimaryKeySelective(EcmUserAcess record);

    int updateByPrimaryKey(EcmUserAcess record);
}