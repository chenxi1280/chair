package com.mpic.evolution.chair.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-14 16:22:23 
*/

public class HttpMpicUtil {
	/**
     * 返回base64位
     *
     * @param uri 发送请求的 uri
     * @param param  请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendPostForBase64(String uri, JSONObject param) {
        PrintWriter printWriter = null;
        InputStream inStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        String result = "";
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            printWriter = new PrintWriter(
			urlConnection.getOutputStream()); printWriter.write(param.toString());
			//flush输出流的缓冲 
			printWriter.flush();
            inStream = urlConnection.getInputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byte[] data;
            data = byteArrayOutputStream.toByteArray();
            String content = new String(data);
            // 能被转成json 表示获取二维码失败
            return HttpMpicUtil.isJsonObject(content) ? content : Base64.encodeBase64String(data);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (inStream != null) {
                    inStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    /**
    * 判断字符串是否可以转化为json对象
    * @param content
    * @return
    */
    public static boolean isJsonObject(String content) {
        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if(StringUtils.isBlank(content)){
            return false;
        }
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}
