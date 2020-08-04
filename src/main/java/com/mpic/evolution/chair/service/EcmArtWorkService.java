package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.common.returnvo.ReturnVo;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;

/**
 * @author Administrator
 */
public interface EcmArtWorkService {

    /**
     *
     * @author: cxd
     * @param ecmArtWorkQuery 传入的 查询参数 查询参数可以有 用户id，用户名字（模糊），视频状态，类型（当前模糊）
     * @return ResponseDTO 中的data 包含 ArtWork的 条件查询 结果集
     */
    ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery);

    /**
     * @param: [ecmArtWorkQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * 描述 : 查询作品详情根据 作品id
     * @author: cxd
     * @Date: 2020/8/4
     */
    ResponseDTO getArtWork(EcmArtWorkQuery ecmArtWorkQuery);

    ResponseDTO addArtWorkNode(EcmArtworkNodes ecmArtworkNodes);

    ResponseDTO addArtWork(EcmArtworkNodesVo ecmArtworkNodesVo);
}
