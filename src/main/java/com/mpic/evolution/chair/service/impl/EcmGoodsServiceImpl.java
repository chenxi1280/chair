package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.dao.EcmGoodsCategoryDao;
import com.mpic.evolution.chair.dao.EcmGoodsDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmGoods;
import com.mpic.evolution.chair.pojo.query.EcmGoodsQuery;
import com.mpic.evolution.chair.pojo.vo.EcmGoodsVO;
import com.mpic.evolution.chair.service.EcmGoodsService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author by cxd
 * @Classname EcmGoodsServiceImpl
 * @Description TODO
 * @Date 2021/3/8 18:09
 */
@Service
public class EcmGoodsServiceImpl implements EcmGoodsService {

    final
    EcmGoodsDao ecmGoodsDao;
    EcmGoodsCategoryDao ecmGoodsCategoryDao;

    public EcmGoodsServiceImpl(EcmGoodsDao ecmGoodsDao, EcmGoodsCategoryDao ecmGoodsCategoryDao) {
        this.ecmGoodsDao = ecmGoodsDao;
        this.ecmGoodsCategoryDao = ecmGoodsCategoryDao;
    }

    @Override
    public ResponseDTO getGoodsAll(EcmGoodsQuery ecmGoodsQuery) {
        List<EcmGoodsVO> list = ecmGoodsDao.selectListByEcmGoodsQuery(ecmGoodsQuery);
        List<EcmGoodsVO> ecmGoodsVOList = list.stream().sorted(Comparator.comparing(EcmGoodsVO::getGoodsSort)).collect(Collectors.toList());
        Map<String, List<EcmGoodsVO>> collect = ecmGoodsVOList.stream().collect(Collectors.groupingBy(EcmGoodsVO::getCategoryName));
        return ResponseDTO.ok(collect);
    }

    @Override
    public EcmGoods getGoodsVOByPkId(Integer id) {

        return ecmGoodsDao.selectByPrimaryKey(id);
    }
}
