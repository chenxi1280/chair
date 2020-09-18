package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;

import java.util.List;

import com.mpic.evolution.chair.pojo.query.EcmInnerMessageQurey;
import com.mpic.evolution.chair.pojo.vo.EcmInnerMessageVo;
import org.apache.ibatis.annotations.Param;

public interface EcmInnerMessageDao {
    int deleteByPrimaryKey(Integer pkMessageId);

    int insert(EcmInnerMessage record);

    int insertSelective(EcmInnerMessage record);

    EcmInnerMessage selectByPrimaryKey(Integer pkMessageId);
    
    List<EcmInnerMessage> selectByList(EcmInnerMessage ecmInnerMessage);
    
    int updateByPrimaryKeySelective(EcmInnerMessage record);

    int updateByPrimaryKey(EcmInnerMessage record);
    
    int batchUpdate(@Param("record") EcmInnerMessage record, List<Integer> ids);


    List<EcmInnerMessageVo> selectByMsgList(EcmInnerMessage ecmInnerMessage);


    Integer updateRedaMsg(@Param("ids") List<Integer> messageIds, @Param("uid") Integer pkUserId);

    Integer updateDelMsg(@Param("ids") List<Integer> messageIds, @Param("uid") Integer pkUserId);

    EcmInnerMessageVo getUserMsgByMsgId(Integer pkMessageId);
}