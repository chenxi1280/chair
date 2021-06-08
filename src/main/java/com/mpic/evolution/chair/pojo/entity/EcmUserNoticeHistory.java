package com.mpic.evolution.chair.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ecm_user_notice_history
 * @author 
 */
@Data
public class EcmUserNoticeHistory implements Serializable {
    /**
     * 主键
     */
    private Integer pkId;

    /**
     * 用户id
     */
    private Integer fkUserId;

    /**
     * 短信类型
     */
    private String noticeType;

    /**
     * 短信内容
     */
    private String noticeContent;

    /**
     * 0发送失败 1发送成功
     */
    private Integer noticeStatus;

    /**
     * 记录更新时间
     */
    private Date updateTime;

    /**
     * 记录创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}