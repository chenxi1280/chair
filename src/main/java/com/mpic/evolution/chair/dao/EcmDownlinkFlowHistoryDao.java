package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmDownlinkFlowHistory;
import org.apache.ibatis.annotations.Param;

public interface EcmDownlinkFlowHistoryDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmDownlinkFlowHistory record);

    int insertSelective(EcmDownlinkFlowHistory record);

    EcmDownlinkFlowHistory selectByPrimaryKey(Integer pkId);

    EcmDownlinkFlowHistory selectByRecord(EcmDownlinkFlowHistory record);

    int updateByPrimaryKeySelective(EcmDownlinkFlowHistory record);

    int updateBySelective(@Param("param1") EcmDownlinkFlowHistory record1, @Param("param2") EcmDownlinkFlowHistory record2);

    int updateByPrimaryKey(EcmDownlinkFlowHistory record);
}