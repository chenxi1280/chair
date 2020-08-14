package com.mpic.evolution.chair.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式

            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(
			httpURLConnection.getOutputStream()); printWriter.write(param.toString());
			//flush输出流的缓冲 
			printWriter.flush();
            // 开始获取数据
            BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            byte[] bytes = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(bytes);
            String content = new String(bytes);
            // 能被转成json 表示获取二维码失败
            if (HttpMpicUtil.isJsonObject(content)) {
            	return content;
			}else {
				String base64Str = Base64.encodeBase64String(bytes);
	            return base64Str;
			}
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
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
        if(StringUtils.isBlank(content))
            return false;
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}
