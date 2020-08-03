package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_node_css
 * @author 
 */
@Data
public class EcmArtworkNodeCss implements Serializable {
    private Integer pkNodeCssId;

    private String xclass;

    private String houheng;

    private String dalogheight;

    private String radioheight;

    private String xiantiaoheight;

    private String topheight;

    private Integer fkArtworkNodeId;

    private static final long serialVersionUID = 1L;
}