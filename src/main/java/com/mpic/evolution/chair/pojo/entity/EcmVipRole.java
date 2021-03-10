package com.mpic.evolution.chair.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ecm_vip_role
 * @author 
 */
@Data
public class EcmVipRole implements Serializable {
    private Integer pkRoleId;

    /**
     * 普通会员,超级会员
     */
    private String vipRoleDescribe;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}