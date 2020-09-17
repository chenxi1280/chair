package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_invite_code
 * @author 
 */
@Data
public class EcmInviteCode implements Serializable {
    /**
     * 邀请码id
     */
    private Integer pkInviteId;

    /**
     * 邀请码绑定的用户id
     */
    private Integer fkUserId;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 状态,0 未绑定 1 已绑定
     */
    private Short inviteCodeStatus;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 绑定时间
     */
    private Date bindDate;

    private static final long serialVersionUID = 1L;
}