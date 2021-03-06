package com.mpic.evolution.chair.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.service.EcmDownLinkFlowService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.CreateSubAppIdRequest;
import com.tencentcloudapi.vod.v20180717.models.CreateSubAppIdResponse;
import com.tencentcloudapi.vod.v20180717.models.DescribeCDNStatDetailsRequest;
import com.tencentcloudapi.vod.v20180717.models.DescribeCDNStatDetailsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EcmDownLinkFlowServiceImpl implements EcmDownLinkFlowService {

    @Value("${sms.secretId}")
    private String secretId;// 密钥对

    @Value("${sms.secretKey}")
    private String secretKey;// 密钥对

    @Override
    public long describeCDNStatDetails(String sTime, String eTime, long subAppId){

        try{
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);

            DescribeCDNStatDetailsRequest req = new DescribeCDNStatDetailsRequest();
            req.setMetric("Traffic");
            // "2021-05-17T00:00:00Z" 代表5月17号的早上8点 iso 8601表示的UTC时间 换算成北京时间 utc时间值 + 8个小时
            req.setStartTime(sTime);
            // "2021-05-17T23:59:59Z" 代表5月18号的早上7点59分59秒 iso 8601表示的UTC时间 换算成北京时间 utc时间值 + 8个小时
            req.setEndTime(eTime);
            req.setArea("Chinese Mainland");
            req.setSubAppId(subAppId);

            DescribeCDNStatDetailsResponse resp = client.DescribeCDNStatDetails(req);
            System.out.println(DescribeCDNStatDetailsResponse.toJsonString(resp));
            JSONObject object = JSONObject.parseObject(CreateSubAppIdResponse.toJsonString(resp));
            String array = object.get("Data").toString();
            JSONArray data = JSONArray.parseArray(array);
            long sum = 0;
            for(int i = 0; i < data.size(); i++){
                JSONObject value = JSONObject.parseObject(data.get(i).toString());
                sum += Long.parseLong(value.get("Value").toString());
            }
            return sum;
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return 0;
        }
    }

    @Override
    public Long createSubAppId(String name){
        try{
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);

            CreateSubAppIdRequest req = new CreateSubAppIdRequest();
            req.setName(name);

            CreateSubAppIdResponse resp = client.CreateSubAppId(req);
            JSONObject object = JSONObject.parseObject(CreateSubAppIdResponse.toJsonString(resp));
            Long subAppId = Long.parseLong(object.get("SubAppId").toString());
            return subAppId;
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return null;
        }
    }

}
