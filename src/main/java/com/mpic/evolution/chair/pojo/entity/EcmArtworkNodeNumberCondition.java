package com.mpic.evolution.chair.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_artwork_node_number_condition
 * @author
 */
@Data
public class EcmArtworkNodeNumberCondition implements Serializable {
    /**
     * nodeId
     */
    private Integer pkDetailid;

    /**
     * 数值0出现的条件
     */
    private String appearCondition0;

    /**
     * 数值0发生怎样的改变
     */
    private String changeCondition0;

    /**
     * 数值1出现的条件
     */
    private String appearCondition1;

    /**
     * 数值1发生怎样的改变
     */
    private String changeCondition1;

    /**
     * 数值2出现的条件
     */
    private String appearCondition2;

    /**
     * 数值2发生怎样的改变
     */
    private String changeCondition2;

    /**
     * 数值3出现的条件
     */
    private String appearCondition3;

    /**
     * 数值3发生怎样的改变
     */
    private String changeCondition3;

    /**
     * 条件0 的名字
     */
    private String nameCondition0;

    /**
     * 条件0的 名字是否在前端展示
     */
    private Byte nameDisplay0;

    /**
     * 条件1的名字
     */
    private String nameCondition1;

    /**
     * 条件1的 名字是否在前端展示
     */
    private Byte nameDisplay1;

    /**
     * 条件2的名字
     */
    private String nameCondition2;

    /**
     * 条件2的 名字是否在前端展示
     */
    private Byte nameDisplay2;

    /**
     * 条件3 的名字
     */
    private String nameCondition3;

    /**
     * 条件3的 名字是否在前端展示
     */
    private Byte nameDisplay3;

    /**
     * 作品id
     */
    private Integer fkArtworkId;

    /**
     * 是否启用数值选项
     */
    private Byte changeFlag;

    /**
     * 是否启用判断出现数值
     */
    private Byte appearFlag;

    /**
     * 是否启用自定义名字
     */
    private Byte nameFlag;

    /**
     * 是否使用全局设置并全局使用 数值选项
     */
    private Byte allNameFlag;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updataDate;

    private static final long serialVersionUID = 1L;
}
