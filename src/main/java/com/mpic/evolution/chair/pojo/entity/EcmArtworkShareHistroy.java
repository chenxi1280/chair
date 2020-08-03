package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_artwork_share_histroy
 * @author 
 */
@Data
public class EcmArtworkShareHistroy implements Serializable {
    /**
     * 作品分享历史表
     */
    private Integer pkSharedId;

    private Integer fkArtworkId;

    private Integer fkUserId;

    private Date sharedDate;

    /**
     * 0未知，1成功，2主动取消，3失败
     */
    private Short sharedResult;

    private static final long serialVersionUID = 1L;
}