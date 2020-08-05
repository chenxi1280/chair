package com.mpic.evolution.chair.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.service.SendSMSService;

/**
 * @author SJ
 */

@Controller
public class SendSMSController {
	
	@Resource
	SendSMSService sendSMSService;
	
	@RequestMapping("/sendSMS")
	@ResponseStatus(HttpStatus.OK)
	public ResponseDTO sendSms() {
		return sendSMSService.sendSMS();
	}
}
