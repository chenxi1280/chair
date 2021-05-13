package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_node_buoy_panoramic
 * @author
 */
@Data
public class EcmArtworkNodeBuoyPanoramic implements Serializable {
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
     * 文字大小
     */
    private String buoyTextSize;

    /**
     * 文字颜色
     */
    private String buoyTextColor;

    /**
     * 浮标物体
     */
    private String buoyThingSize;

    /**
     * 浮标物体地址
     */
    private String buoyThingImgUrl;

    /**
     * 浮标物体类型
     */
    private Integer buoyThingType;

    /**
     * 浮标物体颜色
     */
    private String buoyThingColor;

    /**
     * 浮标物体透明度
     */
    private String buoyThingOpacity;

    /**
     * 开始
     */
    private String buoyStartTime;

    /**
     * 展示类型
     */
    private Integer buoyShowType;

    /**
     * 结束
     */
    private String buoyEndTime;

    /**
     * 动作类型
     */
    private Integer buoyActionType;

    /**
     * 坐标xyz
     */
    private String buoyThingCoordinateXYZ;

    /**
     * 问题坐标
     */
    private String buoyTextCoordinateXYZ;

    /**
     * 类型
     */
    private Integer buoyType;

    /**
     * 选择类型
     */
    private Integer buoyChooseType;

    /**
     * 选择播放类型
     */
    private Integer buoyChoosePlayType;

    /**
     * 浮标全景事件
     */
    private String buoyChooseEvent;

    /**
     * 播放到结尾的时候 重播还是 自动选择 某一个
     */
    private Integer buoyPlayEndType;

    /**
     * 状态 0不启用跳转，1启用跳转
     */
    private Integer buoyStatus;

    /**
     * 浮标 弹窗 内容
     */
    private String buoyPopContext;

    /**
     * 0 其他小程序 1文字 2 图片 3.公众号
     */
    private Integer buoyPopType;

    /**
     * appid
     */
    private String buoyPopAppId;

    /**
     * 网页地址
     */
    private String buoyPopWebUrl;

    /**
     * 事件
     */
    private String buoyEvent;

    /**
     * 浮标context
     */
    private String buoyContext;

    private static final long serialVersionUID = 1L;
}
