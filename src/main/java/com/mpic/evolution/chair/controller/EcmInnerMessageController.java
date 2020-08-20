package com.mpic.evolution.chair.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jfinal.template.expr.ast.Map;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmInnerMessageService;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-20 10:48:54 
*/
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
	public ResponseDTO getInnerMessage(EcmUserVo user) {
    	//求和信息
    	List<EcmInnerMessage> messages = ecmInnerMessageService.getInnerMessage(user);
    	if (messages == null || messages.isEmpty()) {
			return ResponseDTO.fail("获取message失败");
		}
    	messages = messages.stream().filter((EcmInnerMessage m)->m.getMessageStatus()<2).collect(Collectors.toList());
		messages.sort((EcmInnerMessage m1, EcmInnerMessage m2)->m2.getSendDate().compareTo(m1.getSendDate()));
		//TODO 分组操作
		return ResponseDTO.ok("获取成功", messages);	
    }
    
    /**
     * 	获取图片验证码接口
     *	 以BASE64转码的字符串传到前端
     */
    @RequestMapping("/deleteInnerMessage")
    @ResponseBody
	public ResponseDTO deleteInnerMessage(EcmInnerMessage ecmInnerMessage) {
    	boolean flag = ecmInnerMessageService.deleteInnerMessage(ecmInnerMessage);
		return null;
    	
    }
}	
