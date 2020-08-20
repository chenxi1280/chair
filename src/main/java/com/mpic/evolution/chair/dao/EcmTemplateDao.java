package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmTemplate;

public interface EcmTemplateDao {
    int deleteByPrimaryKey(Integer pkTemplateId);

    int insert(EcmTemplate record);

    int insertSelective(EcmTemplate record);

    EcmTemplate selectByPrimaryKey(Integer pkTemplateId);

    int updateByPrimaryKeySelective(EcmTemplate record);

    int updateByPrimaryKey(EcmTemplate record);
}