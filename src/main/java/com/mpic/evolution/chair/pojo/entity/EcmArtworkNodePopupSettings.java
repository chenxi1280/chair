package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

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
     * 弹窗名字
     */
    private String popupName;

    /**
     * 名字状态
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

    private static final long serialVersionUID = 1L;
}
