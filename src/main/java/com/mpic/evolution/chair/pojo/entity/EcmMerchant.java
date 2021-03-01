package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_merchant
 * @author
 */
@Data
public class EcmMerchant implements Serializable {
    /**
     * 商户id
     */
    private Integer pkMerchantId;

    /**
     * 对应的用户id
     */
    private Integer fkUserId;

    /**
     * 商家名称
     */
    private String merchantName;

    /**
     * 商家联系电话
     */
    private String mobilePhone;

    /**
     * 商家简介
     */
    private String merchantDetail;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 启用状态 0未启用 1启用
     */
    private Integer openState;

    /**
     * 审批状态
     */
    private Integer approvalStatus;

    /**
     * 凭证照片
     */
    private String voucherImg;

    /**
     * 商家主营类型
     */
    private String businessType;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人号码
     */
    private String contactPhone;

    /**
     * 联系人WX
     */
    private String contactVx;

    /**
     * H5访问地址
     */
    private String h5Url;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
