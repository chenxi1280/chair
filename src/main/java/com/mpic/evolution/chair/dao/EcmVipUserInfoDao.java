package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmVipUserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface EcmVipUserInfoDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmVipUserInfo record);

    int insertSelective(EcmVipUserInfo record);

    EcmVipUserInfo selectByPrimaryKey(Integer pkId);

    List<EcmVipUserInfo> selectByUserInfo(EcmVipUserInfo record);

    int updateByPrimaryKeySelective(EcmVipUserInfo record);

    int updateByPrimaryKey(EcmVipUserInfo record);

    List<EcmVipUserInfo> selectByUserId(@Param("fkUserId") Integer fkUserId,@Param("vipEndTime") Date vipEndTime );
}
