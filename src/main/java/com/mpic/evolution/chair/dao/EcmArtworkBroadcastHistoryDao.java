package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHistory;
import com.mpic.evolution.chair.pojo.query.EcmUserHistoryFlowQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkFreeAdVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EcmArtworkBroadcastHistoryDao {
    int deleteByPrimaryKey(Integer pkBroadcastId);

    int insert(EcmArtworkBroadcastHistory record);

    int insertSelective(EcmArtworkBroadcastHistory record);

    EcmArtworkBroadcastHistory selectByPrimaryKey(Integer pkBroadcastId);

    List<EcmArtworkBroadcastHistory> selectByRecord(@Param("record") EcmArtworkBroadcastHistory record,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<EcmArtworkBroadcastHistory> selectByPageQuery(@Param("record") EcmUserHistoryFlowQuery record, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int selectArtWorkHistoryCount(@Param("artworks") List<Integer> artworks, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int updateByPrimaryKeySelective(EcmArtworkBroadcastHistory record);

    int updateByPrimaryKey(EcmArtworkBroadcastHistory record);
}