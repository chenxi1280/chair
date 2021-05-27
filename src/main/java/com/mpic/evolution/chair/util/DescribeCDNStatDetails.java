package com.mpic.evolution.chair.util;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;;

public class DescribeCDNStatDetails {

    private static String secretId = "AKIDNs9B1a3HUSFmMgJeIgneFpnYAWcRGfKm";

    private static String secretKey = "MXYlmOeLm9KErRk1TfKj7E4oImzvlKsA";

    public static void main(String [] args) {
        try{

            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);

            DescribeCDNStatDetailsRequest req = new DescribeCDNStatDetailsRequest();
            req.setMetric("Traffic");
            //代表5月17号的早上8点 iso 8601表示的UTC时间 换算成北京时间 utc时间值 + 8个小时
            req.setStartTime("2021-05-17T00:00:00Z");
            //代表5月18号的早上7点59分59秒 iso 8601表示的UTC时间 换算成北京时间 utc时间值 + 8个小时
            req.setEndTime("2021-05-17T23:59:59Z");
            req.setArea("Chinese Mainland");
            req.setSubAppId(1500005184L);

            DescribeCDNStatDetailsResponse resp = client.DescribeCDNStatDetails(req);

            System.out.println(DescribeCDNStatDetailsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

    }

}