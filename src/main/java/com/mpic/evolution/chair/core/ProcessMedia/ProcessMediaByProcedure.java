package com.mpic.evolution.chair.core.ProcessMedia;

import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureRequest;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureResponse;
import org.springframework.stereotype.Component;

import static com.mpic.evolution.chair.common.constant.CosConstant.*;

/**
 * 类名称： ProcessMediaByProcedure
 *
 * @author: admin
 * @date 2020/8/18 14:49
 * 类描述：
 */
@Component
public class ProcessMediaByProcedure {

    /**
      * 方法名:getUnHandeledVideo
      * @author Xuezx (◔‸◔）
      * @date 2020/8/18 15:00
      * 方法描述: 定时任务，捞取库中未审核的视频，并将之开始任务流
      */
    private void getUnHandeledVideo(){

    }




    /**
      * 方法名: handleMedia
      * @author Xuezx (◔‸◔）
      * @param videoCode 腾讯云给每一个video随机生成的id，在我们这里叫做code
      * @date 2020/8/18 14:57
      * 方法描述: 将未转码和审核的视频，交给腾讯进行自动任务流，这些视频会存在redis里面
      */
    private void handleOneMedia(String videoCode, String SessionContext){
        try{

            Credential cred = new Credential(SECRET_ID, SECRET_KEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(TC_API);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, REGION, clientProfile);

            JSONObject params = new JSONObject();
            params.put("FileId", videoCode);
            params.put("ProcedureName", "转码540mp4");

            ProcessMediaByProcedureRequest req = ProcessMediaByProcedureRequest.fromJsonString(params.toJSONString(), ProcessMediaByProcedureRequest.class);
            ProcessMediaByProcedureResponse resp = client.ProcessMediaByProcedure(req);
            System.out.println(ProcessMediaByProcedureResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }
}
