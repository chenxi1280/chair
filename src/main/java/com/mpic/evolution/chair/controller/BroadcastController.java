package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.service.BroadcastService;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 *
 * 类名:BroadcastController
 * @author Xuezx
 * @date 2019/9/24 0024 8:46
 * 描述: 视频播放相关类
 */

@Controller
public class BroadcastController {

    @Resource
    private BroadcastService service;

    /**
      * 方法名: getBroadcastInfo
      * @author Xuezx (◔‸◔）
      * @param ecmArtWorkQuery 普通的request请求实体
      * @date 2020/8/17 16:50
      * @return com.mpic.evolution.chair.pojo.dto.ResponseDTO
      * 方法描述: 播放页获取播放信息，主要是播放页面用来播放作品，选项，并且链接到下一页面用的
      */
    @RequestMapping(value="/getBroadcastInfo")
    @ResponseBody
    public ResponseDTO getBroadcastInfo(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){

        LogUtil.outputLog("/getBroadcastInfo", "getBroadcastInfo", ecmArtWorkQuery.toString(),
                false, "", "getBroadcastInfo入参日志");
        Integer intUserId = ecmArtWorkQuery.getFkUserid();
        if(intUserId == null) {
            String token = ecmArtWorkQuery.getToken();
            String userId = JWTUtil.getUserId(token);
            if(StringUtils.isNotBlank(userId) && NumberUtils.isParsable(userId)){
                intUserId = Integer.parseInt(userId);
                ecmArtWorkQuery.setFkUserid(intUserId);
            } else {
                return ResponseDTO.fail(ErrorEnum.ERR_X00.getValue());

            }
        }
        String videoId = ecmArtWorkQuery.getVideoId();
        //if(StringUtils.isBlank(userId) || !NumberUtils.isParsable(userId)){
        if(StringUtils.isBlank(videoId) || !NumberUtils.isParsable(videoId)){
            return ResponseDTO.fail(ErrorEnum.ERR_201.getValue());
        }
        int intVideoId = Integer.parseInt(videoId);

        ecmArtWorkQuery.setIntVideoId(intVideoId);
        return service.getBroadcastInfo(ecmArtWorkQuery);
    }

    /**
      * 方法名:getStorylineInfo
      * @author Xuezx (◔‸◔）
      * @param ecmArtWorkQuery EcmArtWorkQuery
      * @date 2020/8/17 20:33
      * @return java.lang.String
      * 方法描述:  获取故事线接口
      */
    @RequestMapping(value="/getStoryline")
    @ResponseBody
    public ResponseDTO getStorylineInfo(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
        String videoId = ecmArtWorkQuery.getVideoId();
        if(StringUtils.isBlank(videoId) || !NumberUtils.isParsable(videoId)){
            return ResponseDTO.fail(ErrorEnum.ERR_201.getValue());
        }
        int intVideoId = Integer.parseInt(videoId);

        return service.getStorylineInfo(intVideoId);
    }



}
