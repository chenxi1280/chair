package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkEndings;
import com.mpic.evolution.chair.pojo.query.EcmArtworkEndingsQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkEndingsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EcmArtworkEndingsDao {
    int deleteByPrimaryKey(Integer pkEndingsId);

    int insert(EcmArtworkEndings record);

    int insertSelective(EcmArtworkEndings record);

    EcmArtworkEndings selectByPrimaryKey(Integer pkEndingsId);

    int updateByPrimaryKeySelective(EcmArtworkEndings record);

    int updateByPrimaryKey(EcmArtworkEndings record);

    int insertSelectiveList(@Param("list") List<EcmArtworkEndingsVO> list);

    int deleteByArtwork(int fkArtwork);

    List<EcmArtworkEndingsVO> selectByArtwId(Integer pkArtworkId);

    List<EcmArtworkEndingsVO> selectByEndingList( @Param("list") List<EcmArtworkEndingsVO> list);

    int updateSelectiveList( @Param("list") List<EcmArtworkEndingsVO> list);

    List<EcmArtworkEndingsVO> selectEcmArtworkEndingsQuery(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);

    Integer selectCountEcmArtworkEndingsQuery(Integer ecmArtworkEndingsQuery);

    Integer selectCountEcmArtworkId(int fkArtworkId);

    List<EcmArtworkEndingsVO> selectByPrimaryKeyList(@Param("list") List<Integer> deleteEndingId);

    int deleteByPrimaryList(@Param("list")List<EcmArtworkEndingsVO> ecmArtworkEndings);

    EcmArtworkEndingsVO selectByNodeId(Integer fkNodeId);

    int updatePopupStateByPrimaryKey(EcmArtworkEndingsVO ecmArtworkEndingsVO);

    int updateConditionByNodeId(EcmArtworkEndings ecmArtworkEndings);

    int updateConditionByArtworkId(EcmArtworkEndings ecmArtworkEndings);
}
