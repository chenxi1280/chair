package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmDownlinkFlow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EcmDownlinkFlowDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmDownlinkFlow record);

    int insertSelective(EcmDownlinkFlow record);

    EcmDownlinkFlow selectByPrimaryKey(Integer pkId);

    EcmDownlinkFlow selectByRecord(EcmDownlinkFlow record);

    int updateByPrimaryKeySelective(EcmDownlinkFlow record);

    int updateBySelective(@Param("param1") EcmDownlinkFlow record1, @Param("param2") EcmDownlinkFlow record2);

    int updateByPrimaryKey(EcmDownlinkFlow record);

    List<Integer>  selectByUserIds(List<Integer> userIds);
}