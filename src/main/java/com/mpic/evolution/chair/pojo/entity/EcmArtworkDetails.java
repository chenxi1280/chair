package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_details
 * @author 
 */
@Data
public class EcmArtworkDetails implements Serializable {
    /**
     * 作品详情表,已废弃
     */
    private Integer pkDetailId;

    /**
     * 父节点id，为0代表是根结点
     */
    private Integer parentId;

    /**
     * 用于展现小程序中父子结构的id
     */
    private String nodeCode;

    /**
     * 作品id
     */
    private Integer fkArtworkId;

    /**
     * 作品存储在服务器上的目录，暂时以文件方式存储
     */
    private String path;

    /**
     * 数据大小（kb）
     */
    private Long fileSize;

    /**
     * 数据时长（毫秒）
     */
    private Integer artworkLastLong;

    /**
     * 剧情类型，结局0，1过场，2分支,3根爸爸
     */
    private Short artworkType;

    /**
     * 已废弃
     */
    private String text;

    /**
     * 已废弃
     */
    private Short finalResult;

    /**
     * 备注
     */
    private String tips;

    /**
     * Y/N 是否有效。
     */
    private String isValid;

    /**
     * 已废弃
     */
    private String fileHash;

    private static final long serialVersionUID = 1L;
}