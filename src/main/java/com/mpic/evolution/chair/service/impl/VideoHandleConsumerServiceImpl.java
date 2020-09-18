package com.mpic.evolution.chair.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.service.VideoHandleConsumerService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureRequest;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureResponse;
import org.springframework.stereotype.Service;

import static com.mpic.evolution.chair.common.constant.CosConstant.*;

/**
 * 类名称： VideoHandleConsumerServiceImpl
 *
 * @author xuezx
 * @date 2020/9/18 15:36
 * 类描述：
 */
@Service
public class VideoHandleConsumerServiceImpl implements VideoHandleConsumerService {

    /**
      * 方法名:
      * @author Xuezx (◔‸◔）
      * @param videoCode 事实上是腾讯云的videoId
      * @date 2020/9/18 15:57
      * 方法描述:
     * 向 腾讯云 请求处理某个作品的任务流
      */
    @Override
    public void handleOneVideo(String videoCode) {
        try{

            Credential cred = new Credential(SECRET_ID, SECRET_KEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(TC_API);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);

            JSONObject params = new JSONObject();
            params.put("FileId", videoCode);
            params.put("ProcedureName", CHANGE_PIPELINT);

            ProcessMediaByProcedureRequest req = ProcessMediaByProcedureRequest.fromJsonString(params.toJSONString(), ProcessMediaByProcedureRequest.class);

            ProcessMediaByProcedureResponse resp = client.ProcessMediaByProcedure(req);

            System.out.println(ProcessMediaByProcedureResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }
}
