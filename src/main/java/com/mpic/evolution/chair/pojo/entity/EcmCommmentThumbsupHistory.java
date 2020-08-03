package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_commment_thumbsup_history
 * @author 
 */
@Data
public class EcmCommmentThumbsupHistory implements Serializable {
    /**
     * 评论点赞表
     */
    private Integer pkThumbsupId;

    /**
     * 评论id
     */
    private Integer fkCommentId;

    private Integer fkUserId;

    /**
     * 0有效，1无效
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}