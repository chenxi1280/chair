package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.entity.EcmGoods;
import com.mpic.evolution.chair.pojo.query.EcmGoodsQuery;
import com.mpic.evolution.chair.pojo.vo.EcmGoodsVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcmGoodsDao {
    int deleteByPrimaryKey(Integer pkGoodsId);

    int insert(EcmGoods record);

    int insertSelective(EcmGoods record);

    EcmGoods selectByPrimaryKey(Integer pkGoodsId);

    int updateByPrimaryKeySelective(EcmGoods record);

    int updateByPrimaryKey(EcmGoods record);

    List<EcmGoodsVO> selectListByEcmGoodsQuery(EcmGoodsQuery ecmGoodsQuery);
}
