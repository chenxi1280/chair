package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.service.BroadcastService;
import com.mpic.evolution.chair.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        //if(StringUtils.isBlank(userId) || !NumberUtils.isParsable(userId)){
        if(StringUtils.isBlank(videoId) || !NumberUtils.isParsable(videoId)){
            return ResponseDTO.fail(ErrorEnum.ERR_201.getValue());
        }
        int intVideoId = Integer.parseInt(videoId);

        return service.getStorylineInfo(intVideoId);
    }


    /**
     *
     * 方法名:  recordStorylineClick
     * @author Xuezx
     * @date 2020/6/19 0019 13:40
     * @param userId, userId
     * @param videoId,  videoId
     * @param passiveFlag 是否是主动退出
     * @param currentPlayTime 点击时视频的当前播放进度（秒）
     *
     * 描述: 记录故事线触发事件，有主动点击和被动点击两种
     *                被动点击被记录为作品看完
     *
     * todo 此处应该有错误码返回，不应该直接void

    @RequestMapping(value="/storyline/click/{userId}/{videoId}/{passiveFlag}/{currentPlayTime}")
    @ResponseBody
    public String recordStorylineClick(@PathVariable("userId") String userId,
                                       @PathVariable("videoId") String videoId,
                                       @PathVariable("passiveFlag") String passiveFlag,
                                       @PathVariable("currentPlayTime") String currentPlayTime
    ){

        String checkUserId = intFailReturn(userId, ErrorEnum.ERR_003);
        if(StringUtils.isNotBlank(checkUserId)){
            return ErrorEnum.ERR_003.toString();
        }

        String checkVideo = intFailReturn(videoId, ErrorEnum.ERR_201);
        if(StringUtils.isNotBlank(checkVideo)){
            return ErrorEnum.ERR_201.toString();
        }

        String checkPassiveFlag = intFailReturn(passiveFlag, ErrorEnum.ERR_202);
        if(StringUtils.isNotBlank(checkPassiveFlag)){
            return ErrorEnum.ERR_202.toString();
        }

        String checktCurrentPlayTime = intFailReturn(currentPlayTime, ErrorEnum.ERR_203);
        if(StringUtils.isNotBlank(checktCurrentPlayTime)){
            return ErrorEnum.ERR_203.toString();
        }

        int intUserId = Integer.parseInt(userId);
        int intPassiveFlag = Integer.parseInt(passiveFlag);
        int intVideoId = Integer.parseInt(videoId);
        int intCurrentPlayTime = Integer.parseInt(currentPlayTime);

        service.recordStorylineClick(intUserId, intVideoId, intPassiveFlag, intCurrentPlayTime);
        return "true";
    }
     */


}
