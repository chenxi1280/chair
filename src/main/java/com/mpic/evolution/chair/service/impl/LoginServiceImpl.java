package com.mpic.evolution.chair.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.jfinal.kit.HttpKit;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.service.LoginService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;

import static com.mpic.evolution.chair.common.constant.CommonField.STRING_LOWER_CASE_ERRCODE;

/**
 *
 * @author SJ
 *
 */

@Service
public class LoginServiceImpl implements LoginService{

	@Value("${wx.pc.fw.accessTokenUrl}")
    private String pcAccessTokenUrl;

    @Value("${wx.pc.fw.userInfoUrl}")
    private String pcUserInfoUrl;

    @Value("${wx.appid}")
    private String pcAppID;

    @Value("${wx.appsecret}")
    private String pcAppsecret;

    @Value("${sms.appid}")
	private String appid;// 腾讯短信应用的 SDK AppID

	@Value("${sms.appkey}")
	private String appkey;// 腾讯云短信中的 SDK AppKey

	@Value("${sms.templateId}")
	private String templateId;// 模板id

	@Value("${sms.smsSign}")
	private String smsSign;// 签名内容

	@Value("${sms.secretId}")
	private String secretId;// 密钥对

	@Value("${sms.secretKey}")
	private String secretKey;// 密钥对

	@Override
	public SendStatus sendSMS(String code, String[] phoneNumbers) throws TencentCloudSDKException {
		/*
		 * 必要步骤： 实例化一个认证对象，入参需要传入腾讯云账户密钥对 secretId 和 secretKey
		 */
		Credential cred = new Credential(secretId, secretKey);
		HttpProfile httpProfile = new HttpProfile();
		httpProfile.setReqMethod("POST");
		ClientProfile clientProfile = new ClientProfile();
		/*
		 * SDK 默认用 TC3-HMAC-SHA256 进行签名 非必要请不要修改该字段
		 */
		clientProfile.setSignMethod("HmacSHA256");
		clientProfile.setHttpProfile(httpProfile);
		SmsClient client = new SmsClient(cred, "ap-chongqing", clientProfile);
		SendSmsRequest req = new SendSmsRequest();
		req.setSmsSdkAppid(appid);
		req.setSign(smsSign);
		req.setTemplateID(templateId);
		req.setPhoneNumberSet(phoneNumbers);
		String[] templateParams = { code };
		req.setTemplateParamSet(templateParams);
		SendSmsResponse res = client.SendSms(req);
//		System.out.println(SendSmsResponse.toJsonString(res));
		SendStatus sendStatus = res.getSendStatusSet()[0];
		return sendStatus;
	}

	@Override
	public DefaultKaptcha getConfirmCode() {
		return this.produce();

	}

	private DefaultKaptcha produce() {
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "no");// 图片边框
		properties.setProperty("kaptcha.border.color", "white");// 边框颜色
		properties.setProperty("kaptcha.textproducer.font.color", "255,165,0");// 字体颜色
		properties.setProperty("kaptcha.image.width", "125");// 图片宽
		properties.setProperty("kaptcha.image.height", "45");// 图片高
		properties.setProperty("kaptcha.session.key", "code");// session key
		properties.setProperty("kaptcha.textproducer.font.size", "38");// 字体大小
		properties.setProperty("kaptcha.noise.color", "173,255,47");
		properties.setProperty("kaptcha.background.clear.from", "211,211,211");
		properties.setProperty("kaptcha.background.clear.to", "255,255,255");
		properties.setProperty("kaptcha.textproducer.char.length", "4");// 验证码长度
		properties.setProperty("kaptcha.textproducer.font.names", "Arial");// 字体
		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);
		return defaultKaptcha;
	}

	@Override
	public ResponseDTO loginByWeiXin(String code) {
		 Map<String, String> res = new HashMap<>(8);

	        if ( StringUtils.isBlank(code)) {
	        	return ResponseDTO.fail("微信code为空", null, 501, null);
	        }
	        if (code != null) {
	            // 第一次进入界面，code不空，openid为空，根据code获取openid，然后查询是否存在用户信息。
	            Map<String, String> accessTokenMap = getPcWXAccessToken(code);
	            // 获取getWXAccessToken（微信网站PC扫码登录）
	            /** 请求微信服务器错误 **/
	            if (accessTokenMap.get(STRING_LOWER_CASE_ERRCODE) != null) {
	            	String str = accessTokenMap.get("errcode");
	            	int errcode = Integer.parseInt(str);
	                return ResponseDTO.fail(accessTokenMap.get("errmsg"), null,errcode, null);
	            }
	            String accessToken = accessTokenMap.get("access_token");
	            String openid = accessTokenMap.get("openid");
//	            System.out.println("accessToken:"+accessToken);
//	            System.out.println("openid:"+openid);
	            // 查询出微信信息
	            Map<String, String> wxUserMap = this.getPcWeiXinUserInfo(openid, accessToken); // 获得微信用户信息
	            res = wxUserMap;
	            /** 获取微信信息异常 **/
	            if (wxUserMap.get(STRING_LOWER_CASE_ERRCODE) != null) {
	                String str = wxUserMap.get("errcode");
	                int errcode = Integer.parseInt(str);
	                return ResponseDTO.fail(wxUserMap.get("errmsg"), null, errcode, null);
	            }
	            //TODO 比对验证用户信息与数据库中存储的信息未完成

	        }
	        return ResponseDTO.ok("登陆成功", res);
	}

	private Map<String, String> getPcWXAccessToken(String code) {
        Map<String, String> resMap = new HashMap<String, String>(8);
        StringBuffer target = new StringBuffer();
        target.append(pcAccessTokenUrl).append("appid=").append(pcAppID).append("&secret=").append(pcAppsecret)
                .append("&code=").append(code).append("&grant_type=authorization_code");
        String jsonStr = HttpKit.get(target.toString());
        JSONObject jSONObject = JSONObject.parseObject(jsonStr);
        String errCodeStr = "errcode";
        String errMsgStr = "errmsg";
        if (jSONObject != null && jSONObject.get(errCodeStr) != null) { // 有错误码
            String errcode = String.valueOf(jSONObject.get(errCodeStr));
            String errmsg = String.valueOf(jSONObject.get(errMsgStr));
            resMap.put(errMsgStr, errmsg);
            resMap.put(errCodeStr, errcode);
        } else {
            String accessToken = String.valueOf(jSONObject.get("access_token"));
            String refreshToken = String.valueOf(jSONObject.get("refresh_token"));
            String openid = String.valueOf(jSONObject.get("openid"));
            String expiresIn = String.valueOf(jSONObject.get("expires_in"));
            String unionid = String.valueOf(jSONObject.get("unionid"));

            resMap.put("access_token", accessToken);
            resMap.put("refresh_token", refreshToken);
            resMap.put("openid", openid);
            resMap.put("expires_in", expiresIn);
            resMap.put("unionid", unionid);
        }
        return resMap;
    }

    /**
     * 	获得微信用户信息（微信网站PC扫码）
     *	@author SJ
     * @param openId
     * @param accessToken
     * @return
     */
    private Map<String, String> getPcWeiXinUserInfo(String openId, String accessToken) {
        Map<String, String> resMap = new HashMap<String, String>(8);
        StringBuffer url = new StringBuffer(pcUserInfoUrl);
        url.append("access_token=").append(accessToken).append("&").append("openid=").append(openId).append("&")
                .append("lang=zh_CN");
        String jsonStr = HttpKit.get(url.toString());
        JSONObject jSONObject = JSONObject.parseObject(jsonStr);
		String errCodeStr = "errcode";
		String errMsgStr = "errmsg";
        if (jSONObject != null && jSONObject.get(errCodeStr) != null) {
            String errcode = String.valueOf(jSONObject.get(errCodeStr));
            String errmsg = String.valueOf(jSONObject.get(errMsgStr));
            resMap.put(errMsgStr, errmsg);
            resMap.put(errCodeStr, errcode);
        } else {
            String nickname = String.valueOf(jSONObject.get("nickname"));
            String openid = String.valueOf(jSONObject.get("openid"));
            String sex = String.valueOf(jSONObject.get("sex"));
            String province = String.valueOf(jSONObject.get("province"));
            String city = String.valueOf(jSONObject.get("city"));
            String country = String.valueOf(jSONObject.get("country"));
            String headimgurl = String.valueOf(jSONObject.get("headimgurl"));
            String unionid = String.valueOf(jSONObject.get("unionid"));

            resMap.put("nickname", nickname);
            resMap.put("openid", openid);
            resMap.put("sex", sex);
            resMap.put("province", province);
            resMap.put("city", city);
            resMap.put("country", country);
            resMap.put("headimgurl", headimgurl);
            resMap.put("unionid", unionid);
        }
        return resMap;
    }


}
