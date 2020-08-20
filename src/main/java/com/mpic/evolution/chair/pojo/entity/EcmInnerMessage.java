package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_inner_message
 * @author 
 */
@Data
public class EcmInnerMessage implements Serializable {
    /**
     * 信息记录id
     */
    private Integer pkMessageId;

    /**
     * 模板id
     */
    private Integer fkTemplateId;

    /**
     * 接收消息的用户id
     */
    private Integer fkUserId;

    /**
     * 审核人id
     */
    private Integer reviewerId;

    /**
     * 状态，0未读 1已读
     */
    private Short messageStatus;

    /**
     * 发送时间
     */
    private Date sendDate;

    private static final long serialVersionUID = 1L;
}