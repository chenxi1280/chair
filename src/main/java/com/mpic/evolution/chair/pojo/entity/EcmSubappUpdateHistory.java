package com.mpic.evolution.chair.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ecm_subapp_update_history
 * @author 
 */
@Data
public class EcmSubappUpdateHistory implements Serializable {
    /**
     * 主键
     */
    private Integer pkId;

    /**
     * 用户id
     */
    private Integer fkUserId;

    /**
     * 点播子应用id
     */
    private Integer subAppId;

    /**
     * 0停用 1启用
     */
    private Integer status;

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