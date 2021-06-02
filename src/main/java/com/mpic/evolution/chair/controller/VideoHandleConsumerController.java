package com.mpic.evolution.chair.controller;


import com.mpic.evolution.chair.pojo.tencent.video.TencentVideoResult;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.pojo.vo.SendNoticeVO;
import com.mpic.evolution.chair.service.VideoHandleConsumerService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
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

    /**
     * @param: [jsonParam]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/9/26
     * 描述 : 腾讯云审核回调接口
     *       成功: status 200  msg "success”
     *       失败: status 500  msg "error“
     */
    @RequestMapping("/videoHandleConsumer")
    @ResponseBody
    public ResponseDTO videoHandleConsumer ( @RequestBody  JSONObject jsonParam ) {
        TencentVideoResult tencentVideoResult =  JSONObject.parseObject(jsonParam.toJSONString(), TencentVideoResult.class);
        return videoHandleConsumerService.videoHandleConsumer(tencentVideoResult);
    }


    @RequestMapping("/copyVideoByArtworkId")
    @ResponseBody
    public void copyVideoByArtworkId(@RequestBody EcmArtworkVo ecmArtworkVo) {
        System.out.println("copyVideoByArtworkId，需要移动数据！"+ ecmArtworkVo.getPkArtworkId());
        try {
            videoHandleConsumerService.copyVideo(ecmArtworkVo.getPkArtworkId());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("copy到私有桶失败！");
        }
    }

}
