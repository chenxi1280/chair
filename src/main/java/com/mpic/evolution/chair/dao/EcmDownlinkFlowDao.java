package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmDownlinkFlow;

public interface EcmDownlinkFlowDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmDownlinkFlow record);

    int insertSelective(EcmDownlinkFlow record);

    EcmDownlinkFlow selectByPrimaryKey(Integer pkId);

    EcmDownlinkFlow selectByRecord(EcmDownlinkFlow record);

    int updateByPrimaryKeySelective(EcmDownlinkFlow record);

    int updateByPrimaryKey(EcmDownlinkFlow record);
}