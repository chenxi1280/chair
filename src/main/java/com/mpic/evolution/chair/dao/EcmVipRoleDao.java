package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmVipRole;

public interface EcmVipRoleDao {
    int deleteByPrimaryKey(Integer pkRoleId);

    int insert(EcmVipRole record);

    int insertSelective(EcmVipRole record);

    EcmVipRole selectByPrimaryKey(Integer pkRoleId);

    int updateByPrimaryKeySelective(EcmVipRole record);

    int updateByPrimaryKey(EcmVipRole record);
}