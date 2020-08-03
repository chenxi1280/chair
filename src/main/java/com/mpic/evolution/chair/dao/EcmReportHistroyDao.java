package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmReportHistroy;

public interface EcmReportHistroyDao {
    int deleteByPrimaryKey(Integer reportid);

    int insert(EcmReportHistroy record);

    int insertSelective(EcmReportHistroy record);

    EcmReportHistroy selectByPrimaryKey(Integer reportid);

    int updateByPrimaryKeySelective(EcmReportHistroy record);

    int updateByPrimaryKey(EcmReportHistroy record);
}