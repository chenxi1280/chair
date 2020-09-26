package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.tencent.video.TencentVideoResult;

/**
 * @author Administrator
 */
public interface VideoHandleConsumerService {

    void handleOneVideo(String videoCode);

    ResponseDTO videoHandleConsumer(TencentVideoResult tencentVideoResult);
}
