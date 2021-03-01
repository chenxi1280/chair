package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmMerchant;
import com.mpic.evolution.chair.pojo.vo.EcmMerchantVO;
import org.springframework.stereotype.Repository;

@Repository
public interface EcmMerchantDao {
    int deleteByPrimaryKey(Integer pkMerchantId);

    int insert(EcmMerchant record);

    int insertSelective(EcmMerchant record);

    EcmMerchant selectByPrimaryKey(Integer pkMerchantId);

    int updateByPrimaryKeySelective(EcmMerchant record);

    int updateByPrimaryKey(EcmMerchant record);

    EcmMerchantVO selectByUserId(Integer fkUserid);
}
