package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_achievement_user_hisory
 * @author 
 */
@Data
public class EcmAchievementUserHisory implements Serializable {
    /**
     * 成就与用户关联表
     */
    private Integer pkAuHistoryId;

    private Integer fkAchievementId;

    private Integer fkUserId;

    private Date achieveDate;

    private Integer fkArtworkId;

    private static final long serialVersionUID = 1L;
}