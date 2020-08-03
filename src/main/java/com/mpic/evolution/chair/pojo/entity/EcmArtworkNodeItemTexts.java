package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_node_item_texts
 * @author 
 */
@Data
public class EcmArtworkNodeItemTexts implements Serializable {
    private Integer pkNodeItemTextId;

    /**
     * 前台id
     */
    private String revolutionId;

    /**
     * 选项字符
     */
    private String value;

    /**
     * 是否是备份字段
     */
    private Boolean isBak;

    private static final long serialVersionUID = 1L;
}