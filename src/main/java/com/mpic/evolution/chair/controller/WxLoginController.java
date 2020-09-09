package com.mpic.evolution.chair.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mpic.evolution.chair.common.constant.SecretKeyConstants;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.entity.WxUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.pojo.vo.WxLoginVo;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.service.WxLoginService;
import com.mpic.evolution.chair.service.WxUserService;
import com.mpic.evolution.chair.util.EncryptUtil;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.MD5Utils;
import com.mpic.evolution.chair.util.RedisUtil;
import com.mpic.evolution.chair.util.StringUtils;
import com.mpic.evolution.chair.util.UUIDUtil;

import lombok.extern.log4j.Log4j;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-27 9:48:05 
*/

@Log4j
@Controller
public class WxLoginController {
	
	@Resource
	WxLoginService wxLoginService;
	
	@Resource
	RedisUtil redisUtil;
	
	@Resource
	EcmUserService ecmUserService;
	
	@Resource
	WxUserService wxUserService;
	
	/**
	 * 获取图片验证码接口 以BASE64转码的字符串传到前端
	 */
	@RequestMapping("/getWxConfirmCode")
	@ResponseBody
	public ResponseDTO getConfirmCode() {
		JSONObject data = new JSONObject();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] code = null;
			DefaultKaptcha produce = wxLoginService.getConfirmCode();
			String createText = this.validateCreateText(produce);
			String uuid = UUIDUtil.getUUID();
			// 存的时候不做处理 在获取不到时做报错提示
			redisUtil.lSet(uuid, createText, 300L);
			BufferedImage bi = produce.createImage(createText);
			ImageIO.write(bi, "jpg", out);
			code = out.toByteArray();
			String base64Str = Base64.encodeBase64String(code);
			data.put("imageCodeKey", uuid);
			data.put("base64Str", base64Str);
			return ResponseDTO.ok("获取验证码成功", data);
		} catch (IOException e) {
			log.error("获取图片验证码失败");
			e.printStackTrace();
			return ResponseDTO.fail("failed", null, null, "000039");
		}
	}
	
	/**
	 * 	小程序页面加载时调用此接口保存用户的openid
	 * @param params
	 * @return
	 */

    @ResponseBody
    @RequestMapping("/getOpenid")
    public ResponseDTO getOpenid(@RequestBody WxLoginVo wxLoginVo) {
        InputStream inputStream = null;
        JSONObject data = new JSONObject();
        try {
            URL url = new URL(String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", 
            		wxLoginVo.getAppid(),
            		wxLoginVo.getSecret(),
            		wxLoginVo.getCode()));
            URLConnection open = url.openConnection();
            inputStream = open.getInputStream();
            String result = org.apache.commons.io.IOUtils.toString(inputStream, "utf-8");
            JSONObject jsonObject = JSONObject.parseObject(result);
            //拿到openid
            String openid = jsonObject.getString("openid");
            if(StringUtils.isNullOrBlank(openid)){ 
                return ResponseDTO.fail("openid获取失败");
            }
            data.put("openid", openid);
            return ResponseDTO.ok("openid获取成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.fail("openid获取失败");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    
    /**
	 * 	保存wx游客用户的临时信息到wxUser表
	 * @param params
	 * @return
	 */
    @RequestMapping("/savaUserInfo")
	@ResponseBody
	public ResponseDTO savaWxUserInfo(@RequestBody WxUser wxUser) {
    	Integer fkUserId = wxUserService.savaWxUer(wxUser);
    	if (fkUserId != null) {
    		return ResponseDTO.ok("保存微信用户信息成功",fkUserId);
		}else {
			return ResponseDTO.fail("保存微信用户信息失败");
		}
    	
    }
	
	/**
	 * 登录接口 登陆成功之后 需要修改use表中的count(登录次数)，last_login_time(上一次登录时间)字段，update_time字段
	 * 
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/wxLogin")
	@ResponseBody
	public ResponseDTO loginByToken(@RequestBody EcmUserVo ecmUserVo) {
		JSONArray data = new JSONArray();
		EcmUser ecmUser = new EcmUser();
		EcmUser user = new EcmUser();
		EcmUserVo userVo = new EcmUserVo();
		try {
			ecmUser.setMobile(EncryptUtil.aesEncrypt(ecmUserVo.getMobile(), SecretKeyConstants.secretKey));
			ecmUser = ecmUserService.getUserInfos(ecmUser);
			// 判断账号是否存在
			if (ecmUser == null || StringUtils.isNullOrBlank(ecmUser.getPassword())) {
				data.add("505");
			}
			String confirmCode = ecmUserVo.getConfirmCode();
			String regionCode = String.valueOf(redisUtil.lPop(ecmUserVo.getImageCodeKey()));
			if (StringUtils.isNullOrEmpty(regionCode)) {
				data.add("501");
			} else {
				if (!regionCode.equals(confirmCode)) {
					data.add("501");
				}
			}
			// 账号不存在需要直接返回
			if (!data.isEmpty()) {
				return ResponseDTO.fail("登陆失败", data, null, 508);
			}
			if (ecmUser.getIsValid().equals("N")) {
				data.add("509");
			}
			String inputPwd = ecmUserVo.getPassword();
			String encrypt = MD5Utils.encrypt(inputPwd);
			String password = ecmUser.getPassword();
			if (!encrypt.equals(password)) {
				data.add("506");
			}
			if (!data.isEmpty()) {
				return ResponseDTO.fail("登陆失败", data, null, 508);
			}
			// 设置token
			String token = JWTUtil.sign(String.valueOf(ecmUser.getPkUserId()), ecmUser.getUsername(),
					SecretKeyConstants.jwtSecretKey);
			// 根据userId 修改用户信息	
			userVo.setPkUserId(ecmUser.getPkUserId());
			user.setLastLoginTime(new Date());
			user.setUpdateTime(new Date());
			ecmUserService.updateEcmUserById(user, userVo);
			return ResponseDTO.ok("登陆成功", token);
		} catch (MyBatisSystemException e) {
			log.error("账号在数据库中有多条记录");
			e.printStackTrace();
			return ResponseDTO.fail("failed", null, null, "000039");
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
			return ResponseDTO.fail("failed", null, null, "000039");
		}
	}

	/**
	 * 注册接口 注册成功还需要修改create_time字段和 update_time改字段
	 * 
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/wxRegister")
	@ResponseBody
	public ResponseDTO registerByUserInfos(@RequestBody EcmUserVo ecmUserVo) {
		EcmUser user = new EcmUser();
		// 短信验证码验证
		try {
			String inputPhoneConfirmCode = ecmUserVo.getPhoneConfirmCode();
			String mobile = ecmUserVo.getMobile();
			String phoneKey = EncryptUtil.aesEncrypt(mobile, SecretKeyConstants.secretKey);
			String phoneConfirmCode = String.valueOf(redisUtil.get(phoneKey));
			if (StringUtils.isNullOrEmpty(phoneConfirmCode)) {
				return ResponseDTO.fail("请重新获取验证码", null, null, 507);
			} else {
				if (!inputPhoneConfirmCode.equals(phoneConfirmCode)) {
					return ResponseDTO.fail("请正确输入验证码", null, null, 502);
				}
			}
			// 入库
			user.setUsername(ecmUserVo.getUsername());
			// count 初始时没有count 在注册时设置为0
			user.setCount(0);
			user.setIsValid("Y");
			user.setRoles("1");
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			// 用户敏感信息需要加密 可反解
			user.setMobile(phoneKey);
			user.setPassword(MD5Utils.encrypt(ecmUserVo.getPassword()));
			Integer userId = wxLoginService.savaUser(user,ecmUserVo);
			if (userId != null) {
				return ResponseDTO.ok("注册成功",userId);
			}else {
				return ResponseDTO.fail("保存注册用户信息失败", null, null, "000039");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDTO.fail("failed", null, null, "000039");
		}
	}
	
	/**
	 * 通过正则保证图形验证码都是字母加数字
	 * 
	 * @param ecmUserVo
	 * @return
	 */
	private String validateCreateText(DefaultKaptcha produce) {
		Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,23}$");
		String createText = produce.createText();
		Matcher matcher = pattern.matcher(createText);
		boolean matches = matcher.matches();
		if (matches) {
			return createText;
		} else {
			return this.validateCreateText(produce);
		}
	}

}
