package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmUserNoticeHistory;

public interface EcmUserNoticeHistoryDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmUserNoticeHistory record);

    int insertSelective(EcmUserNoticeHistory record);

    EcmUserNoticeHistory selectByPrimaryKey(Integer pkId);

    int updateByPrimaryKeySelective(EcmUserNoticeHistory record);

    int updateByPrimaryKey(EcmUserNoticeHistory record);
}