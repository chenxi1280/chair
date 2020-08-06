package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ecm_user_roles
 * @author 
 */
@Data
public class EcmUserRoles implements Serializable {
    /**
     * 角色id
     */
    private Integer pkRoleId;

    /**
     * 角色名字
     */
    private String name;

    /**
     * 标记
     */
    private String flag;

    /**
     * 备注
     */
    private String note;

    /**
     * 权限ids , 分割
     */
    private String acess;

    private static final long serialVersionUID = 1L;
}