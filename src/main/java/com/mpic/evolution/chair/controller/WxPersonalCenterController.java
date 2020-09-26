package com.mpic.evolution.chair.controller;

import javax.annotation.Resource;

import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import com.mpic.evolution.chair.common.constant.publishConstants;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.service.WxPersonalCenterService;
import com.mpic.evolution.chair.util.HttpMpicUtil;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.RedisUtil;
import com.qcloud.vod.common.StringUtil;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-4 9:57:20 
*/

@Controller
@RequestMapping("/wxPersonalCenter")
public class WxPersonalCenterController {
	
	@Resource
	EcmUserService ecmUserService;
	
	@Resource
    WxPersonalCenterService personalCenterService;
    
    @Resource
	RedisUtil redisUtil;

	/**
	 * 
	 * @param ecmArtWorkQuery 自带分页
	 * @return com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: sunjie
	 *	个人中心根据用户token获取用户个人信息
	 * @Date: 2020/9/1
	 */
	@RequestMapping("/getWxUserInfo")
	@ResponseBody
	public ResponseDTO getWxUserInfo(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
		String token = ecmArtWorkQuery.getToken();
		String userIdStr = JWTUtil.getUserId(token);
		if(StringUtils.isBlank(userIdStr) || !NumberUtils.isParsable(userIdStr)){
    		return ResponseDTO.fail("游客进入");
    	}
		Integer userId = Integer.parseInt(userIdStr);
		EcmUser user = new EcmUser();
		user.setPkUserId(userId);
		user = ecmUserService.getUserInfos(user);
		return ResponseDTO.ok("获取用户信息成功", user);
	}
	

	/**
	 * @
	 * @param ecmArtWorkQuery 自带分页
	 * @return com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: sunjie
	 * 	根据用户的token获取个人中的个人作品
	 * @Date: 2020/9/1
	 */
	@RequestMapping("/getWxUserArtWorks")
	@ResponseBody
	public ResponseDTO getWxUserArtWorks(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
		String token = ecmArtWorkQuery.getToken();
		if(StringUtils.isBlank(token)){
    		return ResponseDTO.fail("未登录账号");
    	}
		String userIdStr = JWTUtil.getUserId(token);
		if(StringUtils.isBlank(userIdStr) || !NumberUtils.isParsable(userIdStr)){
    		return ResponseDTO.fail("未登录账号");
    	}
		Integer userId = Integer.parseInt(userIdStr);
    	ecmArtWorkQuery.setFkUserid(userId);
		return personalCenterService.getWxUserArtWorks(ecmArtWorkQuery);
	}
	
	/**
     * 	获取发布微信二维码
     * 	这里面的 scene 参数是前台要传过来的videoId
     * 	
     * @author: SJ
     * @Date: 2020/8/14
     * @param ecmArtWorkQuery token
     */
    @RequestMapping("/getWxcode")
    @ResponseBody
    public ResponseDTO getWxcode (@RequestBody EcmArtWorkQuery ecmArtWorkQuery) {
    	String token = ecmArtWorkQuery.getToken();
    	String userId = JWTUtil.getUserId(token);
    	if (StringUtil.isEmpty(userId)){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
    	//如果是null返回false
    	boolean hasKey = redisUtil.hasKey("QRCode");
    	String accessToken = "";
    	try {
	    	if (hasKey) {
	    		accessToken = String.valueOf(redisUtil.get("QRCode"));
			}else {
				accessToken = getAccessToken();
			}
	    	String qrCodeStr = this.getQRCode(accessToken,ecmArtWorkQuery);
			if (HttpMpicUtil.isJsonObject(qrCodeStr)) {
				//返回的结果是：{"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest rid: 5f364b21-395edb8d-336ae042"}
				JSONObject result = JSONObject.parseObject(qrCodeStr);
				if(result.get("errcode").equals("40001")) {
					accessToken = getAccessToken();
					qrCodeStr = this.getQRCode(accessToken,ecmArtWorkQuery);
					String str = "data:image/jpg;base64," + qrCodeStr;
					return ResponseDTO.ok("获取发布二维码成功",str);
				}else {
					return ResponseDTO.fail("获取发布二维码失败", result.get("errmsg"),null,(Integer)result.get("errcode"));
				}
			}else {
				String str = "data:image/jpg;base64," + qrCodeStr;
				System.out.println(str);
				return ResponseDTO.ok("获取发布二维码成功",str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDTO.fail("获取二维码图片失败");
		}
    } 

	/**
     * 	通过请求获取accessToken
     *	用户登陆时的token 解密出来的用户id
     * 	此处要根据过期时间，存redis，有人请求，先从redis里面拿token，没有再请求
     *
     */
    private String getAccessToken() {
    	String requestUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?"
    			+ "grant_type=client_credential&appid=%s&secret=%s", publishConstants.appid,publishConstants.secret);
        //将返回的access_token 存入redis 过期时间3000秒
    	String jsonStr = HttpKit.get(requestUrl);
    	JSONObject result = JSONObject.parseObject(jsonStr);
    	String accessToken = result.getString("access_token");
    	redisUtil.set("QRCode", accessToken, 3000L);
        return accessToken;
    }
    
    private String getQRCode(String accessToken,EcmArtWorkQuery ecmArtWorkQuery) {
    	String url = String.format("https://api.weixin.qq.com/wxa/getwxacodeunlimit?"
        		+ "access_token=%s", accessToken);
        JSONObject param = new JSONObject();
        param.put("page","pages/play/play");
        Integer artWorkId = ecmArtWorkQuery.getPkArtworkId();
        String string = "artWorkId="+artWorkId+"=status=4";
        param.put("scene",string);
		String Base64Str = HttpMpicUtil.sendPostForBase64(url, param);
		return Base64Str;
    }
	
}
