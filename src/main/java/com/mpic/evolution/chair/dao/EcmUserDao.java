package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import org.apache.ibatis.annotations.Param;

import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;

import java.util.List;

public interface EcmUserDao {
    int deleteByPrimaryKey(Integer pkUserId);

    int insert(EcmUser record);

    int insertSelective(EcmUser record);

    EcmUser selectByPrimaryKey(Integer pkUserId);

    int updateByPrimaryKeySelective(EcmUser record);

    int updateByPrimaryKey(EcmUser record);
	
    EcmUser selectByRecord(EcmUser record);
    
    int updateEcmUser(@Param("record") EcmUser record,@Param("recordVo") EcmUserVo ecmUserVo);

    List<EcmUserVo> selectUserByEcmArtworkList(@Param("ids") List<EcmArtworkVo> list);
}