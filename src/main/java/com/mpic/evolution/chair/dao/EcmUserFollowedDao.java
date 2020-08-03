package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmUserFollowed;

public interface EcmUserFollowedDao {
    int deleteByPrimaryKey(Integer pkUserFollowedId);

    int insert(EcmUserFollowed record);

    int insertSelective(EcmUserFollowed record);

    EcmUserFollowed selectByPrimaryKey(Integer pkUserFollowedId);

    int updateByPrimaryKeySelective(EcmUserFollowed record);

    int updateByPrimaryKey(EcmUserFollowed record);
}