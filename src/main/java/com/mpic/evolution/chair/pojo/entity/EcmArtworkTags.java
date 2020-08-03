package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_tags
 * @author 
 */
@Data
public class EcmArtworkTags implements Serializable {
    /**
     * 作品标签表，标签要求20个字符以内
     */
    private Integer pkTagId;

    /**
     * 20个字符以内
     */
    private String name;

    /**
     * status 审核状态0未审核1通过2禁止
     */
    private Short status;

    private static final long serialVersionUID = 1L;
}