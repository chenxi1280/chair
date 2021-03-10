package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ecm_goods
 * @author
 */
@Data
public class EcmGoods implements Serializable {
    /**
     * 商品id
     */
    private Integer pkGoodsId;

    /**
     * 商品类目表
     */
    private Integer fkGoodsCategoryId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品状态
     */
    private Integer goodsState;

    /**
     * 排序
     */
    private Integer goodsSort;

    /**
     * 商品类型
     */
    private String goodsType;

    /**
     * 类型对应的数量
     */
    private Integer goodsActionNumber;

    /**
     * 说明
     */
    private String goodsContext;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
