package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkComment;

public interface EcmArtworkCommentDao {
    int deleteByPrimaryKey(Integer pkCommentId);

    int insert(EcmArtworkComment record);

    int insertSelective(EcmArtworkComment record);

    EcmArtworkComment selectByPrimaryKey(Integer pkCommentId);

    int updateByPrimaryKeySelective(EcmArtworkComment record);

    int updateByPrimaryKey(EcmArtworkComment record);
}