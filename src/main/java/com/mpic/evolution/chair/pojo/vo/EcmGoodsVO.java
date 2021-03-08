package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmGoods;
import lombok.Data;

/**
 * @author by cxd
 * @Classname EcmGoodsVO
 * @Description TODO
 * @Date 2021/3/8 18:19
 */
@Data
public class EcmGoodsVO extends EcmGoods {
    /**
     * 类目名称
     */
    private String categoryName;

}
