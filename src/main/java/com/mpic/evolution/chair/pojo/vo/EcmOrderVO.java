package com.mpic.evolution.chair.pojo.vo;

import com.mpic.evolution.chair.pojo.entity.EcmOrder;
import lombok.Data;

/**
 * @author by cxd
 * @Classname EcmOrderVO
 * @Description TODO
 * @Date 2021/3/9 9:35
 */
@Data
public class EcmOrderVO extends EcmOrder {
    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品名称
     */
    private String totalFee;


    /**
     * 商品类别名称
     */
    private String categoryName;


    /**
     * 用户名
     */
    private String username;

}
