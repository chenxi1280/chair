package com.mpic.evolution.chair.dao;

import org.apache.ibatis.annotations.Param;

import com.mpic.evolution.chair.pojo.entity.EcmUser;

public interface EcmUserDao {
    int deleteByPrimaryKey(Integer pkUserId);

    int insert(EcmUser record);

    int insertSelective(EcmUser record);

    EcmUser selectByPrimaryKey(Integer pkUserId);

    int updateByPrimaryKeySelective(EcmUser record);

    int updateByPrimaryKey(EcmUser record);
	
    EcmUser selectByRecord(EcmUser record);
    
    int updateTokenByEmail(@Param("record") EcmUser record, String email);
    
    int updatePwdByToken(@Param("record") EcmUser record, String token);

	void updateByMobile(@Param("record") EcmUser record, String mobile);
 
	void updateIsvalidByToken(@Param("record") EcmUser record, String token);

}