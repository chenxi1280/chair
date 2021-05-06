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
     * img url
     */
    private String actionEventTypeImgUrl;

    /**
     * 旋转值
     */
    private String actionTransform;

    /**
     * 比列尺
     */
    private String actionScale;

    /**
     * 0 无文字，， 1 有问题
     */
    private Integer actionTextState;

    /**
     * 文字颜色
     */
    private String actionTextColor;

    /**
     * 文字大小
     */
    private String actionTextPoint;

    /**
     * 内容
     */
    private String actionTextValue;

    /**
     * 文字坐标X
     */
    private String actionTextCoordinateX;

    /**
     * 文字坐标Y
     */
    private String actionTextCoordinateY;

    private static final long serialVersionUID = 1L;
}
