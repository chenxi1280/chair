package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

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
     * 	播放端用于分享的图片的url
     */
    private String artworkDescribe;

    /**
     * 状态，0草稿，1待审核，2审核通过，3，ai审核通过，4，已发布，5被删除
     */
    private Short artworkStatus;

    /**
     * 作品封面存储位置，绝对路径
     */
    private String logoPath;

    /**
     * 图片审核状态 0 未审核  1 通过 2 不通过
     */
    private Short logoPathStatus;

    private Date lastCreateDate;

    private Date lastModifyDate;

    /**
     * 四字标签
     */

    private String fourLetterTips;

    /**
     * 审核人
     */
    private Integer fkAuditId;

    /**
     * 播放模式
     */
    private Integer playMode;

    private static final long serialVersionUID = 1L;
}
