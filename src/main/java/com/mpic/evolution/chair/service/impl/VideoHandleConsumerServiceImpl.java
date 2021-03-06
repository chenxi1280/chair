package com.mpic.evolution.chair.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.common.constant.JudgeConstant;
import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.dao.EcmDownlinkFlowDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtwork;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.entity.EcmDownlinkFlow;
import com.mpic.evolution.chair.pojo.tencent.video.AiContentReviewResultSet;
import com.mpic.evolution.chair.pojo.tencent.video.BaseTask;
import com.mpic.evolution.chair.pojo.tencent.video.ProcedureStateChangeEvent;
import com.mpic.evolution.chair.pojo.tencent.video.TencentVideoResult;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.service.VideoHandleConsumerService;
import com.mpic.evolution.chair.util.RedisUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.transform.sax.SAXResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.mpic.evolution.chair.common.constant.CommonField.STRING_REDIS_IP;
import static com.mpic.evolution.chair.common.constant.CosConstant.*;

/**
 * ???????????? VideoHandleConsumerServiceImpl
 *
 * @author xuezx
 * @date 2020/9/18 15:36
 * ????????????
 */
@Service
public class VideoHandleConsumerServiceImpl implements VideoHandleConsumerService {



    @Resource
    EcmArtworkDao ecmArtworkDao;

    @Resource
    EcmArtworkNodesDao ecmArtworkNodesDao;

    @Resource
    EcmDownlinkFlowDao ecmDownlinkFlowDao;

    // ??????ai??????????????????
    @Value("${spring.redis.host}")
    private String redisHost;

    @Autowired
    private ExecutorService executorService;

    @Resource
    RedisUtil redisUtil;


    /**
      * ?????????:
      * @author Xuezx (????????????
      * @param videoCode ????????????????????????videoId
      * @date 2020/9/18 15:57
      * ????????????:
     * ??? ????????? ????????????????????????????????????
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
            // ????????????ip
            if (!STRING_REDIS_IP.equals(redisHost)){
                params.put("SubAppId", 1500001548);
            }else {
                System.out.println("?????????????????????AI??????");
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
        System.out.println("?????????????????????????????????????????????");

//        ecmArtworkDao.selectByPrimaryKey()

        if (tencentVideoResult.getProcedureStateChangeEvent().getErrCode() == 0 && JudgeConstant.SUCCESS.toUpperCase().equals(tencentVideoResult.getProcedureStateChangeEvent().getMessage())){
            // ??????????????????????????????
            Integer artworkId = ecmArtworkDao.selectByVideoCode(tencentVideoResult.getProcedureStateChangeEvent().getFileId());
            List<EcmArtworkNodesVo> ecmArtworkNodesVoList = ecmArtworkNodesDao.selectByVideoCode(tencentVideoResult.getProcedureStateChangeEvent().getFileId());
            if (artworkId != null ) {
                System.out.println("??????????????????????????????copyVideoByNodeList");
                copyVideoByNodeList(ecmArtworkNodesVoList,artworkId);
            }

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
                // ??????????????????
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
                    System.out.println(ecmArtworkNodesVo.getVideoCode() + "???????????????");
                }
            });
        }

    }


    @Override
    public void copyVideo(Integer pkArtworkId) {
        List<EcmArtworkNodesVo> ecmArtworkNodesVos = ecmArtworkNodesDao.selectByArtWorkId(pkArtworkId);
        copyVideoByNodeList(ecmArtworkNodesVos,pkArtworkId);
    }

    @Override
    public void copyVideoByNodeList(List<EcmArtworkNodesVo> ecmArtworkNodesVos,Integer pkArtworkId ) {
        if (!CollectionUtils.isEmpty(ecmArtworkNodesVos)){
            EcmDownlinkFlow ecmDownlinkFlow = new EcmDownlinkFlow();
            EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(pkArtworkId);
            ecmDownlinkFlow.setFkUserId(ecmArtwork.getFkUserid());
            ecmDownlinkFlow = ecmDownlinkFlowDao.selectByRecord(ecmDownlinkFlow);
            int subjectId = ecmDownlinkFlow.getSubAppId();
            List<EcmArtworkNodesVo> collect = ecmArtworkNodesVos.stream().filter(ecmArtworkNodesVo ->  StringUtils.isEmpty(ecmArtworkNodesVo.getPrivateVideoUrl())).collect(Collectors.toList());
            collect.forEach( ecmArtworkNodesVo ->  {
                if(ecmArtworkNodesVo.getVideoUrl() != null) {
                    //?????????????????????
                    this.tencentCopyUrl(ecmArtworkNodesVo.getVideoUrl(), subjectId, ecmArtworkNodesVo.getPkDetailId());
                    System.out.println(ecmArtworkNodesVo.getVideoUrl() + "????????????????????????????????????");
                }
            });
        }
    }

    private void tencentCopyUrl(String videoUrl, long subjectId, int detailId){
        try{

            VodClient client = tencentVodCredentialInit();

            PullUploadRequest req = new PullUploadRequest();
            req.setMediaUrl(videoUrl);
            req.setSubAppId(subjectId);

            PullUploadResponse resp = client.PullUpload(req);
            String taskId = resp.getTaskId();
            setRedis(taskId, subjectId, detailId);

            System.out.println("???taskId?????????");
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

    @PostConstruct
    private void copyUrlWork(){
        executorService.execute(this::doCopyRunner);
    }

    private void doCopyRunner()  {
        try {
            while (true) {
                Map<String, String> urlMap = (Map<String, String>) redisUtil.lPop("copy_url_list");
                if(urlMap != null && org.apache.commons.lang3.StringUtils.isNotBlank(urlMap.get("taskId"))){
                    //?????????
                    String taskId = urlMap.get("taskId");
                    String subjectId = urlMap.get("subjectId");
                    int detailId = Integer.parseInt(urlMap.get("detailId"));
                    callTencentTaskDetail(taskId, Long.parseLong(subjectId), detailId);
                } else {
                    System.out.println("?????????copy??????????????????"+ String.valueOf(new Date()));
                    Thread.sleep(30000);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            copyUrlWork();
        }
    }

    private void callTencentTaskDetail(String taskId, long subAppId, int detailId){
        try{

            VodClient client = tencentVodCredentialInit();

            DescribeTaskDetailRequest req = new DescribeTaskDetailRequest();
            req.setSubAppId(subAppId);
            req.setTaskId(taskId);

            DescribeTaskDetailResponse resp = client.DescribeTaskDetail(req);
            if(resp != null && resp.getPullUploadTask() != null && resp.getPullUploadTask().getMediaBasicInfo() != null){
                String url = resp.getPullUploadTask().getMediaBasicInfo().getMediaUrl();
                if(org.apache.commons.lang3.StringUtils.isNotBlank(url)){
                    // System.out.println(DescribeTaskDetailResponse.toJsonString(resp));1
                    EcmArtworkNodes nodes = new EcmArtworkNodes();
                    nodes.setPrivateVideoUrl(url);
                    nodes.setPkDetailId(detailId);
                    ecmArtworkNodesDao.updatePrivateVideoUrl(nodes);
                    System.out.println("??????????????????url???" + url);
                } else {
                    System.out.println("?????????????????????1");
                    setRedisTimeOut(taskId, subAppId, detailId);
                }
            }else {
                System.out.println("?????????????????????2, ????????????????????????");
                setRedisTimeOut(taskId, subAppId, detailId);
            }

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            System.out.println("?????????????????????3");
            setRedisTimeOut(taskId, subAppId, detailId);
        }


    }

    private VodClient tencentVodCredentialInit(){
        Credential cred = new Credential(SECRET_ID, SECRET_KEY);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(TC_API);

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        return new VodClient(cred, "", clientProfile);
    }

    /**
      * ?????????:
      * @author Xuezx (????????????
      * @param taskId
     * @param subAppId
     * @param detailId
      * @date 2021/5/26 14:16
      * ????????????: ???????????????????????????????????????????????????
      */
    private void setRedisTimeOut(String taskId, long subAppId, int detailId){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setRedis(taskId, subAppId, detailId);

    }

    private void setRedis(String taskId, long subAppId, int detailId){
        Map<String, String> urlMap = new HashMap<>(4);
        urlMap.put("taskId", taskId);
        urlMap.put("subjectId", String.valueOf(subAppId));
        urlMap.put("detailId", detailId + "");
        redisUtil.lSet("copy_url_list", urlMap);
    }
}
