package com.mpic.evolution.chair.service.impl;

import cn.hutool.core.util.XmlUtil;
import com.github.wxpay.sdk.WXPayUtil;
import com.mpic.evolution.chair.config.WxPayConfig;
import com.mpic.evolution.chair.controller.TestController;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmOrder;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;
import com.mpic.evolution.chair.service.EcmOrderHistoryService;
import com.mpic.evolution.chair.service.EcmOrderService;
import com.mpic.evolution.chair.service.EcmPayService;
import com.mpic.evolution.chair.util.HttpClient;
import com.mpic.evolution.chair.util.PayForUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mpic.evolution.chair.common.constant.CommonField.STRING_REDIS_IP;
import static com.mpic.evolution.chair.config.WxPayConfig.*;


/**
 * @author by cxd
 * @Classname EcmPaySericeImpl
 * @Description TODO
 * @Date 2021/3/9 9:20
 */
@Service
public class EcmPayServiceImpl implements EcmPayService {

    // 判断是否为真实环境
    @Value("${spring.redis.host}")
    private String redisHost;

    final
    EcmOrderService ecmOrderService;
    EcmOrderHistoryService ecmOrderHistoryService;
    TestController testController;

    public EcmPayServiceImpl(EcmOrderService ecmOrderService, EcmOrderHistoryService ecmOrderHistoryService, TestController testController) {
        this.ecmOrderService = ecmOrderService;
        this.ecmOrderHistoryService = ecmOrderHistoryService;
        this.testController = testController;
    }


    @Override
    public ResponseDTO wxPayQueryOrder(EcmOrderVO ecmOrderVO) {

        if (ecmOrderVO == null) {
            ResponseDTO.fail("非法商品");
        }

        //1、封装参数
        Map<String, String> data = getParameterMap(ecmOrderVO);
        try {
            // 此处指定为扫码支付
            data.put("trade_type", "NATIVE");
            //2、将参数转成xml字符，并携带签名
            String paramXml = WXPayUtil.generateSignedXml(data, API3_KEY);
            ///3、执行请求
            String wxPayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            HttpClient httpClient = new HttpClient(wxPayUrl);
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();
            //4、获取参数
            String content = httpClient.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(content);

            //5、获取部分页面所需参数
            Map<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("codeUrl", stringMap.get("code_url"));
            dataMap.put("orderCode", ecmOrderVO.getOrderCode());
            dataMap.put("orderPrice", String.valueOf(ecmOrderVO.getOrderPrice()));

            return ResponseDTO.ok(dataMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseDTO.fail("支付失败");
    }

    @Override
    public void wxPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 读取回调数据
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        // 解析xml成map
        Map<String, Object> m = XmlUtil.xmlToMap(sb.toString());
        // 过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator<String> it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = it.next();
            Object parameterValue = m.get(parameter);
            String v = "";
            if (null != parameterValue) {
                v = parameterValue.toString().trim();
            }
            packageParams.put(parameter, v);
        }

        // 微信支付的API密钥
        String key = APIKEY;
        // 判断签名是否正确
        if (PayForUtil.isTenpaySign(packageParams, key)) {
            String resXml = "";
            if ("SUCCESS".equals((String) packageParams.get("result_code"))) {

                System.err.println("--------------------------支付回调------------------");
                EcmOrderVO ecmOrderVO = new EcmOrderVO();
                // 支付成功,执行自己的业务逻辑开始
                String app_id = (String) packageParams.get("appid");
                String mch_id = (String) packageParams.get("mch_id");
                String openid = (String) packageParams.get("openid");
//                // 是否关注公众号
//                String is_subscribe = (String) packageParams.get("is_subscribe");
//                // 附加参数【商标申请_0bda32824db44d6f9611f1047829fa3b_15460】--【业务类型_会员ID_订单号】
//                String attach = (String) packageParams.get("attach");
                String out_trade_no = (String) packageParams.get("out_trade_no");
                String total_fee = (String) packageParams.get("total_fee");
//                // 微信支付订单号
                String transaction_id = (String) packageParams.get("transaction_id");
                EcmOrder ecmOrder = ecmOrderService.queryOrderInfo(out_trade_no);

                ecmOrderVO.setOrderCode(out_trade_no);
                ecmOrderVO.setUpdateTime(new Date());
                ecmOrderVO.setOrderState(1);
                ecmOrderVO.setOrderType(0);
                ecmOrderVO.setPayOrderId(transaction_id);
                ecmOrderService.updateOrderByPay(ecmOrderVO);
                System.err.println("正在执行执行业务逻辑");
                // 调用 业务判断
                System.out.println("total_fee" + "/n" + total_fee + "/n" + ecmOrder.getOrderPrice());
//                if (ecmOrder.getOrderPrice().equals(new BigDecimal(total_fee).divide(BigDecimal.valueOf(100)))) {
                    //执行业务并判断是否成功
                    if (ecmOrderService.savaVipPaymentInfo(out_trade_no)) {
                        ecmOrderVO.setOrderState(2);
                    } else {
                        System.out.println("支付成功，业务因为异常执行失败!!  订单code：" + out_trade_no);
                        ecmOrderVO.setOrderState(1);
                    }
//                } else {
//                    System.out.println("支付成功，业务执行错误!，订单金额和支付金额不符合！！订单code：" + out_trade_no);
//                    ecmOrderVO.setOrderState(3);
//                }
                ecmOrderService.updateOrderByPay(ecmOrderVO);
                ecmOrderHistoryService.insertOrderHistory(out_trade_no, total_fee);
                // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                response.getWriter().write(resXml);
                System.err.println("--------------------------------------------");
            } else {
                System.err.println("支付失败,错误信息：" + packageParams.get("err_code"));

                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            response.getWriter().write(resXml);
        } else {
            System.err.println("通知签名验证失败");
        }

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
        Map<String, String> data = new HashMap<>(12);
        //  appid
        data.put("appid", WxPayConfig.APP_ID);
        // 商户id
        data.put("mch_id", WxPayConfig.MCH_ID);
        //随机字符串
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        //描述 商品名称
        data.put("body", order.getGoodsName());
        //订单编号
        data.put("out_trade_no", String.valueOf(order.getOrderCode()));
        //订单有效支付时间yyyyMMddHHmmss,格式(必须)
        String youxiaoDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis() + 1000 * 60 * 5));
        // 订单有效支付时间
        data.put("time_expire", youxiaoDate);
        // int类型的金额单位是分
        int zonge = order.getOrderPrice().multiply(new BigDecimal("100")).intValue();
        //总金额
        data.put("total_fee", String.valueOf(zonge));
        //设置ip地址
        data.put("spbill_create_ip", getIpAddr());
        //支付结果通知路径异步通知路径

        if (!STRING_REDIS_IP.equals(redisHost)) {
            data.put("notify_url", WxPayConfig.DOM_URL);
        } else {
            data.put("notify_url", WxPayConfig.DOM_URL_TEST);
        }

        data.put("attach", String.valueOf(order.getPkOrderId()));

        return data;
    }

}
