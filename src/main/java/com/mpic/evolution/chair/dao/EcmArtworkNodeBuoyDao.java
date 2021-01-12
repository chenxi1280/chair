package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeBuoy;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodeBuoyVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EcmArtworkNodeBuoyDao {
    int deleteByPrimaryKey(Integer pkBuoyId);

    int insert(EcmArtworkNodeBuoy record);

    int insertSelective(EcmArtworkNodeBuoy record);

    EcmArtworkNodeBuoy selectByPrimaryKey(Integer pkBuoyId);

    int updateByPrimaryKeySelective(EcmArtworkNodeBuoy record);

    int updateByPrimaryKey(EcmArtworkNodeBuoy record);

    List<EcmArtworkNodeBuoyVO> selectByEcmArtworkId(Integer fkArtworkId);

    List<EcmArtworkNodeBuoyVO> selectByEcmNodeId(Integer fkNodeId);

    int deleteByEcmArtworkId(Integer fkArtworkId);

    int insertSelectiveList(@Param("list") List<EcmArtworkNodeBuoy> ecmArtworkNodeBuoyList);

    int updateByPrimaryKeySelectiveList(@Param("list")List<EcmArtworkNodeBuoyVO> ecmArtworkNodeBuoyVOList);

    List<EcmArtworkNodeBuoyVO> selectByEcmArtworkNodeList(@Param("list")List<EcmArtworkNodesVo> filterBuoyList);
}
