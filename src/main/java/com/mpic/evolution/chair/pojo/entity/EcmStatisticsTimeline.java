package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_statistics_timeline
 * @author 
 */
@Data
public class EcmStatisticsTimeline implements Serializable {
    /**
     * 点击故事线统计表
     */
    private Integer pkId;

    /**
     * 0主动1被动
     */
    private Boolean isActive;

    private Integer fkArtworkId;

    private Integer fkArtworkDetailId;

    private Integer fkUserId;

    private Date startTime;

    /**
     * 播放了多少秒
     */
    private Integer currentPlayTime;

    private static final long serialVersionUID = 1L;
}