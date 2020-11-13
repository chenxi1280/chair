package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmUserFlow;
import com.mpic.evolution.chair.pojo.vo.EcmUserFlowVO;

public interface EcmUserFlowDao {
    int deleteByPrimaryKey(Integer userFlowId);

    int insert(EcmUserFlow record);

    int insertSelective(EcmUserFlow record);

    EcmUserFlow selectByPrimaryKey(Integer userFlowId);

    int updateByPrimaryKeySelective(EcmUserFlow record);

    int updateByPrimaryKey(EcmUserFlow record);

    EcmUserFlowVO selectByPkUserId(Integer pkUserId);

    int updateReduceFlow(EcmUserFlow ecmUserFlow);
}