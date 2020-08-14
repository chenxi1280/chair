package com.mpic.evolution.chair.controller;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.core.signature.SignatureUtil;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/videoHandleConsumer")
    @ResponseBody
    public ResponseDTO getConfirmCode ( @RequestBody JSONObject jsonParam) {
        System.out.println("jsonParam.toJSONString() :  " + jsonParam.toJSONString());
        return null;
    }
}
