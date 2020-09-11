package com.mpic.evolution.chair.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.mpic.evolution.chair.pojo.query.EcmInnerMessageQurey;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.StringUtils;
import com.qcloud.vod.common.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;
import com.mpic.evolution.chair.pojo.vo.EcmInnerMessageVo;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmInnerMessageService;

import lombok.extern.slf4j.Slf4j;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-20 10:48:54 
*/
@Slf4j
@Controller
@RequestMapping("/EcmInnerMessage")
public class EcmInnerMessageController {
	
	@Resource
	EcmInnerMessageService ecmInnerMessageService;
	

    @RequestMapping("/getInnerMessage")
    @ResponseBody
	public ResponseDTO getInnerMessage(@RequestBody EcmUserVo user) {
		return ecmInnerMessageService.getMsg(user);
    }
    

    @RequestMapping("/batchDelete")
    @ResponseBody
	public ResponseDTO batchDelete(@RequestBody EcmInnerMessageQurey ecmInnerMessageQurey) {
		if (StringUtil.isEmpty(ecmInnerMessageQurey.getToken())){
			return ResponseDTO.fail("非法访问");
		}
		if (CollectionUtils.isEmpty(ecmInnerMessageQurey.getMessageIds()) ){
			return ResponseDTO.ok("无新消息可操作！");
		}
		String userId = JWTUtil.getUserId(ecmInnerMessageQurey.getToken());
		ecmInnerMessageQurey.setPkUserId(Integer.valueOf(userId));


		return ecmInnerMessageService.batchDelete(ecmInnerMessageQurey);
    }
    

    @RequestMapping("/batchModifyRead")
    @ResponseBody
	public ResponseDTO batchModifyRead(@RequestBody EcmInnerMessageQurey ecmInnerMessageQurey) {
    	if (StringUtil.isEmpty(ecmInnerMessageQurey.getToken())){
    		return ResponseDTO.fail("非法访问");
		}
		String userId = JWTUtil.getUserId(ecmInnerMessageQurey.getToken());
		ecmInnerMessageQurey.setPkUserId(Integer.valueOf(userId));
		return ecmInnerMessageService.batchModifyRead(ecmInnerMessageQurey);

	}
}	
