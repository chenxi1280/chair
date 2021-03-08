package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ecm_order
 * @author
 */
@Data
public class EcmOrder implements Serializable {
    /**
     * 订单主键
     */
    private Integer pkOrderId;

    /**
     * 订单code
     */
    private String orderCode;

    /**
     * 订单类型 0 wx  1支付宝
     */
    private Integer orderType;

    /**
     * 订单金额
     */
    private BigDecimal orderPrice;

    /**
     * 订单状态 0 未支付 1 已完成 2 完成
     */
    private Integer orderState;

    /**
     * 商品数量
     */
    private Integer orderGoodsNumber;

    /**
     * 商品id
     */
    private Integer fkGoodsId;

    /**
     * 用户id
     */
    private Integer fkUserId;

    /**
     * 支付订单号
     */
    private String payOrderId;

    /**
     * 创建时间
     */
    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
