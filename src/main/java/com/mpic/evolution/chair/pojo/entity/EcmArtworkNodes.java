package com.mpic.evolution.chair.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_nodes
 * @author 
 */
@Data
public class EcmArtworkNodes implements Serializable {


    private Integer pkDetailId;

    private Integer parentId;

    private String videoCode;

    private String parentCode;

    private String videoUrl;

    private Boolean branchPre;

    /**
     * 前台id
     */
    private String revolutionId;

    /**
     * 备注
     */
    @JsonProperty(value = "selectTitle" )
    private String videoText;

    private Integer fkArtworkId;

    @JsonProperty(value = "linkUrl" )
    private String items;

    private String itemsText;

    private String itemsBakText;

    @JsonProperty(value = "selectText" )
    private String cssVo;

    @JsonIgnore
    private Integer fkAchievementId;

    private Integer fkEndingId;

    /**
     * Y是更新节点，N不是
     */
    private String isleaf;

    /**
     * 级别，0为根结点，1为1级节点
     */
    @JsonProperty(value = "isLink" )
    private Integer aLevel;

    /**
     * Y是已删除，N是未删除
     */
    private String isDeleted;

    /**
     * （未使用）祖先数组，以逗号分隔，以0开头，以自己的父亲结尾
     */
    private String parentList;

    /**
     * （未使用）被选择的选项文本，与父亲的items_text[i]相同
     */
    private String chosenText;

    private static final long serialVersionUID = 1L;
}