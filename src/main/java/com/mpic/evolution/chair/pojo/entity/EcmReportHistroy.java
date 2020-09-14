package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_report_histroy
 * @author 
 */
@Data
public class EcmReportHistroy implements Serializable {
    /**
     * 投诉id
     */
    private Integer reportId;

    /**
     * 作品id
作品id
     */
    private Integer fkArtworkId;

    /**
     * 用户id
     */
    private Integer fkUserid;

    /**
     * 节点id
     */
    private Integer fkArtworkNodeId;

    /**
     * 处理人id
     */
    private Integer fkChUserid;

    /**
     * 投诉 状态 1.未处理 2。已处理
     */
    private Short reState;

    /**
     * 投诉类型：1侵权，2违规，3其他
     */
    private Short reportStatue;

    /**
     * 截图地址
     */
    private String imgUrl;

    /**
     * 投诉内容
     */
    private String content;

    /**
     * 举报时间
     */
    private Date createtime;

    /**
     * 处理时间
     */
    private Date updataTime;

    private static final long serialVersionUID = 1L;
}