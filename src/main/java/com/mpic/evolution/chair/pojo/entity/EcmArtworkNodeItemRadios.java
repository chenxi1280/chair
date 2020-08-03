package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_node_item_radios
 * @author 
 */
@Data
public class EcmArtworkNodeItemRadios implements Serializable {
    private Integer pkItemRadioId;

    private Integer fkArtworkNodeId;

    private String radioName;

    private String radioValue;

    private String radioChecked;

    private static final long serialVersionUID = 1L;
}