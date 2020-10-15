package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_video_temporary_storage
 * @author 
 */
@Data
public class EcmVideoTemporaryStorage implements Serializable {
    /**
     * 视频暂存表id
     */
    private Integer pkVideoId;

    /**
     * 视频验证code
     */
    private String videoCode;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 作品id
     */
    private Integer fkArtworkId;

    /**
     * 用户id
     */
    private Integer fkUserId;

    /**
     * 视频状态0为使用，1已使用
     */
    private Short videoStatus;

    private static final long serialVersionUID = 1L;
}