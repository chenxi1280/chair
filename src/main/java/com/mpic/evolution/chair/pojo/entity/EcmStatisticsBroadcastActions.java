package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_statistics_broadcast_actions
 * @author 
 */
@Data
public class EcmStatisticsBroadcastActions implements Serializable {
    /**
     * 用户播放动作表
     */
    private Integer pkId;

    /**
     * exit等
     */
    private String actType;

    /**
     * 动作数值
     */
    private String actValue;

    private Integer fkArtworkId;

    private Integer fkArtworkDetailId;

    private Integer fkUserId;

    private Date startTime;

    /**
     * 当前播放花四溅
     */
    private Integer currentPlayTime;

    private static final long serialVersionUID = 1L;
}