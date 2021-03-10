package com.mpic.evolution.chair.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ecm_vip_user_info
 * @author 
 */
@Data
public class EcmVipUserInfo implements Serializable {
    private Integer pkId;

    /**
     * 用户id
     */
    private Integer fkUserid;

    /**
     * 用户vip角色 0普通会员，1超级会员
     */
    private Integer fkVipRoleId;

    /**
     * 状态，0停用 1在用
     */
    private Short vipStatus;

    /**
     * vip起始时间
     */
    private Date vipStartTime;

    /**
     * vip截止时间
     */
    private Date vipEndTime;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}