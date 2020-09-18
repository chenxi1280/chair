package com.mpic.evolution.chair.core.ProcessMedia;

import static com.mpic.evolution.chair.common.constant.CosConstant.REGION;
import static com.mpic.evolution.chair.common.constant.CosConstant.SECRET_ID;
import static com.mpic.evolution.chair.common.constant.CosConstant.SECRET_KEY;
import static com.mpic.evolution.chair.common.constant.CosConstant.TC_API;

import java.util.List;

import javax.annotation.Resource;

import com.mpic.evolution.chair.dao.EcmUserFlowDao;
import com.mpic.evolution.chair.pojo.entity.EcmUserFlow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.dao.ProcessMediaByProcedureDao;
import com.mpic.evolution.chair.pojo.vo.MediaByProcedureVo;
import com.mpic.evolution.chair.util.RedisUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureRequest;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 类名称： ProcessMediaByProcedure
 *
 * @author: admin
 * @date 2020/8/18 14:49
 * 类描述：
 */
@Slf4j
@Component
//@EnableScheduling
public class ProcessMediaByProcedure {

    @Resource
    private
    ProcessMediaByProcedureDao processMediaByProcedureDao;

    @Resource
    private RedisUtil redisUtil;

    /**
      * 方法名:getUnHandeledVideo
      * @author Xuezx (◔‸◔）
      * @date 2020/8/18 15:00
      * 方法描述: 定时任务10分钟一次，捞取库中10条未审核的视频，并将之存入redis队列
     * 开始任务流
      * 以后有mq后把这个存到mq里
      */
    @Scheduled(fixedRate=60*1000*10)
    private void getUnHandledVideo(){
          
        List<MediaByProcedureVo> res = processMediaByProcedureDao.getUnHandledVideo();
        return;
        /*
        for( MediaByProcedureVo vo : res){
            String videoCode = vo.getVideoCode();
            if(StringUtils.isBlank(videoCode)){
                log.info("获取视频处理任务流数据错误，id为=" + vo.getPipelineId());
                continue;
            }
            //先存入redis，避免多节点重复操作
            String hasValue = String.valueOf(redisUtil.get(videoCode));
            if(StringUtils.isNotBlank(hasValue)){
                continue;
            }
            redisUtil.set(vo.getVideoCode(), "value", 60*60);
            handleOneMedia(videoCode, null);
        }
*/
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
            if(StringUtils.isNotBlank(SessionContext)){
                params.put("SessionContext", SessionContext);
            }

            ProcessMediaByProcedureRequest req = ProcessMediaByProcedureRequest.fromJsonString(params.toJSONString(), ProcessMediaByProcedureRequest.class);
            ProcessMediaByProcedureResponse resp = client.ProcessMediaByProcedure(req);
            System.out.println(ProcessMediaByProcedureResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }




}
