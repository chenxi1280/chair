package com.mpic.evolution.chair.util;

import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author by cxd
 * @Classname TreeUtil
 * @Description TODO
 * @Date 2020/8/4 11:16
 */


public class TreeUtil {

    /**
     * 描述 : 构建树方法
     * 返回parentid=0的为更根点的树的EcmArtworkNodesVo的list集合
     *
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
        nodes.forEach(node -> {
                node.setNodesVos(sub.get(node.getPkDetailId()));
        });

        //返回 parentid=0的为更根点的树的EcmArtworkNodesVo的list集合
        return nodes.stream().filter(node -> node.getParentId() == 0).collect(Collectors.toList());
    }
    

    /**
     * 描述 : 构建树方法
     * 返回parentid=0的为更根点的树的EcmArtworkNodesVo的list集合
     *
     * @param: [nodes]  需要生成的树的数据 list
     * @author: cxd
     * @Date: 2020/8/4
     * @return: java.util.List<com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo>
     * 返回parentid=0的为更根点的树的EcmArtworkNodesVo的list集合
     */
    public static List<EcmArtworkNodesVo> buildTreeByDetailId(List<EcmArtworkNodesVo> nodes, Integer pkDetailId ) {
        //过滤掉根节点的 以 parentid 分组
        Map<Integer, List<EcmArtworkNodesVo>> sub = nodes.stream().filter(node -> node.getParentId() != 0).collect(Collectors.groupingBy(EcmArtworkNodesVo::getParentId));
        //从分组之后的sub集合中根据key (parentId)获取其孩子然后放到对应的父亲对象中分辨父亲的方式是通过detailId = parentId 即可找到父亲
        nodes.forEach(node -> {
                node.setNodesVos(sub.get(node.getPkDetailId()));
        }); 
        //返回 parentid=detailId的 子树
        if (pkDetailId == null){
            return nodes.stream().filter(node -> node.getParentId().equals(0)).collect(Collectors.toList());
        }
        return nodes.stream().filter(node -> node.getPkDetailId().equals(pkDetailId)).collect(Collectors.toList());
    }



}
