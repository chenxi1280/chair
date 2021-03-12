package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmUserHistoryFlow;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

public interface EcmUserHistoryFlowDao {
    int deleteByPrimaryKey(Integer flowHistoryId);

    int insert(EcmUserHistoryFlow record);

    int insertSelective(EcmUserHistoryFlow record);

    EcmUserHistoryFlow selectByPrimaryKey(Integer flowHistoryId);

    List<EcmUserHistoryFlow> selectByVipTimeZone(@Param("startDate") String startDate,
                                                 @Param("endDate") String endDate,
                                                 @Param("fkUserId") Integer fkUserId );

    int updateByPrimaryKeySelective(EcmUserHistoryFlow record);

    int updateByPrimaryKey(EcmUserHistoryFlow record);
}