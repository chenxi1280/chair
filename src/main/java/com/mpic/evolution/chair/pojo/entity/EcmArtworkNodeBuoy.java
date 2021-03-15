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
     * 状态 0不启用跳转，1启用跳转
     */
    private Integer buoyStatus;

    /**
     * 浮标 弹窗 内容
     */
    private String buoyPopContext;

    /**
     * 0 其他小程序 1文字 2 图片
     */
    private Integer buoyPopType;

    /**
     * appid
     */
    private String buoyPopAppId;

    /**
     * 事件
     */
    private String buoyEvent;

    private static final long serialVersionUID = 1L;
}
