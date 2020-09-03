package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHot;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkBroadcastHotVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EcmArtworkBroadcastHotDao {
    int deleteByPrimaryKey(Integer pkBroadcastHotId);

    int insert(EcmArtworkBroadcastHot record);

    int insertSelective(EcmArtworkBroadcastHot record);

    EcmArtworkBroadcastHot selectByPrimaryKey(Integer pkBroadcastHotId);

    int updateByPrimaryKeySelective(EcmArtworkBroadcastHot record);

    int updateByPrimaryKey(EcmArtworkBroadcastHot record);

    List<EcmArtworkBroadcastHotVO> selectFindAll(EcmArtWorkQuery ecmArtWorkQuery);

    List<EcmArtworkBroadcastHotVO> selectEcmArtworkList(@Param("ids") List<EcmArtworkVo> list);
}