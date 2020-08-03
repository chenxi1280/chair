package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_achievement
 * @author 
 */
@Data
public class EcmAchievement implements Serializable {
    /**
     * 成就表
     */
    private Integer achievementId;

    /**
     * 成就标题
     */
    private String achievementTitle;

    /**
     * 成就描述
     */
    private String achievementContext;

    /**
     * 成就图片路径
     */
    private String achievementImageurl;

    /**
     * 详情外键id
     */
    private Integer fkDetailId;

    /**
     * 作品外键id
     */
    private Integer fkArtworkId;

    private static final long serialVersionUID = 1L;
}