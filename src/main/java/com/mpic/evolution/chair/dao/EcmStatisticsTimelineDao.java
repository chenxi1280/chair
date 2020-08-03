package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmStatisticsTimeline;

public interface EcmStatisticsTimelineDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmStatisticsTimeline record);

    int insertSelective(EcmStatisticsTimeline record);

    EcmStatisticsTimeline selectByPrimaryKey(Integer pkId);

    int updateByPrimaryKeySelective(EcmStatisticsTimeline record);

    int updateByPrimaryKey(EcmStatisticsTimeline record);
}