package com.mpic.evolution.chair.service.impl;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.mpic.evolution.chair.common.constant.WxConfig;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;
import com.mpic.evolution.chair.service.EcmPaySerice;
import com.mpic.evolution.chair.util.DeviceUtil;
import com.mpic.evolution.chair.util.HttpClient;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.mpic.evolution.chair.common.constant.WxConfig.getIpAddr;


/**
 * @author by cxd
 * @Classname EcmPaySericeImpl
 * @Description TODO
 * @Date 2021/3/9 9:20
 */
@Service
public class EcmPaySericeImpl implements EcmPaySerice {
    @Override
    public ResponseDTO wxPayQueryOrder(EcmOrderVO ecmOrderVO) {

        //1、封装参数
        Map<String, String> data = getParameterMap(ecmOrderVO);
        try {
            // 此处指定为扫码支付
            data.put("trade_type", "NATIVE");
            //2、将参数转成xml字符，并携带签名
            String paramXml = WXPayUtil.generateSignedXml(data, "sb5cj6ovc3yhe96ypxlnictxhaoupohi");
            ///3、执行请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();
            //4、获取参数
            String content = httpClient.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(content);
            System.out.println("stringMap:" + stringMap);

            //5、获取部分页面所需参数
            Map<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("code_url", stringMap.get("code_url"));
            dataMap.put("out_trade_no",ecmOrderVO.getOrderCode());
            dataMap.put("total_fee",String.valueOf(ecmOrderVO.getOrderPrice()));

            return ResponseDTO.ok(dataMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseDTO.fail("支付失败");
    }


    /**
     * 基础必传参数简单封装
     * <p>
     * appid            应用id
     * mch_id           商户id
     * nonce_str        随机字符串
     * body             主体
     * out_trade_no     订单号
     * time_expire      过期时间默认29分钟
     * total_fee        总金额，单位是分，传入BigDecimal
     * spbill_create_ip 用户ip地址
     * notify_url       微信异步通知地址
     */
    private Map<String, String> getParameterMap(EcmOrderVO order) {

        // 定义个装参数的map
        Map<String, String> data = new HashMap<>();
        //沙箱环境
        data.put("appid", WxConfig.APPID);//  appid
        data.put("mch_id", WxConfig.MCH_ID);// 商户id
        data.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串
        data.put("body", order.getGoodsName());//描述 商品名称
        data.put("out_trade_no", String.valueOf(order.getOrderCode()));//订单编号
        //订单有效支付时间yyyyMMddHHmmss,格式(必须)
        String youxiaoDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis() + 1000 * 60 * 2));
        data.put("time_expire", youxiaoDate);// 订单有效支付时间

        int zonge = order.getOrderPrice().multiply(new BigDecimal("100")).intValue();// int类型的金额单位是分

        data.put("total_fee", String.valueOf(zonge));//总金额
        //设置ip地址
        data.put("spbill_create_ip", getIpAddr());
        //支付结果通知路径
        data.put("notify_url", WxConfig.DOM_URL); // 异步通知路径
        return data;
    }

}
