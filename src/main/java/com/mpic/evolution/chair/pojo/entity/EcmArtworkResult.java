package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_result
 * @author 
 */
@Data
public class EcmArtworkResult implements Serializable {
    /**
     * 作品结局表
     */
    private Integer pkArtworkResultId;

    /**
     * 作品详情表id
     */
    private Integer fkVideoId;

    /**
     * 好，坏，自定义结局
     */
    private String resultType;

    /**
     * 结局描述
     */
    private String resultDetails;

    /**
     * 图片描述地址
     */
    private String picUrl;

    private static final long serialVersionUID = 1L;
}