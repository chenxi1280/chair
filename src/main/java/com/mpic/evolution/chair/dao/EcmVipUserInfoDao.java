package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmVipUserInfo;

import java.util.List;

public interface EcmVipUserInfoDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmVipUserInfo record);

    int insertSelective(EcmVipUserInfo record);

    EcmVipUserInfo selectByPrimaryKey(Integer pkId);

    List<EcmVipUserInfo> selectByUserInfo(EcmVipUserInfo record);

    int updateByPrimaryKeySelective(EcmVipUserInfo record);

    int updateByPrimaryKey(EcmVipUserInfo record);
}