package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmUserRoles;

public interface EcmUserRolesDao {
    int deleteByPrimaryKey(Integer pkRoleId);

    int insert(EcmUserRoles record);

    int insertSelective(EcmUserRoles record);

    EcmUserRoles selectByPrimaryKey(Integer pkRoleId);

    int updateByPrimaryKeySelective(EcmUserRoles record);

    int updateByPrimaryKey(EcmUserRoles record);
}