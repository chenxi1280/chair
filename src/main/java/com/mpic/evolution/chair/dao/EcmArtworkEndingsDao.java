package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkEndings;
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

    void insertSelectiveList(@Param("list") List<EcmArtworkEndingsVO> ecmArtworkEndingsVOS);

    int deleteByArtwork(int fkArtwork);

}
