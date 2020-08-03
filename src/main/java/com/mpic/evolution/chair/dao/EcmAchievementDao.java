package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmAchievement;

public interface EcmAchievementDao {
    int deleteByPrimaryKey(Integer achievementId);

    int insert(EcmAchievement record);

    int insertSelective(EcmAchievement record);

    EcmAchievement selectByPrimaryKey(Integer achievementId);

    int updateByPrimaryKeySelective(EcmAchievement record);

    int updateByPrimaryKey(EcmAchievement record);
}