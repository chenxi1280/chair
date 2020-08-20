package com.mpic.evolution.chair.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpic.evolution.chair.pojo.dto.EcmInnerMessageDto;
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
    	List<EcmInnerMessageDto> innerMessages = ecmInnerMessageService.getInnerMessage(user);
    	if (innerMessages == null || innerMessages.isEmpty()) {
			return ResponseDTO.fail("获取message失败");
		}
		return ResponseDTO.ok("获取成功", innerMessages);	
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
