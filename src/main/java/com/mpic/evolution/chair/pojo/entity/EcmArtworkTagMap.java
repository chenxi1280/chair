package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_tag_map
 * @author 
 */
@Data
public class EcmArtworkTagMap implements Serializable {
    /**
     * 标签映射表
     */
    private Integer pkMapId;

    /**
     * 作品表id
     */
    private Integer fkArtworkId;

    /**
     * tag表id
     */
    private Integer fkTagId;

    private static final long serialVersionUID = 1L;
}