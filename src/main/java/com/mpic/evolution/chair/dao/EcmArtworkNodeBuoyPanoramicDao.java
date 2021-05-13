package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeBuoy;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeBuoyPanoramic;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodeBuoyPanoramicVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodeBuoyVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcmArtworkNodeBuoyPanoramicDao {
    int deleteByPrimaryKey(Integer pkBuoyId);

    int insert(EcmArtworkNodeBuoyPanoramic record);

    int insertSelective(EcmArtworkNodeBuoyPanoramic record);

    EcmArtworkNodeBuoyPanoramic selectByPrimaryKey(Integer pkBuoyId);

    int updateByPrimaryKeySelective(EcmArtworkNodeBuoyPanoramic record);

    int updateByPrimaryKey(EcmArtworkNodeBuoyPanoramic record);


    List<EcmArtworkNodeBuoyPanoramicVO> selectByEcmArtworkId(Integer fkArtworkId);

    List<EcmArtworkNodeBuoyPanoramicVO> selectByEcmNodeId(Integer fkNodeId);

    int deleteByEcmArtworkId(Integer fkArtworkId);

    int insertSelectiveList(@Param("list") List<EcmArtworkNodeBuoyPanoramic> ecmArtworkNodeBuoyList);

    int updateByPrimaryKeySelectiveList(@Param("list")List<EcmArtworkNodeBuoyPanoramic> ecmArtworkNodeBuoyVOList);

    List<EcmArtworkNodeBuoyPanoramicVO> selectByEcmArtworkNodeList(@Param("list")List<EcmArtworkNodesVo> filterBuoyList);

    List<EcmArtworkNodeBuoyPanoramicVO> selectByEcmNodeIdList(@Param("list")List<Integer> fkNodeIdList);

}
