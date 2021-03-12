package com.mpic.evolution.chair.config;

import com.github.wxpay.sdk.WXPayUtil;
import com.mpic.evolution.chair.util.DeviceUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by cxd
 * @Classname WxPayConfig
 * @Description TODO
 * @Date 2021/3/9 14:08
 */
public class WxPayConfig {

    public static final String APP_ID = "wxa001a9842ad0f851";
    public static final String MCH_ID = "1607156332";
    public static final String API3_KEY = "sb5cj6ovc3yhe96ypxlnictxhaoupohi";
    // 微信支付异步通知接口
    public static final String DOM_URL_TEST = "https://wanxiangchengzhen.com/bpi/callpay.action";
    public static final String DOM_URL = "https://wanxiangchengzhen.com/pbpi/callpay.action";


    public static final String  APIKEY = "sb5cj6ovc3yhe96ypxlnictxhaoupohi";



    //获得客户端访问的IP地址
    public static String getIpAddr() {
        HttpServletRequest request = DeviceUtil.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
