package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_broadcast_hot
 * @author 
 */
@Data
public class EcmArtworkBroadcastHot implements Serializable {
    /**
     * 主键
     */
    private Integer pkBroadcastHotId;

    /**
     * 作品id
     */
    private Integer fkArkworkId;

    /**
     * 播放次数
     */
    private Integer broadcastCount;

    /**
     * 昨天等待累加的次数
     */
    private Integer waitCount;

    private static final long serialVersionUID = 1L;
}