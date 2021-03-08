package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_goods_category
 * @author
 */
@Data
public class EcmGoodsCategory implements Serializable {
    /**
     * 类目主键
     */
    private Integer pkCategoryId;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 状态
     */
    private Integer categoryState;

    /**
     * 类型
     */
    private Integer categoryType;

    /**
     * 备注
     */
    private String categoryContext;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
