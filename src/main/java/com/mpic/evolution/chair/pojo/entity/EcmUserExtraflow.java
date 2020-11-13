package com.mpic.evolution.chair.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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
     * 加油包购买时间
     */
    private Date extraflowCreateTime;

    /**
     * 记录更新时间
     */
    private Date extraflowUpdateTime;

    private static final long serialVersionUID = 1L;
}