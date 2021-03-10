package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ecm_order_history
 * @author
 */
@Data
public class EcmOrderHistory implements Serializable {
    /**
     * 订单历史id
     */
    private Integer pkOrderHistoryId;

    /**
     * 支付订单id
     */
    private String payOrderId;

    /**
     * 支付订单金额
     */
    private BigDecimal payOrderPrice;

    /**
     * 订单支付类型
     */
    private Integer orderType;

    /**
     * 订单code
     */
    private String orderCode;

    /**
     * 订单id
     */
    private Integer fkOrderId;

    /**
     * 商品名字
     */
    private String goodsName;

    /**
     * 商品id
     */
    private Integer fkGoodsId;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品动作数量
     */
    private Integer goodsActionNumber;

    /**
     * 商品类型
     */
    private String goodsType;

    private Integer fkUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
