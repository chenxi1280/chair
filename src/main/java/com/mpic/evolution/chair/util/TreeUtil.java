package com.mpic.evolution.chair.util;

import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname TreeUtil
 * @Description TODO
 * @Date 2020/8/4 11:16
 * @author by cxd
 */


public class TreeUtil {

    /**
     * 描述 : 构建树方法
     *       返回parentid=0的为更根点的树的EcmArtworkNodesVo的list集合
     * @param: [nodes]  需要生成的树的数据 list
     * @author: cxd
     * @Date: 2020/8/4
     * @return: java.util.List<com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo>
     * 返回parentid=0的为更根点的树的EcmArtworkNodesVo的list集合
     */
    public static List<EcmArtworkNodesVo> buildTree(List<EcmArtworkNodesVo> nodes) {
        //过滤掉根节点的 以 parentid 分组
        Map<Integer, List<EcmArtworkNodesVo>> sub = nodes.stream().filter(node -> node.getParentId() != 0).collect(Collectors.groupingBy(EcmArtworkNodesVo::getParentId));
        //把 每一个的node节点都 list设置到 父节点中
        nodes.forEach(node -> node.setNodesVos(sub.get(node.getPkDetailId())));
        //返回 parentid=0的为更根点的树的EcmArtworkNodesVo的list集合
        return nodes.stream().filter(node -> node.getParentId() == 0).collect(Collectors.toList());
    }

}
