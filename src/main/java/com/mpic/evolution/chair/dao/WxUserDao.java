package com.mpic.evolution.chair.dao;

import org.apache.ibatis.annotations.Param;

import com.mpic.evolution.chair.pojo.entity.WxUser;

public interface WxUserDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(WxUser record);

    int insertSelective(WxUser record);

    WxUser selectByPrimaryKey(Integer pkId);
    
    WxUser selectByWxUser(WxUser record);
    
    int updateByPrimaryKeySelective(WxUser record);

    int updateByPrimaryKey(WxUser record);
    
    int updateByWxUser(@Param("w1") WxUser wxUser1,@Param("w2") WxUser wxUser2);
}