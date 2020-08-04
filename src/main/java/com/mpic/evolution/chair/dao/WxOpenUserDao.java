package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.WxOpenUser;

public interface WxOpenUserDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(WxOpenUser record);

    int insertSelective(WxOpenUser record);

    WxOpenUser selectByPrimaryKey(Integer pkId);

    int updateByPrimaryKeySelective(WxOpenUser record);

    int updateByPrimaryKey(WxOpenUser record);
}
