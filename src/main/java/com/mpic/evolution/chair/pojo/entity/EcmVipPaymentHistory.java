package com.mpic.evolution.chair.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ecm_vip_payment_history
 * @author 
 */
@Data
public class EcmVipPaymentHistory implements Serializable {
    private Integer pkId;

    /**
     * 用户id
     */
    private Integer fkUserid;

    /**
     * 支付状态 0待支付 1已完成 2已支付 3已超时 4已失败
     */
    private String vipType;

    /**
     * 支付时间
     */
    private Date createTime;

    /**
     * vip购买月
     */
    private Integer vipMonth;

    private static final long serialVersionUID = 1L;
}