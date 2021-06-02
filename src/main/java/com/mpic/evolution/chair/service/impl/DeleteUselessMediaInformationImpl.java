package com.mpic.evolution.chair.service.impl;

import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtwork;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.service.DeleteUselessMediaInformationService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DeleteUselessMediaInformationImpl implements DeleteUselessMediaInformationService {

    @Resource
    private EcmArtworkDao ecmArtworkDao;

    @Resource
    private EcmArtworkNodesDao ecmArtworkNodesDao;

    @Value("${sms.secretId}")
    private String secretId;// 密钥对

    @Value("${sms.secretKey}")
    private String secretKey;// 密钥对

    /**
     * @author SJ
     * 对外暴露的方法
     * @return
     */
    public ResponseDTO deleteUselessMediaInformation(){
        Set<String> deletedList = this.selectUselessMediaInformation();
        this.selectMediaInfosFromTencentCloud(deletedList);
        return ResponseDTO.ok("删除成功");
    }

    /**
     * @author SJ
     * 从数据库中查询出无用的媒体信息
     * @return
     */
    private Set<String> selectUselessMediaInformation(){
        //由于当前的服务我们在删除作品时没有对该作品的节点进行标记所以我们要找到删除的作品id 然后查询他所有的节点 这些节点都是应该被删除的 同时他的节点也应该都被标记上已删除的标记
        //同时当前服务有删除某个作品节点的功能 因此出现作品未删除 但是节点删除的问题 需要对已删除的节点做单独查询  筛选仔细
        EcmArtwork ecmArtwork = new EcmArtwork();
        short status = 5;
        ecmArtwork.setArtworkStatus(status);
        //查出所有的已删除的作品id
        List<EcmArtwork> ecmArtworks = ecmArtworkDao.selectDeletedArtWorks(ecmArtwork);
        ArrayList<Integer> ecmArtworkIds = new ArrayList<>();
        ecmArtworks.forEach(item->{
            ecmArtworkIds.add(item.getPkArtworkId());
        });
        //已被删除的子节点对象的总集合
        ArrayList<String> ecmArtworkNodesTotalDeletedList = new ArrayList<>();
        //根据已删除的作品id 查出所有的对应子节点
        List<String> ecmArtworkNodesDeletedList = ecmArtworkNodesDao.selectDeletedArtWorkNodesByArtworkIds(ecmArtworkIds);
        ecmArtworkNodesTotalDeletedList.addAll(ecmArtworkNodesDeletedList);
        EcmArtworkNodes ecmArtworkNodes = new EcmArtworkNodes();
        ecmArtworkNodes.setIsDeleted("Y");
        //根据节点的已删除状态  查出所有已删除的节点
        List<String> ecmArtworkNodesDeletedList2 = ecmArtworkNodesDao.selectDeletedArtWorkNodesByNodesStatus(ecmArtworkNodes);
        ecmArtworkNodesTotalDeletedList.addAll(ecmArtworkNodesDeletedList2);
        Set<String> deletedList = new HashSet<>(ecmArtworkNodesTotalDeletedList);
        return deletedList;
    }

    /**
     *  @author SJ
     * 查询云点播无用的媒体信息 根据查询出来的videoCode 去清理腾讯云上无用的数据
     * @return
     */
    private void selectMediaInfosFromTencentCloud(Set<String> deletedList){
        long subAppId = 1259692143;
        long offset = 0;
        long limit = 5000;
        try{
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);

            SearchMediaRequest req = new SearchMediaRequest();
//            for test check request result
//            for (int k = 0; k < 5; k++) {
            for(;;){
                req.setOffset(offset);
                req.setLimit(limit);
                req.setSubAppId(subAppId);

                SearchMediaResponse resp = client.SearchMedia(req);
                MediaInfo[] mediaInfoSet = resp.getMediaInfoSet();
                if(mediaInfoSet == null || mediaInfoSet.length == 0){
                    break;
                }
                for (int i = 0; i < mediaInfoSet.length; i++) {
                    String fileId = mediaInfoSet[i].getFileId();
                    if(deletedList.contains(fileId)){
                        this.deleteUselessMediaInfosFromTencentCloud(fileId);
                    }
                }
                offset += limit;
            }

//            System.out.println(fieldIds.size());
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

    /**
     *  @author SJ
     * 删除云点播无用的媒体信息
     * @return
     */
    private void deleteUselessMediaInfosFromTencentCloud(String fileId){
        //根据查询出来的videoCode 去清理腾讯云上无用的数据
        try{

            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);

            DeleteMediaRequest req = new DeleteMediaRequest();
            req.setFileId(fileId);

            DeleteMediaResponse resp = client.DeleteMedia(req);

            System.out.println(DeleteMediaResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

    private void deleteUselessMediaInfosFromDataBase(String fileId){
        short status = 5;
        String isDeleted = "Y";
        ecmArtworkNodesDao.deleteByIsDelete(isDeleted);
        ecmArtworkDao.deleteByStatus(status);
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(5);
        list.add(3);
        list.add(3);
        list.add(7);
        list.add(4);
        list.add(4);
        list.add(2);
        list.add(8);
        list.forEach(System.out::println);
        System.out.println("*****************************************");
        HashSet<Integer> integers = new HashSet<>(list);
        integers.forEach(System.out::println);
    }

}
