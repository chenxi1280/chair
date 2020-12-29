package com.mpic.evolution.chair.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.common.constant.JudgeConstant;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.tencent.video.AiContentReviewResultSet;
import com.mpic.evolution.chair.pojo.tencent.video.BaseTask;
import com.mpic.evolution.chair.pojo.tencent.video.TencentVideoResult;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.service.VideoHandleConsumerService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureRequest;
import com.tencentcloudapi.vod.v20180717.models.ProcessMediaByProcedureResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.transform.sax.SAXResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.mpic.evolution.chair.common.constant.CommonField.STRING_REDIS_IP;
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


    @Resource
    EcmArtworkNodesDao ecmArtworkNodesDao;

    @Value("${spring.redis.host}")
    private String redisHost;// 腾讯ai审核需要使用


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
            System.out.println("redisHost:"+redisHost);
            // 测试环境ip
            if (!STRING_REDIS_IP.equals(redisHost)){
                params.put("SubAppId", 1500001548);
            }else {
                System.out.println("这是测试环境的AI审核");
            }

            ProcessMediaByProcedureRequest req = ProcessMediaByProcedureRequest.fromJsonString(params.toJSONString(), ProcessMediaByProcedureRequest.class);

            ProcessMediaByProcedureResponse resp = client.ProcessMediaByProcedure(req);

            System.out.println(ProcessMediaByProcedureResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }



    @Override
    public ResponseDTO videoHandleConsumer(TencentVideoResult tencentVideoResult) {
        System.out.println("腾讯视频审核回调接口开始工作了");

        if (tencentVideoResult.getProcedureStateChangeEvent().getErrCode() == 0 && JudgeConstant.SUCCESS.toUpperCase().equals(tencentVideoResult.getProcedureStateChangeEvent().getMessage())){

            List<EcmArtworkNodesVo> ecmArtworkNodesVoList = ecmArtworkNodesDao.selectByVideoCode(tencentVideoResult.getProcedureStateChangeEvent().getFileId());
            List<EcmArtworkNodesVo> ecmArtworkNodesList = new ArrayList<>();
            for (EcmArtworkNodesVo ecmArtworkNodesVo : ecmArtworkNodesVoList) {
                EcmArtworkNodesVo ecmArtworkNode = new EcmArtworkNodesVo();
                ecmArtworkNodesVo.setVideoUrl(tencentVideoResult.getProcedureStateChangeEvent().getMediaProcessResultSet().get(0).getTranscodeTask().getOutput().getUrl());
                List<AiContentReviewResultSet> aiContentReviewResultSet = tencentVideoResult.getProcedureStateChangeEvent().getAiContentReviewResultSet();
                for (AiContentReviewResultSet aiContentReviewResult : aiContentReviewResultSet) {
                    try {
                        Method method = aiContentReviewResult.getClass().getMethod("get" + aiContentReviewResult.getType().replace(".", "") + "Task");
                        BaseTask invoke = (BaseTask) method.invoke(aiContentReviewResult);
                        ecmArtworkNodesVo.setFkEndingId(1);
                        if (invoke != null) {
                            if (!"pass".equals(invoke.getOutput().getSuggestion())) {
                                ecmArtworkNodesVo.setFkEndingId(2);
                                break;
                            }
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                ecmArtworkNode.setPkDetailId(ecmArtworkNodesVo.getPkDetailId());
                ecmArtworkNode.setFkEndingId(ecmArtworkNodesVo.getFkEndingId());
                ecmArtworkNode.setVideoUrl(ecmArtworkNodesVo.getVideoUrl());
                ecmArtworkNodesList.add(ecmArtworkNode);

            }
            ecmArtworkNodesDao.updateByEcmArtworkNodesList(ecmArtworkNodesList);
//            ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodesVo);

        }

        return null;
    }

    @Override
    @Async("taskExecutor")
    public void handleArtwork(Integer pkArtworkId) {
        List<EcmArtworkNodesVo> ecmArtworkNodesVos = ecmArtworkNodesDao.selectByArtWorkId(pkArtworkId);
        if (!CollectionUtils.isEmpty(ecmArtworkNodesVos)){
            List<EcmArtworkNodesVo> collect = ecmArtworkNodesVos.stream().filter(ecmArtworkNodesVo -> !"Y".equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());
            collect.forEach( ecmArtworkNodesVo ->  {
                if (!StringUtils.isEmpty(ecmArtworkNodesVo.getVideoCode())){
                    this.handleOneVideo(ecmArtworkNodesVo.getVideoCode());
                    System.out.println(ecmArtworkNodesVo.getVideoCode() + "已发送审核");
                }
            });
        }

    }
}
