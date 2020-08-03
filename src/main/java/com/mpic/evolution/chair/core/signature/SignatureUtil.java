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
        sign.setSignValidDuration(3600 * 24 * 2);

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

    public static String getUploadSignature(){

        return getUploadSignature("AKIDNs9B1a3HUSFmMgJeIgneFpnYAWcRGfKm",
                "MXYlmOeLm9KErRk1TfKj7E4oImzvlKsA");
    }
}
