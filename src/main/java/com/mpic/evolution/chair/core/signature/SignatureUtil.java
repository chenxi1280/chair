package com.mpic.evolution.chair.core.signature;

import java.util.Random;

import static com.mpic.evolution.chair.common.constant.CosConstant.SECRET_ID;
import static com.mpic.evolution.chair.common.constant.CosConstant.SECRET_KEY;

/**
 *
 * 类名:  SignatureUtil
 * @author Xuezx
 * @date 2019/9/24 0024 8:46
 * 描述: 获得云点播签名的工具类
 */
public class SignatureUtil {


    public static String getUploadSignature(){
        Signature sign = new Signature();
        sign.setSecretId(SECRET_ID);
        sign.setSecretKey(SECRET_KEY);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(Integer.MAX_VALUE));
        sign.setSignValidDuration(60);
        // sign.setProcedure("change540mp4"); 暂时不在上传之后立即开始任务流
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

}
