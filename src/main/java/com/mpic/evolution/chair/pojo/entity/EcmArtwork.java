package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_artwork
 * @author 
 */
@Data
public class EcmArtwork implements Serializable {
    /**
     * 作品表
     */
    private Integer pkArtworkId;

    /**
     * 作者
     */
    private Integer fkUserid;

    private String artworkName;

    /**
     * 作品描述
     */
    private String artworkDescribe;

    /**
     * 状态，0草稿，1待审核，2已发布审核通过，3审核不通过禁封，5被删除
     */
    private Short artworkStatus;

    /**
     * 作品封面存储位置，绝对路径
     */
    private String logoPath;

    private Date lastCreateDate;

    private Date lastModifyDate;

    /**
     * 四字标签
     */
    private String fourLetterTips;

    private static final long serialVersionUID = 1L;
}