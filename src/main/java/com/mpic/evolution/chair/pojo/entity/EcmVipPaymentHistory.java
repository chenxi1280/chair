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
    private Integer vipPaymentStatus;

    /**
     * 用户购买的vip类型 0普通会员 1超级会员
     */
    private Integer fkVipRoleId;

    /**
     * 支付时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}