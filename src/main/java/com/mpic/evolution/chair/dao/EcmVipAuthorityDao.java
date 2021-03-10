package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmVipAuthority;

import java.util.List;

public interface EcmVipAuthorityDao {
    int deleteByPrimaryKey(Integer pkAuthorityId);

    int insert(EcmVipAuthority record);

    int insertSelective(EcmVipAuthority record);

    EcmVipAuthority selectByPrimaryKey(Integer pkAuthorityId);

    List<EcmVipAuthority> selectByAll();

    int updateByPrimaryKeySelective(EcmVipAuthority record);

    int updateByPrimaryKey(EcmVipAuthority record);
}