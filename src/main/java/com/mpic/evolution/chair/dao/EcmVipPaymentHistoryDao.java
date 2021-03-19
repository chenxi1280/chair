package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmVipPaymentHistory;

public interface EcmVipPaymentHistoryDao {
    int deleteByPrimaryKey(Integer pkId);

    int insert(EcmVipPaymentHistory record);

    int insertSelective(EcmVipPaymentHistory record);

    EcmVipPaymentHistory selectByPrimaryKey(Integer pkId);

    int updateByPrimaryKeySelective(EcmVipPaymentHistory record);

    int updateByPrimaryKey(EcmVipPaymentHistory record);
}