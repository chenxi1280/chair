package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_user_history_flow
 * @author 
 */
@Data
public class EcmUserHistoryFlow implements Serializable {
    /**
     * 主键
     */
    private Integer flowHistoryId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 视频code
     */
    private String videoCode;

    /**
     * 视频大小
     */
    private Integer videoFlow;

    /**
     * 创建时间
     */
    private Date creatTime;

    /**
     * 作品id
     */
    private Integer atrworkId;

    /**
     * 作品节点id
     */
    private Integer artworkNodeId;

    /**
     * 备注
     */
    private String remarks;

    private static final long serialVersionUID = 1L;
}