package com.mpic.evolution.chair.pojo.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mpic.evolution.chair.pojo.dto.EcmArtworkNodesDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeNumberCondition;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @Classname EcmArtworkNodesVo
 * @Description TODO
 * @Date 2020/8/4 9:47
 * @Created by cxd
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EcmArtworkNodesVo extends EcmArtworkNodes  {

    /**
     * 储存 自己的 子节点集合
     */
    @JsonProperty(value = "childs" )
    @JSONField( name = "childs" )
    private List<EcmArtworkNodesVo> nodesVos;


    /**
     * 	用户token
     */
    private String token;

    private Integer fkUserId;

    private EcmArtworkNodesDTO linkNode;
    // 兄弟节点 list 小程序 故事线 在使用
    private List<EcmArtworkNodesVo> brotherNode;
    // 作品 类型
    private String artWorkTips;
    // 定位选项 （前端还原使用）
    private List<NodeOptionLocationVO> nodeLocationList;

    // 数值选项（前端还原使用）
    private List<NodeNumberConditionVO> onAdvancedList;
    private EcmArtworkNodeNumberCondition ecmArtworkNodeNumberCondition;


    /**
     * 是否启用改变数值
     */
    private Byte changeFlag;

    /**
     * 是否启用判断出现数值
     */
    private Byte appearFlag;

    /**
     * 前端数值名字list
     */
    private List<String> onNameConditionList;

    private Map<Integer, List<EcmArtworkNodeNumberConditionVO>> appearConditionMap ;


    /**
     * 是否使用全局设置并全局使用 数值选项
     */
    private Byte allNameFlag;
    /**
     * 播放模式
     */
    private Integer playMode;
}
