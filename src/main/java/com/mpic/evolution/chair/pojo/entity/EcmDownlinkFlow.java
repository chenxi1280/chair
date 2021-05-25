package com.mpic.evolution.chair.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ecm_downlink_flow
 * @author 
 */
@Data
public class EcmDownlinkFlow implements Serializable {
    /**
     * 主键
     */
    private Integer pkId;

    /**
     * 用户id
     */
    private Integer fkUserId;

    /**
     * 点播子应用id
     */
    private Integer subAppId;

    /**
     * 用户下行总流量（单位KB）
     */
    private Long subTotalFlow;

    /**
     * 用户下行已使用流量（单位KB）
     */
    private Long subUsedFlow;

    /**
     * 当前记录的状态(预留)
     */
    private Integer subFlowStatus;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}