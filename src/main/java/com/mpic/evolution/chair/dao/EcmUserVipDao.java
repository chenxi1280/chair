package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmUserVip;

public interface EcmUserVipDao {
    int deleteByPrimaryKey(Integer vipId);

    int insert(EcmUserVip record);

    int insertSelective(EcmUserVip record);

    EcmUserVip selectByPrimaryKey(Integer vipId);
    
    EcmUserVip selectByVipUser(EcmUserVip record);

    int updateByPrimaryKeySelective(EcmUserVip record);

    int updateByPrimaryKey(EcmUserVip record);
}