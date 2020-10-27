package com.mpic.evolution.chair.pojo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ecm_artwork_node_number_condition
 * @author 
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
     * 作品id
     */
    private Integer fkArtworkId;

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