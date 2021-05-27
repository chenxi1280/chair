package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.tencent.video.TencentVideoResult;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @author Administrator
 */
public interface VideoHandleConsumerService {

    void handleOneVideo(String videoCode);

    /**
     * @param: [jsonParam]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/9/26
     * 描述 : 腾讯云审核回调接口
     *       成功: status 200  msg "success”
     *       失败: status 500  msg "error“
     */
    ResponseDTO videoHandleConsumer(TencentVideoResult tencentVideoResult);

    void handleArtwork(Integer pkArtworkId);

    /**
      * 方法名:copyVideo
      * @author Xuezx (◔‸◔）
      * @param pkArtworkId
      * @date 2021/5/25 11:06
      * 方法描述: 将一个作品中所有的视频从主库中copy到各自的独立库中
      */
    void copyVideo(Integer pkArtworkId);

    /**
     * 方法名:copyVideoByNodeList
     * @author cxd
     * @param ecmArtworkNodesVos <EcmArtworkNodesVo> , Integer pkArtworkId
     * @date 2021/5/25 11:06
     * 方法描述: 将一个节点list中所有的视频从主库中copy到各自的独立库中
     */
    void copyVideoByNodeList(List<EcmArtworkNodesVo> ecmArtworkNodesVos,Integer pkArtworkId);

}
