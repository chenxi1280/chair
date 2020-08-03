package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_node_ending
 * @author 
 */
@Data
public class EcmArtworkNodeEnding implements Serializable {
    private Integer pkEndingId;

    /**
     * 0是假1是真
     */
    private Boolean isEnding;

    private String endingText;

    private String endingImageUrl;

    private String endingType;

    private Boolean endingShow;

    private static final long serialVersionUID = 1L;
}