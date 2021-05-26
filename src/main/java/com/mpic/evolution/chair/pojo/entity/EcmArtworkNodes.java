package com.mpic.evolution.chair.pojo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ecm_artwork_nodes
 * @author
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class EcmArtworkNodes implements Serializable  {


    private Integer pkDetailId;

    private Integer parentId;

    private String videoCode;

    private String parentCode;

    private String videoUrl;

    private String privateVideoUrl;

    // 是否为 定位
    @JsonProperty(value = "isPosition" )
    @JSONField(name = "isPosition ")
    /**
     * 选项类型 0普通选项 1定位选项 2浮标选项
     */
    private Byte branchPre;

    /**
     * 前台id
     */
    @JsonProperty(value = "xid" )
    @JSONField(name = "xid" )
    private String revolutionId;

    /**
     * 备注
     * 播放
     */
    @JsonProperty(value = "selectTitle" )
    @JSONField(name = "selectTitle")
    private String videoText;

    private Integer fkArtworkId;

    // 跳转url
    @JsonProperty(value = "linkUrl" )
    @JSONField(name = "linkUrl")
    private String items;

    // node 定位节点
    @JsonProperty(value = "nodeLocation" )
    @JSONField(name = "nodeLocation")
    @JsonIgnore
    private String itemsText;

    // 节点图片url
    @JsonProperty(value = "nodeImgUrl" )
    @JSONField(name = "nodeImgUrl" )
    private String itemsBakText;

    //
    @JsonProperty(value = "selectText" )
    @JSONField(name = "selectText")
    private String cssVo;


    private Integer fkAchievementId;
    /**
     *  1:ai审核通过，2ai审核不通过，3ai审核有相似的，4，后台人员通过，5后台人员不通过 6投诉节点
     */
    @JsonIgnore
    private Integer fkEndingId;

    /**
     * Y是更新节点，N不是
     */
    private String isleaf;

    /**
     * 是否为跳转节点 1 跳
     */
    @JsonProperty(value = "isLink" )
    @JSONField(name = "isLink" )
    private Integer aLevel;

    /**
     * Y是已删除，N是未删除
     */
    private String isDeleted;

    /**
     * （使用）视频最后一帧
     */
    @JsonProperty(value = "nodeLastImgUrl" )
    @JSONField(name = "nodeLastImgUrl" )
    private String parentList;

    /**
     * （未使用）被选择的选项文本，与父亲的items_text[i]相同
     */
    @JsonProperty(value = "isNumberSelect" )
    @JSONField(name = "isNumberSelect" )
    private String chosenText;

    /**
     * 视频宽高，时长
     */
    private String videoInfo;

    /**
     * 弹窗是否启用 0 null 为未启用 1 启用
     */
    private Integer popupState;

    /**
     * 条件是否启用 0 null 为未启用 1 启用
     */
    private Integer conditionState;

    /**
     * 百分比状态是否启用 0 null 为未启用 1 启用
     */
    private Integer percentageState;

    private static final long serialVersionUID = 1L;



}
