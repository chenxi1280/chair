package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_user_flow
 * @author 
 */
@Data
public class EcmUserFlow implements Serializable {
    /**
     * 主键
     */
    private Integer userFlowId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 剩余流量（单位KB）
     */
    private Integer surplusFlow;

    /**
     * 总流量（单位KB）
     */
    private Integer totalFlow;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户使用的总流量（单位KB）
     */
    private Integer checkFlow;

    /**
     * 用户已使用流量
     */
    private Integer usedFlow;

    private static final long serialVersionUID = 1L;
}