package com.mpic.evolution.chair.pojo.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 * @Classname EcmArtworkNodesVo
 * @Description TODO
 * @Date 2020/8/4 9:47
 * @Created by cxd
 */

@Data
public class EcmArtworkNodesVo extends EcmArtworkNodes {

    /**
     * 储存 自己的 子节点集合
     */
    @JsonProperty(value = "childs" )
    @JSONField( name = "childs" )
    private List<EcmArtworkNodesVo> nodesVos;




}
