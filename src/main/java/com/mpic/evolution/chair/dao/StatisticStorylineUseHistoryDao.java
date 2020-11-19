package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.StatisticStorylineUseHistory;

public interface StatisticStorylineUseHistoryDao {
    int deleteByPrimaryKey(Integer statisticStorylineUseHistoryId);

    int insert(StatisticStorylineUseHistory record);

    int insertSelective(StatisticStorylineUseHistory record);

    StatisticStorylineUseHistory selectByPrimaryKey(Integer statisticStorylineUseHistoryId);

    int updateByPrimaryKeySelective(StatisticStorylineUseHistory record);

    int updateByPrimaryKey(StatisticStorylineUseHistory record);
}