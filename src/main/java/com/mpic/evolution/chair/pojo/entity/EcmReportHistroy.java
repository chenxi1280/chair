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
    private Integer reportid;

    /**
     * 举报历史记录表
     */
    private Integer fkArtworkId;

    private Integer fkUserid;

    private String comtemt;

    /**
     * 举报历史表
     */
    private Date datetime;

    private static final long serialVersionUID = 1L;
}