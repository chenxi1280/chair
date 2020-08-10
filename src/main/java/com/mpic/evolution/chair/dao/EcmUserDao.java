package com.mpic.evolution.chair.dao;

import org.apache.ibatis.annotations.Param;

import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;

public interface EcmUserDao {
    int deleteByPrimaryKey(Integer pkUserId);

    int insert(EcmUser record);

    int insertSelective(EcmUser record);

    EcmUser selectByPrimaryKey(Integer pkUserId);

    int updateByPrimaryKeySelective(EcmUser record);

    int updateByPrimaryKey(EcmUser record);
	
    EcmUser selectByRecord(EcmUser record);
    
    int updateEcmUser(@Param("record") EcmUser record,@Param("recordVo") EcmUserVo ecmUserVo);

}