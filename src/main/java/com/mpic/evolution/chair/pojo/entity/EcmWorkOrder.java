package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_work_order
 * @author
 */
@Data
public class EcmWorkOrder implements Serializable {
    /**
     * 工单主键
     */
    private Integer pkWorkOrderId;

    /**
     * 用户id
     */
    private Integer fkUserId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户公司
     */
    private String userCompany;

    /**
     * 用户联系方式
     */
    private String userContactInformation;

    /**
     * 用户申请功能
     */
    private String userApplicationFunction;

    /**
     * 工单状态
     */
    private Integer applyStatus;

    /**
     * 处理人
     */
    private Integer fkHandlerId;

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
