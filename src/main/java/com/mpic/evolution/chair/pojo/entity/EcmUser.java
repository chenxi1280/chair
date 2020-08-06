package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_user
 * @author 
 */
@Data
public class EcmUser implements Serializable {
    private Integer pkUserId;

    /**
     * 加密
     */
    private String password;

    /**
     * 权限表id
     */
    private Short fkAccessId;

    /**
     * 是否有效Y/N
     */
    private String isValid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 加密
     */
    private String mobile;

    /**
     * 加密
     */
    private String email;

    private Date createTime;

    private Date updateTime;

    /**
     * 登录次数
     */
    private Integer count;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 身份证号，加密
     */
    private String cardCode;

    /**
     * 最后一次查看消息是什么时候
     */
    private Date lastCheckMail;

    /**
     * 角色id , 分割
     */
    private String roles;

    private static final long serialVersionUID = 1L;
}