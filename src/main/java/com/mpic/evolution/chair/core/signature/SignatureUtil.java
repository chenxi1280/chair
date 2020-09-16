package com.mpic.evolution.chair.core.signature;

import java.util.Random;

/**
 *
 * 类名:  SignatureUtil
 * @author Xuezx
 * @date 2019/9/24 0024 8:46
 * 描述: 获得云点播签名的工具类
 */
public class SignatureUtil {


    public static String getUploadSignature(String id, String key ){
        Signature sign = new Signature();
        sign.setSecretId(id);
        sign.setSecretKey(key);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(Integer.MAX_VALUE));
        sign.setSignValidDuration(60);
        sign.setProcedure("change540mp4");
        sign.setTaskNotifyMode("Finish");

        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
            return signature;
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过子账户的id和key获取云点播签名，这个签名是有时效的，也是每个账户不同的，也是全平台公用的
     * @return 签名
     */
    public static String getUploadSignature(){

        return getUploadSignature("AKIDebOJ4yeOIiuc5HRmIXKqY3KK2YmtxDUT",
                "EIBqMaWCnOjHdhPu4zfGXcXCfz0qQimY");
    }
}
