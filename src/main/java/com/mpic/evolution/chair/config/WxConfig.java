package com.mpic.evolution.chair.config;

import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayUtil;
import com.mpic.evolution.chair.util.DeviceUtil;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author by cxd
 * @Classname WxPayConfig
 * @Description TODO
 * @Date 2021/3/9 14:08
 */
public class WxConfig  {

    public static final String APPID = "wxa001a9842ad0f851";
    public static final String MCH_ID = "1607156332";

    public static final String DOM_URL = "https://wanxiangchengzhen.com/bpi/callpay.action";

    public static final String  APIKEY = "sb5cj6ovc3yhe96ypxlnictxhaoupohi";


    // 微信支付异步通知接口
    public static final String NOTIFY_URL = "https://wanxiangchengzhen.com/bpi/callpay.action";
    // 微信没有同步通知接口:付款之后,还得自己去查询订单状态是否改变,所以还需要自己写.








    /**
     * 告诉微信异步通知的时候，告诉微信我收到了，你不要再给我发用户付款成功的消息了
     *
     * @return
     */
    public static String notifyWxImGet() {
        Map<String, String> resMap = new HashMap<>();
        resMap.put("return_code", "SUCCESS");
        resMap.put("return_msg", "OK");
        //将map转换成xml字符串返回给微信，我收到了，你别再发了
        try {
            return WXPayUtil.mapToXml(resMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


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
