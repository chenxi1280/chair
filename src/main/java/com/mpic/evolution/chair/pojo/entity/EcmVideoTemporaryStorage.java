package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
     * 视频名字
     */
    private String videoName;

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

    /**
     * 节点id
     */
    private Integer fkNodeId;

    /**
     * 作品图片url
     */
    private String nodeImgUrl;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date updateDate;


    /**
     * 视频信息
     */
    private String videoInfo;

    private static final long serialVersionUID = 1L;
}
