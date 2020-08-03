package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_artwork_liked_history
 * @author 
 */
@Data
public class EcmArtworkLikedHistory implements Serializable {
    /**
     * 作品喜欢历史表
     */
    private Integer pkLikedId;

    /**
     * 作品id
     */
    private Integer fkArtworkId;

    private Integer fkUserId;

    /**
     * 0未喜欢，1已喜欢，2已取消
     */
    private Short likedStatus;

    private Date createdAt;

    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}