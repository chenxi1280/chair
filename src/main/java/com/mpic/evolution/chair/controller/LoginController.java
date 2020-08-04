package com.mpic.evolution.chair.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Value("${wx.pc.fw.accessTokenUrl}")
    private String pcAccessTokenUrl;

    @Value("${wx.pc.fw.userInfoUrl}")
    private String pcUserInfoUrl;

    @Value("${wx.appid}")
    private String pcAppID;

    @Value("${wx.appsecret}")
    private String pcAppsecret;


    @RequestMapping(value = "/loginByWeiXin", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO loginByWeiXin(@RequestParam String state, @RequestParam String code) {
        Map<String, String> res = new HashMap<>();
        
        if ( StringUtils.isBlank(code)) {
        	return ResponseDTO.fail("微信code为空", null, 501, null);
        }
        if (code != null) {
            // 第一次进入界面，code不空，openid为空，根据code获取openid，然后查询是否存在用户信息。
            Map<String, String> accessTokenMap = getPcWXAccessToken(code); // 获取getWXAccessToken（微信网站PC扫码登录）
            /** 请求微信服务器错误 **/
            if (accessTokenMap.get("errcode") != null) {
            	String str = accessTokenMap.get("errcode");
            	int errcode = Integer.parseInt(str);
                return ResponseDTO.fail(accessTokenMap.get("errmsg"), null,errcode, null);
            }
            String accessToken = accessTokenMap.get("access_token");
            String openid = accessTokenMap.get("openid");
            System.out.println("accessToken:"+accessToken);
            System.out.println("openid:"+openid);
            // 查询出微信信息
            Map<String, String> wxUserMap = this.getPcWeiXinUserInfo(openid, accessToken); // 获得微信用户信息
            res = wxUserMap;
            /** 获取微信信息异常 **/
            if (wxUserMap.get("errcode") != null) {
                String str = wxUserMap.get("errcode");
                int errcode = Integer.parseInt(str);
                return ResponseDTO.fail(wxUserMap.get("errmsg"), null, errcode, null);
            }


        }
        return ResponseDTO.ok("登陆成功", res);
    }

    /**
     * 	获取getPcWXAccessToken（微信网站PC扫码）
     *
     */
    private Map<String, String> getPcWXAccessToken(String code) {
        Map<String, String> resMap = new HashMap<String, String>();
        StringBuffer target = new StringBuffer();
        target.append(pcAccessTokenUrl).append("appid=").append(pcAppID).append("&secret=").append(pcAppsecret)
                .append("&code=").append(code).append("&grant_type=authorization_code");
        String jsonStr = HttpKit.get(target.toString());
        JSONObject jSONObject = JSONObject.parseObject(jsonStr);
        if (jSONObject != null && jSONObject.get("errcode") != null) { // 有错误码
            String errcode = String.valueOf(jSONObject.get("errcode"));
            String errmsg = String.valueOf(jSONObject.get("errmsg"));
            resMap.put("errmsg", errmsg);
            resMap.put("errcode", errcode);
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
     * 获得微信用户信息（微信网站PC扫码）
     *
     * @param openId
     * @param accessToken
     * @return
     */
    private Map<String, String> getPcWeiXinUserInfo(String openId, String accessToken) {
        Map<String, String> resMap = new HashMap<String, String>();
        StringBuffer url = new StringBuffer(pcUserInfoUrl);
        url.append("access_token=").append(accessToken).append("&").append("openid=").append(openId).append("&")
                .append("lang=zh_CN");
        String jsonStr = HttpKit.get(url.toString());
        JSONObject jSONObject = JSONObject.parseObject(jsonStr);
        if (jSONObject != null && jSONObject.get("errcode") != null) {
            String errcode = String.valueOf(jSONObject.get("errcode"));
            String errmsg = String.valueOf(jSONObject.get("errmsg"));
            resMap.put("errmsg", errmsg);
            resMap.put("errcode", errcode);
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
