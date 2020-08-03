package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * wx_user
 * @author 
 */
@Data
public class WxUser implements Serializable {
    private Integer pkId;

    /**
     * userId
     */
    private Integer fkUserId;

    /**
     * 小程序用户的openid
     */
    private String openid;

    /**
     * 用户头像
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatarurl;

    /**
     * 性别  0-男、1-女
     */
    private Boolean gender;

    /**
     * 所在国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 语种
     */
    private String language;

    /**
     * 创建/注册时间
     */
    private Date ctime;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 是否授权了
     */
    private Boolean authorized;

    private static final long serialVersionUID = 1L;
}