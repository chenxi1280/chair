package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmVipUserInfo;

public interface EcmVipUserInfoDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmVipUserInfo record);

    int insertSelective(EcmVipUserInfo record);

    EcmVipUserInfo selectByPrimaryKey(Integer pkId);

    EcmVipUserInfo selectByUserInfo(EcmVipUserInfo record);

    int updateByPrimaryKeySelective(EcmVipUserInfo record);

    int updateByPrimaryKey(EcmVipUserInfo record);
}