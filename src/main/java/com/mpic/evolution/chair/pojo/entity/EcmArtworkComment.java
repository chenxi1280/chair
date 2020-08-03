package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_artwork_comment
 * @author 
 */
@Data
public class EcmArtworkComment implements Serializable {
    /**
     * 作品评论表
     */
    private Integer pkCommentId;

    private Integer fkArtworkId;

    private Integer fkUserId;

    private Date cmmentedDate;

    /**
     * 0待审核，1已审核通过，2被作者删除，3审核不通过，4其他
     */
    private Short commentStatus;

    /**
     * 作品评论
     */
    private String comment;

    private static final long serialVersionUID = 1L;
}