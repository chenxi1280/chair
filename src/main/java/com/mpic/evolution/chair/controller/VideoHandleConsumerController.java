package com.mpic.evolution.chair.controller;

import com.alibaba.fastjson.JSON;
import com.mpic.evolution.chair.pojo.tencent.video.TencentVideoResult;
import com.mpic.evolution.chair.service.VideoHandleConsumerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;

import javax.annotation.Resource;

/**
 *
 * 类名:  VideoHandleConsumerController
 * @author Xuezx
 * @date 2020/8/15 13:29
 * 描述: 用于云点播任务流等工作处理成功后，回调我们
 *
 */
@Controller
public class VideoHandleConsumerController {

    @Resource
    VideoHandleConsumerService videoHandleConsumerService;

    @RequestMapping("/videoHandleConsumer")
    @ResponseBody
    public ResponseDTO videoHandleConsumer ( @RequestBody  JSONObject jsonParam ) {
        TencentVideoResult tencentVideoResult = JSON.parseObject(jsonParam.toJSONString(), TencentVideoResult.class);
        return videoHandleConsumerService.videoHandleConsumer(tencentVideoResult);
    }
}
