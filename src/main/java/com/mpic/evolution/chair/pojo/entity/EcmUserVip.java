package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_user_vip
 * @author 
 */
@Data
public class EcmUserVip implements Serializable {
    private Integer vipId;

    /**
     * 用户id
     */
    private Integer fkUserId;

    /**
     * 状态，0停用 1在用
     */
    private Short vipStatus;

    /**
     * vip购买时间
     */
    private Date vipCreateTime;

    /**
     * vip记录更新时间
     */
    private Date vipUpdateTime;

    /**
     * vip起始时间
     */
    private Date vipStartTime;

    /**
     * vip截止时间
     */
    private Date vipEndTime;

    private static final long serialVersionUID = 1L;
}