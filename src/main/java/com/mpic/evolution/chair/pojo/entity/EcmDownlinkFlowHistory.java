package com.mpic.evolution.chair.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ecm_downlink_flow_history
 * @author 
 */
@Data
public class EcmDownlinkFlowHistory implements Serializable {
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
     * 用户下行已使用流量（单位Byte）
     */
    private Long subUsedFlow;

    /**
     * 当前记录的状态(预留)
     */
    private Integer subFlowStatus;

    /**
     * 计算已使用下行流量的起始时间
     */
    private Date startTime;

    /**
     * 计算已使用下行流量的结束时间
     */
    private Date endTime;

    /**
     * 记录创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}