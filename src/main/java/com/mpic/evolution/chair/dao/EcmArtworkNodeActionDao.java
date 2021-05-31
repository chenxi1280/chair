package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeAction;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodeActionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EcmArtworkNodeActionDao {
    int deleteByPrimaryKey(Integer pkActionId);

    int insert(EcmArtworkNodeAction record);

    int insertSelective(EcmArtworkNodeAction record);

    EcmArtworkNodeAction selectByPrimaryKey(Integer pkActionId);

    int updateByPrimaryKeySelective(EcmArtworkNodeAction record);

    int updateByPrimaryKey(EcmArtworkNodeAction record);

    List<EcmArtworkNodeActionVO> selectByFkArtworkId(Integer fkArtworkId);

    int insertSelectiveList(@Param("list") List<EcmArtworkNodeAction> ecmArtworkNodeActionList);

    int updateByPrimaryKeySelectiveList(@Param("list") List<EcmArtworkNodeAction> upDataArtworkNodeActions);

    List<EcmArtworkNodeActionVO> selectByEcmNodeIdList(@Param("list") List<Integer> fkNodeIdList);

    int insertSelectiveVOList(@Param("list") List<EcmArtworkNodeActionVO> ecmArtworkNodeActionVOS);
}
