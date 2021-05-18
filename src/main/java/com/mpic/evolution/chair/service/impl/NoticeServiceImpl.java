package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.service.NoticeService;
import com.mpic.evolution.chair.util.StringUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author SJ
 *
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Value("${sms.smsSign}")
    private String smsSign;// 签名内容

    @Value("${sms.secretId}")
    private String secretId;// 密钥对

    @Value("${sms.secretKey}")
    private String secretKey;// 密钥对

    @Value("${sms.appid}")
    private String appid;// 腾讯短信应用的 SDK AppID

    /**
     * 根据模板发送短信
     * @param code 短信中占位符的内容
     * @param phoneNumbers 用户手机号码
     * @param templateId 模板id 在腾讯云上先配置
     * @return sendStatus 发送的结果
     * @throws TencentCloudSDKException
     */

    @Override
    public SendStatus sendSMS(String code, String[] phoneNumbers,String templateId) throws TencentCloudSDKException {
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
        SmsClient client = new SmsClient(cred, "ap-chongqing", clientProfile);
        SendSmsRequest req = new SendSmsRequest();
        req.setSmsSdkAppid(appid);
        req.setSign(smsSign);
        req.setTemplateID(templateId);
        req.setPhoneNumberSet(phoneNumbers);
        if(StringUtils.isNullOrBlank(code)){
            String[] templateParams = {};
            req.setTemplateParamSet(templateParams);
        }else{
            String[] templateParams = { code };
            req.setTemplateParamSet(templateParams);
        }
        SendSmsResponse res = client.SendSms(req);
        SendStatus sendStatus = res.getSendStatusSet()[0];
        return sendStatus;
    }
}
