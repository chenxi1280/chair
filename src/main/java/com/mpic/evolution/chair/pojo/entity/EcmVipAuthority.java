package com.mpic.evolution.chair.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ecm_vip_authority
 * @author 
 */
@Data
public class EcmVipAuthority implements Serializable {
    private Integer pkAuthorityId;

    /**
     * 普通会员,超级会员
     */
    private String vipAuthorityDescribe;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}