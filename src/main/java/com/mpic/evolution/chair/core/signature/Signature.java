package com.mpic.evolution.chair.core.signature;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Random;

import static com.mpic.evolution.chair.common.constant.CosConstant.SECRET_ID;
import static com.mpic.evolution.chair.common.constant.CosConstant.SECRET_KEY;

/**
 *
 * 类名:  Signature
 * @author Xuezx
 * @date 2019/9/23 0023 17:08
 * 描述: 小程序请求调用云点播接口签名类
 */
public class Signature {

    private String secretId;
    private String secretKey;
    private long currentTime;
    private int random;
    private int signValidDuration;
    /**
     *  	视频后续任务处理操作，即完成视频上传后，可自动发起任务流操作。参数值为任务流模板名，云点播支持 创建任务流模板 并为模板命名。
     */
    private String procedure;

    /**
     *任务流状态变更通知模式（仅当指定了 procedure 时才有效）。
     Finish：只有当任务流全部执行完毕时，才发起一次事件通知。
     Change：只要任务流中每个子任务的状态发生变化，都进行事件通知。
     None：不接受该任务流回调。
     默认为 Finish
     */
    private String taskNotifyMode;
    private static final String HMAC_ALGORITHM = "HmacSHA1";
    private static final String CONTENT_CHARSET = "UTF-8";

    public static byte[] byteMerger(byte[] byte1, byte[] byte2) {
        byte[] byte3 = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
        return byte3;
    }

    public String getUploadSignature() throws Exception {
        String strSign = "";
        String contextStr = "";

        long endTime = (currentTime + signValidDuration);
        contextStr += "secretId=" + java.net.URLEncoder.encode(secretId, "utf8");
        contextStr += "&currentTimeStamp=" + currentTime;
        contextStr += "&expireTime=" + endTime;
        contextStr += "&random=" + random;
        contextStr += "&procedure=" + procedure;
        contextStr += "&vodSubAppId=" + "1500001548";
        contextStr += "&taskNotifyMode=" + taskNotifyMode;

        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(this.secretKey.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
            mac.init(secretKey);

            byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
            byte[] sigBuf = byteMerger(hash, contextStr.getBytes("utf8"));
            strSign = base64Encode(sigBuf);
            strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");
        } catch (Exception e) {
            throw e;
        }
        return strSign;
    }

    private String base64Encode(byte[] buffer) {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(buffer);
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public void setSignValidDuration(int signValidDuration) {
        this.signValidDuration = signValidDuration;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public void setTaskNotifyMode(String taskNotifyMode) {
        this.taskNotifyMode = taskNotifyMode;
    }




    public  static void test() {
        Signature sign = new Signature();
        sign.setSecretId(SECRET_ID);
        sign.setSecretKey(SECRET_KEY);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(Integer.MAX_VALUE));
        sign.setSignValidDuration(60);
        sign.setProcedure("change540mp4");
        sign.setTaskNotifyMode("Finish");
        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
    	Signature.test();
	}
}


