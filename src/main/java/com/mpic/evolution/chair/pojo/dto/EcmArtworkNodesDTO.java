package com.mpic.evolution.chair.pojo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author by cxd
 * @Classname EcmArtworkNodesDTO
 * @Description TODO
 * @Date 2020/9/9 9:13
 */
@Data
public class EcmArtworkNodesDTO implements Serializable {

    private Integer pkDetailId;

    private Integer parentId;

    private String videoCode;

    private String parentCode;

    private String videoUrl;

    // 是否为 定位
    @JsonProperty(value = "isPosition" )
    @JSONField(name = "isPosition ")
    /**
     * 选项类型 0普通选项 1定位选项 2数值选项
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
     * （未使用）祖先数组，以逗号分隔，以0开头，以自己的父亲结尾
     */
    private String parentList;

    /**
     * （未使用）被选择的选项文本，与父亲的items_text[i]相同
     */
    @JsonProperty(value = "isNumberSelect" )
    @JSONField(name = "isNumberSelect" )
    private String chosenText;



    /**
     * 	用户token
     */
    private String token;

    private Integer fkUserId;

    private EcmArtworkNodes linkNode;

    private List<EcmArtworkNodesVo> brotherNode;


    /**
     * 是否为多结局 0 ，null 不是 多结局，1是多结局
     */
    private Integer isEndings;

    private static final long serialVersionUID = 1L;
}
