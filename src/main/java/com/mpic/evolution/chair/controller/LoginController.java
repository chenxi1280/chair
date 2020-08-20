package com.mpic.evolution.chair.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mpic.evolution.chair.common.constant.SecretKeyConstants;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.service.LoginService;
import com.mpic.evolution.chair.util.EncryptUtil;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.MD5Utils;
import com.mpic.evolution.chair.util.RandomUtil;
import com.mpic.evolution.chair.util.RedisUtil;
import com.mpic.evolution.chair.util.StringUtils;
import com.mpic.evolution.chair.util.UUIDUtil;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;

/**
 * 
 * @author SJ
 */

@Controller
public class LoginController extends BaseController {
	
	private Logger log = LogManager.getLogger(LoginController.class);
	
	@Resource
	LoginService loginService;
	
	@Resource
	EcmUserService ecmUserService;
	
	@Resource
	RedisUtil redisUtil;
	
	/**
	 * abandon! it is going to user in the future
	 * @param code
	 * @return
	 */
    @RequestMapping(value = "/loginByWeiXin", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO loginByWeiXin(@RequestParam String code) {
       return loginService.loginByWeiXin(code);
    }
    
    /**
     * 	获取图片验证码接口
     *	 以BASE64转码的字符串传到前端
     */
    @RequestMapping("/getConfirmCode")
    @ResponseBody
	public ResponseDTO getConfirmCode() {
		byte[] code = null;
		JSONObject data = new JSONObject();
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	DefaultKaptcha produce = loginService.getConfirmCode();
    	String createText = produce.createText();
    	String uuid = UUIDUtil.getUUID();
    	//存的时候不做处理 在获取不到时做报错提示
    	redisUtil.lSet(uuid, createText,300L);
    	BufferedImage bi = produce.createImage(createText);
    	try {
			ImageIO.write(bi, "jpg", out);
	    	code = out.toByteArray();
		} catch (IOException e) {
			log.error("获取图片验证码失败");
			e.printStackTrace();
		}
        String base64Str = Base64.encodeBase64String(code);
        data.put("imageCodeKey", uuid);
        data.put("base64Str", base64Str);
        return ResponseDTO.ok("获取验证码成功", data);
	}
    
    /**
     * 	登录接口 
     * 	登陆成功之后 需要修改use表中的count(登录次数)，last_login_time(上一次登录时间)字段，update_time字段
     * @param ecmUserVo
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResponseDTO loginByToken(@RequestBody EcmUserVo ecmUserVo) {
    	EcmUser ecmUser = new EcmUser();
    	try {
			ecmUser.setMobile(EncryptUtil.aesEncrypt(ecmUserVo.getMobile(), SecretKeyConstants.secretKey));
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
		}
    	EcmUser userInfos = ecmUserService.getUserInfos(ecmUser);
    	// 判断账号是否存在
    	if (userInfos==null || StringUtils.isNullOrBlank(userInfos.getPassword())) {
    		return ResponseDTO.fail("账号未注册，请注册账号", null, null, 505);
		}
    	JSONArray data = new JSONArray();
		String confirmCode = ecmUserVo.getConfirmCode(); 
		String regionCode = String.valueOf(redisUtil.lPop(ecmUserVo.getImageCodeKey())); 
		if(StringUtils.isNullOrEmpty(regionCode)){
			data.add("501");
    	}else {
    		if (!regionCode.equals(confirmCode)) { 
    			data.add("501"); 
    		}
		}
    	String inputPwd = ecmUserVo.getPassword();
    	String encrypt = MD5Utils.encrypt(inputPwd); 
		String password = userInfos.getPassword();
    	if (!encrypt.equals(password)) {
    		data.add("506");
		}
    	if (!data.isEmpty()) {
			return ResponseDTO.fail("登陆失败", data, null, 508);
		}
    	// 设置token
    	String token = JWTUtil.sign(String.valueOf(userInfos.getPkUserId()), 
    			userInfos.getUsername(), 
    			SecretKeyConstants.jwtSecretKey);
    	//根据userId 修改用户信息
    	EcmUser user = new EcmUser();
    	EcmUserVo userVo = new EcmUserVo();
    	userVo.setPkUserId(userInfos.getPkUserId());
    	user.setLastLoginTime(new Date());
    	user.setUpdateTime(new Date());
    	ecmUserService.updateEcmUserById(user, userVo);
		return ResponseDTO.ok("登陆成功", token);
    }
    
    /**
     * 	注册接口
     * 	注册成功还需要修改create_time字段和 update_time改字段
     * @param ecmUserVo
     * @return
     */
	@RequestMapping("/register")
	@ResponseBody
	public ResponseDTO registerByUserInfos(@RequestBody EcmUserVo ecmUserVo) {
		//短信验证码验证
		String inputPCC = ecmUserVo.getPhoneConfirmCode();
		String phoneConfirmCode = String.valueOf(redisUtil.get(ecmUserVo.getPhoneCodeKey()));
		if(StringUtils.isNullOrEmpty(phoneConfirmCode)){
			return ResponseDTO.fail("请重新获取验证码",null,null,502);
    	}else {
    		if (!inputPCC.equals(phoneConfirmCode)) {
        		return ResponseDTO.fail("请正确输入验证码",null,null,502);
    		}
		}
		//入库
		EcmUser ecmUser = new EcmUser();
		ecmUser.setUsername(ecmUserVo.getUsername());
    	// count 初始时没有count 在注册时设置为0
		ecmUser.setCount(0);
		ecmUser.setIsValid("N");
		ecmUser.setRoles("1");
		ecmUser.setCreateTime(new Date());
		ecmUser.setUpdateTime(new Date());
		try {
			// 用户敏感信息需要加密 可反解
			ecmUser.setMobile(EncryptUtil.aesEncrypt(ecmUserVo.getMobile(), SecretKeyConstants.secretKey));
			ecmUser.setEmail(EncryptUtil.aesEncrypt(ecmUserVo.getEmail(), SecretKeyConstants.secretKey));
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
		}
		ecmUser.setPassword(MD5Utils.encrypt(ecmUserVo.getPassword()));
		ecmUserService.savaUser(ecmUser);
		return ResponseDTO.ok("注册成功");
	}
	
	 /*
     * 	短信验证
     * @param ecmUserVo
     * @return
     */
	@RequestMapping("/sendSMS")
	@ResponseBody
	public ResponseDTO sendShortMessage(@RequestBody EcmUserVo ecmUserVo) {
		JSONObject data = new JSONObject();
		String mobile = "86"+ecmUserVo.getMobile();
		String[] phoneNumbers = {mobile};
		String phoneConfirmCode = RandomUtil.getCode();
		String phoneCodeKey = "";
    	try {
    		phoneCodeKey = EncryptUtil.aesEncrypt(ecmUserVo.getMobile(), SecretKeyConstants.secretKey);
    		//往redis存的时候不做判断 取得时候做错误校验
    		redisUtil.set(phoneCodeKey, phoneConfirmCode, 300L);
    		data.put("phoneCodeKey", phoneCodeKey);
			SendStatus status = loginService.sendSMS(phoneConfirmCode,phoneNumbers);
			if (!status.getCode().equals("Ok")) {
				log.error("手机验证码发送失败："+status.getMessage());
				return ResponseDTO.fail("手机验证码发送失败："+status.getMessage(),null,null,502);
			}
		} catch (TencentCloudSDKException e) {
			log.error("手机验证码发送失败");
			e.printStackTrace();
		}catch (Exception e1) {
			log.error("手机号加密查询用户信息时手机号加密失败");
			e1.printStackTrace();
		}
		return ResponseDTO.ok("手机验证码发送成功",data);
	}
	
	/**
	 * 	验证手机号 
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/validateMobile")
	@ResponseBody
	public ResponseDTO validateMobile(@RequestBody EcmUserVo ecmUserVo) {
		JSONArray data = new JSONArray();
		String confirmCode = ecmUserVo.getConfirmCode();
		String regionCode = String.valueOf(redisUtil.lPop(ecmUserVo.getImageCodeKey()));
		if(StringUtils.isNullOrEmpty(regionCode)){
			data.add("501");
    	}else {
    		if (!regionCode.equals(confirmCode)) {
    			data.add("501");
    		}
		}
		String mobile = ecmUserVo.getMobile();
		EcmUser ecmUser = new EcmUser();
    	try {
			ecmUser.setMobile(EncryptUtil.aesEncrypt(mobile, SecretKeyConstants.secretKey));
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
		}
    	EcmUser userInfos = ecmUserService.getUserInfos(ecmUser);
    	if (userInfos==null || StringUtils.isNullOrBlank(userInfos.getMobile())) {
    		data.add("505");
		}
    	if (!data.isEmpty()) {
			return ResponseDTO.fail(null, data, null, 508);
		}
		return ResponseDTO.ok();
	}
	
	/**
	 * 	忘记密码  
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/forgetPwd")
	@ResponseBody
	public ResponseDTO forgetPassword(@RequestBody EcmUserVo ecmUserVo) {	
		JSONArray data = new JSONArray();
		//确认密码验证
		String inputPwd = ecmUserVo.getPassword();
		if(!inputPwd.equals(ecmUserVo.getConfirmPwd())) {
			data.add("503");
		}
		String inputPCC = ecmUserVo.getPhoneConfirmCode();
		String mobile = ecmUserVo.getMobile();
		String mobileKey = "";
		try {
			mobileKey = EncryptUtil.aesEncrypt(mobile, SecretKeyConstants.secretKey);		
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
		}
		EcmUserVo userVo = new EcmUserVo();
		userVo.setMobile(mobile);
		String phoneConfirmCode = String.valueOf(redisUtil.get(mobileKey));
		if(StringUtils.isNullOrEmpty(phoneConfirmCode)){
			data.add("502");
    	}else {
    		//手机验证码校验
    		if (!phoneConfirmCode.equals(inputPCC)) {
    			data.add("502");
    		}
		}
		if (!data.isEmpty()) {
			return ResponseDTO.fail("密码修改失败", data, null, 508);
		}
		EcmUser user = new EcmUser();
		user.setPassword(MD5Utils.encrypt(inputPwd));//修改后的密码以MD5加密入库
		user.setUpdateTime(new Date());//修改updateTime字段
		boolean flag = ecmUserService.updatePwdByToken(user,userVo);
		if (!flag) {
			return ResponseDTO.fail("密码修改失败");
		}
		return ResponseDTO.ok("密码修改成功");
    }
	
	/**
	 * 	校验昵称  
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/validateUserName")
	@ResponseBody
	private ResponseDTO validateUsername(@RequestBody EcmUserVo ecmUserVo) {
		//TODO 用户昵称是否违法
		EcmUser user = new EcmUser();
		user.setUsername(ecmUserVo.getUsername());
		EcmUser userInfo = ecmUserService.getUserInfos(user);
		//用户昵称是否存在
		if (userInfo!=null && !StringUtils.isNullOrBlank(String.valueOf(userInfo.getUsername()))) {
			return ResponseDTO.fail("昵称已被使用", null, null, 504);
		}
		return ResponseDTO.ok();
	}
	
	/**
	 * 	校验手机号
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/isExistMobile")
	@ResponseBody
	private ResponseDTO isExistMobile(@RequestBody EcmUserVo ecmUserVo) {
		EcmUser user = new EcmUser();
		String mobile = "";
		try {
			mobile = EncryptUtil.aesEncrypt(ecmUserVo.getMobile(), SecretKeyConstants.secretKey);
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
		}
		user.setMobile(mobile);
		EcmUser userInfo = ecmUserService.getUserInfos(user);
		if (userInfo!=null && !StringUtils.isNullOrBlank(String.valueOf(userInfo.getMobile()))) {
			return ResponseDTO.fail("账号已被注册，请直接登陆", null, null, 505);
		}
		return ResponseDTO.ok();
	}
	
	/**
	 * 	校验图形验证码
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/validateImageCode")
	@ResponseBody
	private ResponseDTO validateImageCode(@RequestBody EcmUserVo ecmUserVo) {
		// 验证码验证
		String regionCode = String.valueOf(redisUtil.lPop(ecmUserVo.getImageCodeKey()));
		if(StringUtils.isNullOrEmpty(regionCode)){
			return ResponseDTO.fail("点击刷新，重新获取验证码",null,null,501);
    	}else {
    		String confirmCode = ecmUserVo.getConfirmCode();
    		if (!regionCode.equals(confirmCode)) {
    			return ResponseDTO.fail("请正确输入验证码", null, null, 501);
    		}
		}
		return ResponseDTO.ok();
	}
    
}
