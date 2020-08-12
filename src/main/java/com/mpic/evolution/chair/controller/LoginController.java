package com.mpic.evolution.chair.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
import com.mpic.evolution.chair.util.MailUtil;
import com.mpic.evolution.chair.util.RandomUtil;
import com.mpic.evolution.chair.util.UUIDUtil;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;

import io.netty.util.internal.StringUtil;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author SJ
 */

@Controller
public class LoginController extends BaseController {
	
	@Resource
	LoginService loginService;
	
	@Resource
	EcmUserService ecmUserService;
	
	private ConcurrentHashMap<String,String> map = new ConcurrentHashMap<String,String>();
	
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
     *	 以流的形式传到前端
     * @throws IOException
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
    	map.put(uuid, createText);
    	BufferedImage bi = produce.createImage(createText);
    	try {
			ImageIO.write(bi, "jpg", out);
	    	code = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseDTO.fail("验证码生成失败");
		}
    	BASE64Encoder encoder = new BASE64Encoder();
        String base64Str = encoder.encode(code);
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
     	String regionCode = ecmUserVo.getImageCodeKey();
    	String confirmCode = ecmUserVo.getConfirmCode();
    	if (!regionCode.equals(confirmCode)) {
			return ResponseDTO.fail("验证码错误");
		}
    	String inputPwd = ecmUserVo.getPassword();
    	String encrypt = MD5Utils.encrypt(inputPwd); 
    	EcmUser ecmUser = new EcmUser();
    	// 根据mobile获取用户信息 后续改成token
    	ecmUser.setMobile(ecmUserVo.getMobile());
    	EcmUser userInfos = ecmUserService.getUserInfos(ecmUser);
    	String password = userInfos.getPassword();
    	if (!password.equals(encrypt)) {
			return ResponseDTO.fail("密码错误");
		}
    	// 设置token
    	String token = JWTUtil.sign(String.valueOf(userInfos.getPkUserId()), 
    			userInfos.getUsername(), 
    			SecretKeyConstants.jwtSecretKey);
    	//根据userId 修改用户信息
    	EcmUser user = new EcmUser();
    	EcmUserVo userVo = new EcmUserVo();
    	userVo.setPkUserId(userInfos.getPkUserId());
    	Integer count = userInfos.getCount();
    	++count;
    	user.setCount(count);
    	user.setLastLoginTime(new Date());
    	user.setUpdateTime(new Date());
    	ecmUserService.updateEcmUserByMobile(user, userVo);
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
		EcmUser user = new EcmUser();
		user.setIsValid("Y");//表示注册成功已激活
		user.setMobile(ecmUserVo.getMobile());
		EcmUser userInfos = ecmUserService.getUserInfos(user);
		if (userInfos!=null && !StringUtil.isNullOrEmpty(userInfos.getPkUserId().toString())) {
			return ResponseDTO.fail("该账号已注册");
		}
		if(!ecmUserVo.getPassword().equals(ecmUserVo.getConfirmPwd())) {
			return ResponseDTO.fail("密码与确认密码不一致");
		}
		// 验证码验证
		String regionCode = map.get(ecmUserVo.getImageCodeKey());
		String confirmCode = ecmUserVo.getConfirmCode();
		if (!regionCode.equals(confirmCode)) {
			return ResponseDTO.fail("验证码错误");
		}
		//短信验证码验证
		String inputPCC = ecmUserVo.getPhoneConfirmCode();
		String phoneConfirmCode = map.get(ecmUserVo.getPhoneCodeKey());
    	if (inputPCC.equals(phoneConfirmCode)) {
			return ResponseDTO.fail("手机短信验证码错误");
		}
		//入库
		EcmUser ecmUser = new EcmUser();
		ecmUser.setUsername(ecmUserVo.getUsername());
		ecmUser.setIsValid("N");
		ecmUser.setRoles("1");
		ecmUser.setCreateTime(new Date());
		ecmUser.setUpdateTime(new Date());
		try {
			// 用户敏感信息需要加密 可反解
			ecmUser.setMobile(EncryptUtil.aesEncrypt(ecmUserVo.getMobile(), SecretKeyConstants.secretKey));
			ecmUser.setEmail(EncryptUtil.aesEncrypt(ecmUserVo.getEmail(), SecretKeyConstants.secretKey));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDTO.fail("加密失败", null, null, 501);
		}
		ecmUser.setPassword(MD5Utils.encrypt(ecmUserVo.getPassword()));
		ecmUserService.savaUser(ecmUser);
		return ResponseDTO.ok("注册成功");
	}
	
	 /**
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
    		map.put(phoneCodeKey, phoneConfirmCode);
			SendStatus status = loginService.sendSMS(phoneConfirmCode,phoneNumbers);
			if (!status.getCode().equals("Ok")) {
				return ResponseDTO.fail("手机验证码发送失败："+status.getMessage());
			}
		} catch (TencentCloudSDKException e) {
			e.printStackTrace();
			return ResponseDTO.fail("手机验证码发送失败");
		}catch (Exception e1) {
			e1.printStackTrace();
			return ResponseDTO.fail("用户信息加密失败");
		}
		return ResponseDTO.ok("手机验证码发送成功",data.put("phoneCodeKey", phoneCodeKey));
	}
	
	/**
	 * 	发送邮件
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/sendEmail")
	@ResponseBody
	public ResponseDTO sendEmail(@RequestBody EcmUserVo ecmUserVo) {
		String uuid = UUIDUtil.getUUID();//激活码
		String emailType = ecmUserVo.getEmailType();
		String subject = "账号激活";
		if (!emailType.equals("verification")) {
			subject = "修改密码";
		}
		MailUtil mailUtil = new MailUtil(ecmUserVo.getEmail(),emailType,uuid,subject);
		try {	
			mailUtil.sendEmail();
			EcmUser user = new EcmUser();
			user.setEmailUuid(uuid);
			user.setUpdateTime(new Date());
			user.setLastCheckMail(new Date());
			ecmUserService.saveToken(user,ecmUserVo);//发送邮邮件 并更新emailUuid值
			return ResponseDTO.ok("邮件发送成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDTO.fail("邮件发送失败");
		}
    }
	
	/**
	 * 	邮箱激活
	 * 	邮箱激活之后还需要修改last_check_mail字段和 updateTime字段
	 * @param ecmUserVo
	 */
	@RequestMapping("/activateUser")
	@ResponseBody
	public void activateUser(EcmUserVo ecmUserVo) {	
		//根据uuid来修改数据库中isvalid状态 
		String uuid = UUIDUtil.getUUID();
		EcmUser user = new EcmUser();
		user.setEmailUuid(uuid);//修改掉emailUuid在数据库中的值
		user.setIsValid("Y");
		user.setUpdateTime(new Date());//修改updateTime字段
		ecmUserService.updateIsvalidByToken(user,ecmUserVo);
    }
	
	/**
	 * 	忘记密码  
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/forgetPwd")
	@ResponseBody
	public ResponseDTO forgetPassword(@RequestBody EcmUserVo ecmUserVo) {	
		//确认密码验证
		String password = ecmUserVo.getPassword();
		if(!password.equals(ecmUserVo.getConfirmPwd())) {
			return ResponseDTO.fail("密码与确认密码不一致");
		}
		EcmUser user = new EcmUser();
		String uuid = UUIDUtil.getUUID();
		user.setEmailUuid(uuid);// 修改EmailUuid在数据库中的值 
		user.setPassword(MD5Utils.encrypt(password));//修改后的密码以MD5加密入库
		user.setLastCheckMail(new Date());//发送了邮件做跳转 故修改last_check_mail字段
		user.setUpdateTime(new Date());//修改updateTime字段
		boolean flag = ecmUserService.updatePwdByToken(user,ecmUserVo);
		if (!flag) {
			return ResponseDTO.ok("密码修改失败");
		}
		return ResponseDTO.fail("密码修改成功");
    }
	
	
	/*@RequestMapping("/getConfirmCode")
    @ResponseBody
	public void getConfirmCode() {
		//以流的形式传图片到前端
    	ServletOutputStream sout = null;
    	try {
    		byte[] code = null;
        	ByteArrayOutputStream out = new ByteArrayOutputStream();
        	DefaultKaptcha produce = loginService.getConfirmCode();
        	String createText = produce.createText();
        	HttpSession session = getRequstSession();
        	session.setAttribute("regionCode", createText);
        	BufferedImage bi = produce.createImage(createText);
        	ImageIO.write(bi, "jpg", out);
        	code = out.toByteArray();
        	HttpServletResponse response = getResponse();
        	response.setHeader("Cache-Control", "no-store");
    		response.setHeader("Pragma", "no-cache");
    		response.setDateHeader("Expires", 0);
    		response.setContentType("image/jpeg");
    		sout = response.getOutputStream();
    		sout.write(code);
    		sout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				sout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/
    
}
