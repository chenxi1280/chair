package com.mpic.evolution.chair.core.signature;

import com.tencent.cloud.CosStsClient;
import org.json.JSONObject;

import java.util.TreeMap;

import static com.mpic.evolution.chair.common.constant.CosConstant.*;


/**
 *
 * 类名:  CosSignatureUtils
 * @author Xuezx
 * @date 2020/6/12 0012 14:26
 * 描述: 用于对象存储获取临时签名档工具类
 */
public class CosSignatureUtils {


    /**
     *
     * 方法名:  getSignature
     * @author Xuezx
     * @date 2020/6/12 0012 14:27
     * @return org.json.JSONObject
     * 描述: 获取临时签名
     */
    public static JSONObject getSignature(){

        TreeMap<String, Object> config = new TreeMap<>();

        try {
            // 替换为您的 SecretId
            config.put("SecretId", SECREID);
            // 替换为您的 SecretKey
            config.put("SecretKey",  SECRETKEY);

            // 临时密钥有效时长，单位是秒，默认1800秒，最长可设定有效期为7200秒
            config.put("durationSeconds", 1800);

            // 换成您的 bucket
            config.put("bucket", BUCKETNAME);
            // 换成 bucket 所在地区
            config.put("region", REGION);

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，例子：a.jpg 或者 a/* 或者 * 。
            // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
            config.put("allowPrefix", "*");

            // 密钥的权限列表。简单上传、表单上传和分片上传需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[] {
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);

            JSONObject credential = CosStsClient.getCredential(config);
            //成功返回临时密钥信息，如下打印密钥信息
            System.out.println(credential);
            return credential;
        } catch (Exception e) {
            //失败抛出异常
            throw new IllegalArgumentException("no valid secret !");
        }


    }
}
