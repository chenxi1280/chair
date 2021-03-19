package com.mpic.evolution.chair.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ecm_vip_role_authority
 * @author 
 */
@Data
public class EcmVipRoleAuthority implements Serializable {
    private Integer pkId;

    /**
     * 用户vip角色id 0普通会员，1超级会员
     */
    private Integer fkVipRoleId;

    /**
     * 角色权限id
     */
    private Integer fkVipAuthorityId;

    /**
     * 普通会员,超级会员,普通用户
     */
    private String vipRoleDescribe;

    /**
     * 权限描述
     */
    private String vipAuthorityDescribe;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}