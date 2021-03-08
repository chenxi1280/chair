package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmGoodsCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface EcmGoodsCategoryDao {
    int deleteByPrimaryKey(Integer pkCategoryId);

    int insert(EcmGoodsCategory record);

    int insertSelective(EcmGoodsCategory record);

    EcmGoodsCategory selectByPrimaryKey(Integer pkCategoryId);

    int updateByPrimaryKeySelective(EcmGoodsCategory record);

    int updateByPrimaryKey(EcmGoodsCategory record);
}
