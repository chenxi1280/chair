package com.mpic.evolution.chair.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.service.SendSMSService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;

/**
 * 
 * @author SJ
 *
 */

@Service
public class SendSMSServiceImpl implements SendSMSService {

	@Value("${sms.appid}")
	private String appid;// 腾讯短信应用的 SDK AppID

	@Value("${sms.appkey}")
	private String appkey;// 腾讯云短信中的 SDK AppKey

	@Value("${sms.templateId}")
	private String templateId;// 模板id

	@Value("${sms.smsSign}")
	private String smsSign;// 签名内容
	
	@Value("${sms.secretId}")
	private String secretId;// 密钥对 
	
	@Value("${sms.secretKey}")
	private String secretKey;// 密钥对 
	
	private String confirmCode="";//验证码
	
	@Override
	public ResponseDTO sendSMS() {
		try {
			/*
			 * 必要步骤： 实例化一个认证对象，入参需要传入腾讯云账户密钥对 secretId 和 secretKey
			 */
			Credential cred = new Credential(secretId, secretKey);
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setReqMethod("POST");
			ClientProfile clientProfile = new ClientProfile();
			/*
			 * SDK 默认用 TC3-HMAC-SHA256 进行签名 非必要请不要修改该字段
			 */
			clientProfile.setSignMethod("HmacSHA256");
			clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-chongqing",clientProfile);
			SendSmsRequest req = new SendSmsRequest();
			req.setSmsSdkAppid(appid);
			req.setSign(smsSign);
			req.setTemplateID(templateId);
			String[] phoneNumbers = { "+8618895397505"};
			req.setPhoneNumberSet(phoneNumbers);
			Random random = new Random();
	        for (int i = 0; i < 6; i++) {
	        	confirmCode += random.nextInt(10);
	        }
			String[] templateParams = { confirmCode };
			req.setTemplateParamSet(templateParams);
			SendSmsResponse res = client.SendSms(req);
			System.out.println(SendSmsResponse.toJsonString(res));
			SendStatus sendStatus = res.getSendStatusSet()[0];
			if (!sendStatus.getCode().equals("Ok")) return ResponseDTO.fail(sendStatus.getMessage());
			return ResponseDTO.ok(sendStatus.getMessage());
		} catch (TencentCloudSDKException e) {
			e.printStackTrace();
			return ResponseDTO.fail("发送失败", null, null, 500);
		}	
	}

}
