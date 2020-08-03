package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_user_followed
 * @author 
 */
@Data
public class EcmUserFollowed implements Serializable {
    /**
     * 用户关注表
     */
    private Integer pkUserFollowedId;

    private Integer userId;

    private Integer followedUserId;

    /**
     * 状态，0取消，1关注
     */
    private String status;

    private Date createdAt;

    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}