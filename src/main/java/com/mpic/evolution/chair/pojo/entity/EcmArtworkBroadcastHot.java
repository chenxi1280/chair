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

    private static final long serialVersionUID = 1L;
}