package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkFreeAd;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkFreeAdVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EcmArtworkFreeAdDao {
    int deleteByPrimaryKey(Integer pkEcmArtworkFreeAdId);

    int insert(EcmArtworkFreeAd record);

    int insertSelective(EcmArtworkFreeAd record);

    EcmArtworkFreeAd selectByPrimaryKey(Integer pkEcmArtworkFreeAdId);

    int updateByPrimaryKeySelective(EcmArtworkFreeAd record);

    EcmArtworkFreeAd selectByRecord(EcmArtworkFreeAd record);

    List<EcmArtworkFreeAdVO> selectFreeAdArtwork(@Param("artworks") List<Integer> artworks);

    int updateByPrimaryKey(EcmArtworkFreeAd record);
}