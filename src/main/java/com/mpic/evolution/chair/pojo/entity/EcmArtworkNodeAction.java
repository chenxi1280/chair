package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_node_action
 * @author
 */
@Data
public class EcmArtworkNodeAction implements Serializable {
    private Integer pkActionId;

    /**
     * 节点id
     */
    private Integer fkNodeId;

    /**
     * 作品id
     */
    private Integer fkArtworkId;

    /**
     * 动作宽
     */
    private String actionWide;

    /**
     * 动作高
     */
    private String actionHigh;

    /**
     * 透明度
     */
    private String actionOpacity;

    /**
     * 开始时间
     */
    private String actionSectionTime;

    /**
     * 结束时间
     */
    private String actionEndTime;

    /**
     * 坐标X
     */
    private String actionCoordinateX;

    /**
     * 坐标Y
     */
    private String actionCoordinateY;

    private Integer actionType;

    /**
     * -1 重播 0 选A 1 选b 2选 C 3选D
     */
    private Integer actionPlayEndType;

    /**
     * 0 圈 1 短直线 2 长直线
     */
    private Integer actionEventType;

    /**
     * 旋转值
     */
    private String actionTransform;

    /**
     * 比列尺
     */
    private String actionScale;

    private static final long serialVersionUID = 1L;
}
