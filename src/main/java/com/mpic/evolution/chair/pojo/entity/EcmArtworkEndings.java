package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_endings
 * @author
 */
@Data
public class EcmArtworkEndings implements Serializable {
    /**
     *
     */
    private Integer pkEndingsId;

    /**
     * 对应作品id
     */
    private Integer fkArtworkId;

    /**
     * 结局名称
     */
    private String selectTitle;

    /**
     * 视频code
     */
    private String videoCode;

    /**
     * 视频img
     */
    private String videoImg;

    /**
     * 视频url
     */
    private String videoUrl;

    /**
     * 多结局数据json串
     */
    private String selectTree;

    /**
     * 备注
     */
    private String comment;

    /**
     * 对应的节点id
     */
    private Integer fkNodeId;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 弹窗是否启用 0 null 为未启用 1 启用
     */
    private Integer conditionState;

    /**
     * 弹窗是否启用 0 null 为未启用 1 启用
     */
    private Integer popupState;

    private static final long serialVersionUID = 1L;
}
