package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmDownlinkFlowHistory;

public interface EcmDownlinkFlowHistoryDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmDownlinkFlowHistory record);

    int insertSelective(EcmDownlinkFlowHistory record);

    EcmDownlinkFlowHistory selectByPrimaryKey(Integer pkId);

    EcmDownlinkFlowHistory selectByRecord(EcmDownlinkFlowHistory record);

    int updateByPrimaryKeySelective(EcmDownlinkFlowHistory record);

    int updateByPrimaryKey(EcmDownlinkFlowHistory record);
}