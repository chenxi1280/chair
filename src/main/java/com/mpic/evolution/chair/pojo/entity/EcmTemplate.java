package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_template
 * @author 
 */
@Data
public class EcmTemplate implements Serializable {
    /**
     * 消息模板
     */
    private Integer pkTemplateId;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 上次操作时间
     */
    private Date updateDate;

    private static final long serialVersionUID = 1L;
}