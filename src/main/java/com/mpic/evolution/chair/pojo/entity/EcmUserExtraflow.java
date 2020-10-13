package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_user_extraflow
 * @author 
 */
@Data
public class EcmUserExtraflow implements Serializable {
    private Integer extraflowId;

    /**
     * 用户id
     */
    private Integer fkUserId;

    /**
     * 加油包状态 0停用 1可用
     */
    private Short extraflowStatus;

    /**
     * 加油包起始时间
     */
    private Date extraflowStartTime;

    /**
     * 加油包截止时间
     */
    private Date extraflowEndTime;

    /**
     * 记录更新时间
     */
    private Date extraflowUpdateTime;

    private static final long serialVersionUID = 1L;
}