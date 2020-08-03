package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_user_acess
 * @author 
 */
@Data
public class EcmUserAcess implements Serializable {
    /**
     * 用户权限表
     */
    private Integer pkAcessId;

    /**
     * 权限名称
     */
    private String accessName;

    /**
     * 权限描述
     */
    private String accessDetails;

    private static final long serialVersionUID = 1L;
}