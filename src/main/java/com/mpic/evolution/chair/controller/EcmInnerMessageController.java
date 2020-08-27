package com.mpic.evolution.chair.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.StringUtils;
import com.qcloud.vod.common.StringUtil;
import org.springframework.stereotype.Controller;
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
	
	/**
     * 	获取图片验证码接口
     *	 以BASE64转码的字符串传到前端
     */
    @RequestMapping("/getInnerMessage")
    @ResponseBody
	public ResponseDTO getInnerMessage(@RequestBody EcmUserVo user) {
//    	JSONObject data = new JSONObject();
//    	if (StringUtil.isEmpty(user.getToken())){
//    		return ResponseDTO.fail("网络错误");
//		}
//    	List<EcmInnerMessage> messages = ecmInnerMessageService.getInnerMessage(user);
//    	if (messages == null || messages.isEmpty()) {
//			return ResponseDTO.fail("获取message失败");
//		}
//    	messages = messages.stream().filter((EcmInnerMessage m)->m.getMessageStatus()<2).collect(Collectors.toList());
//		messages.sort((EcmInnerMessage m1, EcmInnerMessage m2)->m2.getSendDate().compareTo(m1.getSendDate()));
//		Map<Short, Long> sum = messages.stream().collect(Collectors.groupingBy(EcmInnerMessage::getMessageStatus, Collectors.counting()));
//		data.put("messages", messages);
//		data.put("read", sum.get((short)1));
//		data.put("unread", sum.get((short)0));
//		return ResponseDTO.ok("获取成功", data);
		return ecmInnerMessageService.getMsg(user);
    }
    
    /**
     * 	获取图片验证码接口
     *	 以BASE64转码的字符串传到前端
     */
    @RequestMapping("/batchDelete")
    @ResponseBody
	public ResponseDTO batchDelete(EcmInnerMessageVo ecmInnerMessageVo) {
    	List<Integer> messageIds = ecmInnerMessageVo.getMessageIds();
    	boolean batchDelete = ecmInnerMessageService.batchDelete(ecmInnerMessageVo);
    	if (batchDelete) {
			return ResponseDTO.ok("批量删除信息success");
		}else {
			log.error("批量删除信息failed受影响的id："+messageIds);
			return ResponseDTO.fail("failed", null, null, "000039");
		}
    }
    
    /**
     * 	获取图片验证码接口
     *	 以BASE64转码的字符串传到前端
     */
    @RequestMapping("/batchModifyRead")
    @ResponseBody
	public ResponseDTO batchModifyRead(EcmInnerMessageVo ecmInnerMessageVo) {
    	List<Integer> messageIds = ecmInnerMessageVo.getMessageIds();
    	boolean batchModifyRead = ecmInnerMessageService.batchModifyRead(ecmInnerMessageVo);
    	if (batchModifyRead) {
    		return ResponseDTO.ok("批量标记已读success");
		}else {
			log.error("批量标记已读failed受影响的id："+messageIds);
			return ResponseDTO.fail("failed", null, null, "000039");
		}
    }
}	
