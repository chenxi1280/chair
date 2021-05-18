package com.mpic.evolution.chair.service;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;

public interface NoticeService {

    /**
     * @author SJ
     *	 发送下行流量相关短信通知
     * @throws TencentCloudSDKException
     */
    SendStatus sendSMS(String code, String[] phoneNumbers,String templateId) throws TencentCloudSDKException;
}
