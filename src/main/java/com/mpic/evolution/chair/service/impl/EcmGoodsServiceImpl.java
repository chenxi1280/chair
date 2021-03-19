package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.dao.EcmGoodsCategoryDao;
import com.mpic.evolution.chair.dao.EcmGoodsDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmGoods;
import com.mpic.evolution.chair.pojo.query.EcmGoodsQuery;
import com.mpic.evolution.chair.pojo.vo.EcmGoodsVO;
import com.mpic.evolution.chair.service.EcmGoodsService;
import org.springframework.stereotype.Service;

import java.util.*;
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
        Set<String> strings = collect.keySet();
        HashMap<Integer, Object> integerComparatorHashMap = new HashMap<>(2);
        integerComparatorHashMap.put(0,strings);
        integerComparatorHashMap.put(1,collect);
//        Map<Integer, List<EcmGoodsVO>> collect = ecmGoodsVOList.stream().collect(Collectors.groupingBy(EcmGoodsVO::getFkGoodsCategoryId));

        return ResponseDTO.ok(integerComparatorHashMap);
    }

    @Override
    public EcmGoods getGoodsVOByPkId(Integer id) {

        return ecmGoodsDao.selectByPrimaryKey(id);
    }
}
