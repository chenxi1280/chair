package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EcmArtworkNodesDao {
    int deleteByPrimaryKey(Integer pkDetailId);

    int insert(EcmArtworkNodes record);

    int insertSelective(EcmArtworkNodes record);

    EcmArtworkNodes selectByPrimaryKey(Integer pkDetailId);

    int updateByPrimaryKeySelective(EcmArtworkNodes record);

    int updateByPrimaryKey(EcmArtworkNodes record);

    List<EcmArtworkNodesVo> selectByArtWorkId(Integer pkArtworkId);

    Integer insertList(@Param("list") List<EcmArtworkNodes> addlist);

    int removeByPrimaryKey(Integer pkDetailId);

    List<EcmArtworkNodesVo> selectByVideoCode(String fileId);

    int updateByEcmArtworkNodesList(List<EcmArtworkNodesVo> ecmArtworkNodesList);
}