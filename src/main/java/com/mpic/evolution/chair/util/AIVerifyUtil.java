package com.mpic.evolution.chair.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.cms.v20190321.CmsClient;
import com.tencentcloudapi.cms.v20190321.models.TextModerationRequest;
import com.tencentcloudapi.cms.v20190321.models.TextModerationResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.mpic.evolution.chair.common.constant.CosConstant.SECRET_ID;
import static com.mpic.evolution.chair.common.constant.CosConstant.SECRET_KEY;

/**
 * 类名称： AIVerifyUtil
 *
 * @author: admin
 * @date 2020/8/24 16:17
 * 类描述：
 */
public class AIVerifyUtil {

    public static List<String> getDirtyWords (String content) {
        List<String> res = new ArrayList<>(16);
        String base64Content = Base64.getEncoder().encodeToString(content.getBytes());

        try{

            Credential cred = new Credential(SECRET_ID, SECRET_KEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("cms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CmsClient client = new CmsClient(cred, "ap-guangzhou", clientProfile);

            String params = "{\"Content\":\"" + base64Content + "\"}";

            TextModerationRequest req = TextModerationRequest.fromJsonString(params, TextModerationRequest.class);

            TextModerationResponse resp = client.TextModeration(req);

            JSONObject obj = JSON.parseObject(TextModerationResponse.toJsonString(resp));
            if(obj !=null && obj.get("Data")!=null){
                JSONArray array = (JSONArray) ((JSONObject)obj.get("Data")).get("DetailResult");
                if(array!=null && !array.isEmpty()){
                    for(Object o : array){
                        JSONArray oArray = (JSONArray)((JSONObject)o).get("Keywords");
                        if(!oArray.isEmpty()){
                            for(int i = 0; i < oArray.size(); i++){
                                res.add(oArray.getString(i));
                            }
                        }
                    }
                }
            }else if (obj !=null && obj.get("Error")!= null ){
                res.add("error");
            }

        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            res.add("error");
        }
        return res;
    }


    public static void main(String [] args) {
        List<String> res;
        res = getDirtyWords("他妈的八九的时候你还是个傻逼呢，我是吾尔开希，你吃婴儿，脑残，妈拉个巴子的，操你妈，草拟吗，草泥马，白花花的奶子");
        System.out.println(res);
        res = getDirtyWords("的，操你妈，草拟吗，草泥马，白花花的奶子");
        System.out.println(res);
        res = getDirtyWords("刘真？呵呵，我是文明人，我讲文明化");
        System.out.println(res);
        res = getDirtyWords("");
        System.out.println(res);

    }
}
