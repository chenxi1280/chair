package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_artwork_node_popup_settings
 * @author
 */
@Data
public class EcmArtworkNodePopupSettings implements Serializable {
    /**
     * 主键
     */
    private Integer pkNodePopupSettingsId;

    /**
     * node id
     */
    private Integer fkNodeId;

    /**
     * 作品id
     */
    private Integer fkArtworkId;

    /**
     * 弹窗名字
     */
    private String popupName;

    /**
     * 名字状态 0
     */
    private Integer popupNameState;

    /**
     * 弹窗位置
     */
    private Integer popupPosition;

    /**
     * 弹窗内容
     */
    private String popupContext;

    /**
     * 弹窗内容格式 1 图片 2 图片连接地址
     */
    private Integer popupContextState;

    /**
     * 弹窗跳转地址
     */
    private String popupSkip;

    /**
     * 弹窗跳转appid
     */
    private String popupAppId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
