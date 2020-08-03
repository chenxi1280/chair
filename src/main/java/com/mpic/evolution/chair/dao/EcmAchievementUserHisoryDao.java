package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmAchievementUserHisory;

public interface EcmAchievementUserHisoryDao {
    int deleteByPrimaryKey(Integer pkAuHistoryId);

    int insert(EcmAchievementUserHisory record);

    int insertSelective(EcmAchievementUserHisory record);

    EcmAchievementUserHisory selectByPrimaryKey(Integer pkAuHistoryId);

    int updateByPrimaryKeySelective(EcmAchievementUserHisory record);

    int updateByPrimaryKey(EcmAchievementUserHisory record);
}