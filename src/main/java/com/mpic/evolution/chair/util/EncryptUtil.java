package com.mpic.evolution.chair.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import com.mpic.evolution.chair.common.constant.SecretKeyConstants;

/**
 * 
 * 类名称：  EncryptUtil   
 * 描述：加密机密工具  
 *@author  wusc   
 * 创建时间：  2018年11月23日 下午1:08:36 
 * 修改备注：
 *
 */
public class EncryptUtil {
    
    /**
     *默认 email名长度
     */
    private static final int DEFAULT_MIN_EMAIL_LENGTH = 3;
    /**
     * 默认手机号码长度
     */
    private static final int DEFAULT_PHONE_NUMBER_LENGTH = 11;
    /**
     * 默认手机号码长度
    */
   private static final int DEFAULT_ID_CARD_LENGTH = 18;
    
    /**
     * 
      * 方法名：  md5 
      * 描述：  md5加密
     * @author  wusc   
      * 创建时间：2018年11月23日 下午1:32:05
     * @param inStr
     * @return
     *
     */
    public static String md5(String inStr) {
        return encrypt(inStr, "MD5");
    }

    /**
     * 
      * 方法名：  sha 
      * 描述：  sha加密
     * @author  wusc   
      * 创建时间：2018年11月23日 下午6:27:27
     * @param inStr
     * @return
     *
     */
    public static String sha(String inStr) {
        return encrypt(inStr, "SHA1");
    }

    /**
     * 
      * 方法名：  encode 
      * 描述：  加密基础方法
     * @author  wusc   
      * 创建时间：2018年11月23日 下午6:27:41
     * @param inStr
     * @param encodeType
     * @return
     *
     */
    private static String encrypt(String inStr, String encodeType) {
        try {
            MessageDigest digest = MessageDigest.getInstance(encodeType);
            char[] charArray = inStr.toCharArray();
            byte[] byteArray = new byte[charArray.length];

            for (int i = 0; i < charArray.length; i++) {
                byteArray[i] = (byte) charArray[i];
            }

            byte[] digestArray = digest.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < digestArray.length; i++) {
                int val = ((int) digestArray[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }

                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            return null;
        }

    }

    
    /**
     * 
      * 方法名：  aesEncrypt 
      * 描述：  
     * @author  wusc   
      * 创建时间：2018年11月23日 下午8:49:10
     * @param content
     * @param password
     * @return
     * @throws Exception 
     *
     */
    public static String aesEncrypt(String content, String password) throws Exception {
        return aesBase(content, password, Cipher.ENCRYPT_MODE);
    }
    
    /**
     * 
      * 方法名：  aesDecrypt 
      * 描述：  
     * @author  wusc   
      * 创建时间：2018年11月23日 下午8:49:13
     * @param content
     * @param password
     * @return
     * @throws Exception 
     *
     */
    public static String aesDecrypt(String content, String password) throws Exception {
        return aesBase(content, password, Cipher.DECRYPT_MODE);
    }
    
    /**
     * 
      * 方法名：  aesBase 
      * 描述：  aesBase 加密解密基础
     * @author  wusc   
      * 创建时间：2018年11月23日 下午8:47:20
     * @param content
     * @param password
     * @param cipherMode
     * @return
     * @throws Exception
     *
     */
    private static String aesBase(String content, String password, int cipherMode) throws Exception {
        String result = null;
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        kgen.init(128, secureRandom);

        byte[] keyPtr = new byte[16];
        IvParameterSpec ivParam = new IvParameterSpec(keyPtr);
        byte[] passPtr = password.getBytes();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        int defaultBitSize = 16;
        for (int i = 0; i < defaultBitSize; ++i) {
            if (i < passPtr.length)
                keyPtr[i] = passPtr[i];
            else {
                keyPtr[i] = 0;
            }
        }
        SecretKeySpec keySpec = new SecretKeySpec(keyPtr, "AES");

        switch (cipherMode) {
            case Cipher.DECRYPT_MODE:
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParam);
                result = new String(cipher.doFinal(hex2byte(content)));
                break;
            case Cipher.ENCRYPT_MODE:
                byte[] byteContent = content.getBytes("UTF-8");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParam);
                result = byte2Hex(cipher.doFinal(byteContent));
                break;
            default:
                break;
        }

        return result;
    }

    /**
     * 
      * 方法名：  hex2byte 
      * 描述：  hex to byte
     * @author  wusc   
      * 创建时间：2018年11月23日 下午8:45:33
     * @param strhex
     * @return
     *
     */
    public static byte[] hex2byte(String strhex) {
        int mod = 2;
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % mod == 1) {
            return null;
        }
        byte[] b = new byte[l / mod];
        for (int i = 0; i != l / mod; ++i) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * mod, i * 2 + mod), 16);
        }
        return b;
    }

    /**
     * 
      * 方法名：  byte2Hex 
      * 描述：  byte to hex
     * @author  wusc   
      * 创建时间：2018年11月23日 下午8:45:37
     * @param b
     * @return
     *
     */
    private static String byte2Hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; ++n) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    /**
     * 
      * 方法名：  base64Encryption 
      * 描述：  base64加密
     * @author  wusc   
      * 创建时间：2018年11月23日 下午6:38:50
     * @param inStr
     * @return
     *
     */
    public static String base64Encrypt(String inStr) {
        try {
            return Base64.getEncoder().encodeToString(inStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
      * 方法名：  base64Decryption 
      * 描述：   base64解密
     * @author  wusc   
      * 创建时间：2018年11月23日 下午6:40:30
     * @param inStr
     * @return
     *
     */
    public static String base64Decrypt(String inStr) {
        try {
            return new String(Base64.getDecoder().decode(inStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
      * 方法名：  hiddenPhoneNumber 
      * 描述：  前三后四，隐藏手机号码中间部分
     * @author  wusc   
      * 创建时间：2018年11月26日 上午10:15:54
     * @param inStr
     * @param sensitiveInfoType
     * @return
     * @throws Exception
     *
     */
    public static String hiddenPhoneNumber(String number )  {
        if (StringUtils.isEmpty(number) 
                || RegularUtils.isNotDigst(number) 
                || (number.length() != DEFAULT_PHONE_NUMBER_LENGTH) ) {
            return null;
        }
        return number.substring(0, 3)+generateAsterisk(4)+number.substring(7);
    }
    
    
    /**
     * 
     * 
      * 方法名：  hiddenEmail 
      * 描述：  
     * @author  wusc   
      * 创建时间：2018年11月26日 上午10:37:31
     * @param email
     * @return
     * @throws Exception 
     *
     */
    public static String hiddenEmail(String email){
        if (RegularUtils.isNotEmail(email)) {
            return null;
        }
        StringBuilder emailDeal = new StringBuilder();
        String[] emailArray = email.split("@");
        int len = emailArray[0].length();
        if (len > DEFAULT_MIN_EMAIL_LENGTH) {
            emailDeal.append(emailArray[0].substring(0, DEFAULT_MIN_EMAIL_LENGTH)).append(generateAsterisk(3));
        } else {
            emailDeal.append(emailArray[0]).append(generateAsterisk(3));
        }
        return emailDeal.append("@").append(emailArray[1]).toString();
    }
    
    /**
     * 
      * 方法名：  generateAsterisk 
      * 描述：  生成多个*号
     * @author  wusc   
      * 创建时间：2018年11月26日 上午11:29:57
     * @param length
     * @return
     *
     */
    private static String generateAsterisk(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }

    
    /**
     * 
      * 方法名：  hiddenIdCard 
      * 描述：  异常身份证
     * @author  wusc   
      * 创建时间：2018年11月26日 下午12:55:42
     * @param idStr
     * @return
     *
     */
    public static String hiddenIdCard(String idStr) {
        
        StringBuilder builder = new StringBuilder();
        if (idStr.length() != DEFAULT_ID_CARD_LENGTH) {
            builder.append(idStr.substring(0, 1)).append(generateAsterisk(12))
            .append(idStr.substring(idStr.length() - 1));
        } else {
            builder.append(idStr.substring(0, 4)).append(generateAsterisk(12))
            .append(idStr.substring(idStr.length() - 2));
        }
        return builder.toString();
    }
    
    public static void main(String[] args) throws Exception {
    	String aesEncrypt = EncryptUtil.aesEncrypt("18895397505", SecretKeyConstants.secretKey);
    	String aesDecrypt = EncryptUtil.aesDecrypt("C1F293236B49949322DBA88C2EE2B429", SecretKeyConstants.secretKey);
    	System.out.println(aesEncrypt);
    	System.out.println(aesDecrypt);
        System.out.println(EncryptUtil.aesEncrypt("15213064486", SecretKeyConstants.secretKey));
	}
}
