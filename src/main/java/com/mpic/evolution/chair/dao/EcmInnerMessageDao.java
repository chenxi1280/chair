package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.dto.EcmInnerMessageDto;
import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;

import java.util.List;

public interface EcmInnerMessageDao {
    int deleteByPrimaryKey(Integer pkMessageId);

    int insert(EcmInnerMessage record);

    int insertSelective(EcmInnerMessage record);

    EcmInnerMessage selectByPrimaryKey(Integer pkMessageId);

    int updateByPrimaryKeySelective(EcmInnerMessage record);

    int updateByPrimaryKey(EcmInnerMessage record);

    List<EcmInnerMessageDto> selectByList(EcmInnerMessage ecmInnerMessage);
}