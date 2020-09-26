package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.tencent.video.TencentVideoResult;

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
}
