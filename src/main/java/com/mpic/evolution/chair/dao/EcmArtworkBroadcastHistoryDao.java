package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EcmArtworkBroadcastHistoryDao {
    int deleteByPrimaryKey(Integer pkBroadcastId);

    int insert(EcmArtworkBroadcastHistory record);

    int insertSelective(EcmArtworkBroadcastHistory record);

    EcmArtworkBroadcastHistory selectByPrimaryKey(Integer pkBroadcastId);

    List<EcmArtworkBroadcastHistory> selectByRecord(@Param("record") EcmArtworkBroadcastHistory record,@Param("startTime") String startTime,@Param("endTime") String endTime);

    int updateByPrimaryKeySelective(EcmArtworkBroadcastHistory record);

    int updateByPrimaryKey(EcmArtworkBroadcastHistory record);
}