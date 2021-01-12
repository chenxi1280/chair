package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_node_buoy
 * @author
 */
@Data
public class EcmArtworkNodeBuoy implements Serializable {
    /**
     * 浮标主键
     */
    private Integer pkBuoyId;

    /**
     * 节点id
     */
    private Integer fkNodeId;

    /**
     * 作品id
     */
    private Integer fkArtworkId;

    /**
     * 浮标宽
     */
    private String buoyWide;

    /**
     * 浮标高
     */
    private String buoyHigh;

    /**
     * 浮标框透明度
     */
    private String buoyOpacity;

    /**
     * 切片时间点
     */
    private String buoySectionTime;

    /**
     * 坐标x
     */
    private String buoyCoordinateX;

    /**
     * 坐标y
     */
    private String buoyCoordinateY;

    /**
     * 类型
     */
    private Integer buoyType;

    /**
     * 状态
     */
    private Integer buoyStatus;

    /**
     * 备注
     */
    private String buoyContext;

    private static final long serialVersionUID = 1L;
}
