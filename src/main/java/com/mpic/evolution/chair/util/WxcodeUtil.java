package com.mpic.evolution.chair.util;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import com.mpic.evolution.chair.common.constant.publishConstants;

import io.netty.util.internal.StringUtil;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-13 17:22:47 
*/
public class WxcodeUtil {
	/**
     * 	通过请求获取accessToken
     *	用户登陆时的token 解密出来的用户id
     * 	此处要根据过期时间，存redis，有人请求，先从redis里面拿token，没有再请求
     *
     */
    private static String getAccessToken(String userId) {
    	String requestUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?"
    			+ "grant_type=client_credential&appid=%s&secret=%s", publishConstants.appid,publishConstants.secret);
        //将返回的access_token 存入redis 过期时间3000秒
    	String jsonStr = HttpKit.get(requestUrl);
    	JSONObject result = JSONObject.parseObject(jsonStr);
    	String accessToken = result.getString("access_token");
    	RedisUtil redisUtil = new RedisUtil();
    	redisUtil.set(userId, accessToken, 3000L);
        return accessToken;
    }

    /**
     * 	这里面的 scene 参数是前台要传过来的videoId
     */
    public static void getWxcode (String token) {
    	String userId = JWTUtil.getUserId(token);
    	RedisUtil redisUtil = new RedisUtil();
    	String accessToken = String.valueOf(redisUtil.get(userId));
    	//如果是null返回false
    	if (!StringUtil.isNullOrEmpty(accessToken)) {
    		accessToken = getAccessToken(userId);
		}
        String url = String.format("https://api.weixin.qq.com/wxa/getwxacodeunlimit?"
        		+ "access_token=%s", accessToken);
        JSONObject param = new JSONObject();
        param.put("page","pages/play/play");
        param.put("scene","12345");
        wxPost(url, param);
    }
    
    /**
     * 	更具参数获取二维码
     * @param uri
     * @param param
     * @param fileName
     */
    private static void wxPost(String uri, JSONObject param) {
        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(
                    httpURLConnection.getOutputStream());
            printWriter.write(param.toString());
            // flush输出流的缓冲
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
		WxcodeUtil.getWxcode("2^&Ra5s6ary76r8RS*rs^rT8");
	}
}
