package com.mpic.evolution.chair.dao;

import java.util.List;

import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;

public interface EcmInnerMessageDao {
    int deleteByPrimaryKey(Integer pkMessageId);

    int insert(EcmInnerMessage record);

    int insertSelective(EcmInnerMessage record);

    EcmInnerMessage selectByPrimaryKey(Integer pkMessageId);
    
    List<EcmInnerMessage> selectByList(EcmInnerMessage ecmInnerMessage);

    int updateByPrimaryKeySelective(EcmInnerMessage record);

    int updateByPrimaryKey(EcmInnerMessage record);
}