package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmVideoTemporaryStorage;
import com.mpic.evolution.chair.pojo.vo.EcmVideoTemporaryStorageVO;

import java.util.List;

public interface EcmVideoTemporaryStorageDao {
    int deleteByPrimaryKey(Integer pkVideoId);

    int insert(EcmVideoTemporaryStorage record);

    int insertSelective(EcmVideoTemporaryStorage record);

    EcmVideoTemporaryStorage selectByPrimaryKey(Integer pkVideoId);

    int updateByPrimaryKeySelective(EcmVideoTemporaryStorage record);

    int updateByPrimaryKey(EcmVideoTemporaryStorage record);

    List<EcmVideoTemporaryStorageVO> selectByFkArtworkId(Integer fkArtworkId);
}