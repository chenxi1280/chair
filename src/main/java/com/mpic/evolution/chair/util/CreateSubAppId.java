package com.mpic.evolution.chair.util;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;;

public class CreateSubAppId {
    public static void main(String [] args) {
        try{

            Credential cred = new Credential("AKIDNs9B1a3HUSFmMgJeIgneFpnYAWcRGfKm", "MXYlmOeLm9KErRk1TfKj7E4oImzvlKsA");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);

            CreateSubAppIdRequest req = new CreateSubAppIdRequest();
            req.setName("1619");

            CreateSubAppIdResponse resp = client.CreateSubAppId(req);

            System.out.println(CreateSubAppIdResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

    }

}
