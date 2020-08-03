package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_artwork_broadcast_history
 * @author 
 */
@Data
public class EcmArtworkBroadcastHistory implements Serializable {
    /**
     * 作品播放历史表,以空间换时间
     */
    private Integer pkBroadcastId;

    /**
     * 作品id
     */
    private Integer fkArtworkId;

    /**
     * 作品【详情】id
     */
    private Integer fkArtworkDetailId;

    /**
     * userId
     */
    private Integer fkUserId;

    /**
     * 作品观看时间
     */
    private Date startTime;

    private static final long serialVersionUID = 1L;
}