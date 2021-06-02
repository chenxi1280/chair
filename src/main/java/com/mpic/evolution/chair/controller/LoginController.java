package com.mpic.evolution.chair.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import com.mpic.evolution.chair.common.constant.JudgeConstant;
import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mybatis.spring.MyBatisSystemException;
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
import com.mpic.evolution.chair.pojo.entity.EcmInviteCode;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmInviteCodeVo;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmInviteCodeService;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.service.LoginService;
import com.mpic.evolution.chair.util.AIVerifyUtil;
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

@SuppressWarnings("ALL")
@Controller
public class LoginController extends BaseController {

	private Logger log = LogManager.getLogger(LoginController.class);

	@SuppressWarnings("AlibabaConstantFieldShouldBeUpperCase")
	private static final Pattern PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,23}$");


	@Resource
	LoginService loginService;

	@Resource
	EcmUserService ecmUserService;

	@Resource
	EcmInviteCodeService ecmInviteCodeService;

	@Resource
	RedisUtil redisUtil;

	/**
	 * abandon! it is going to user in the future
	 *
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/loginByWeiXin", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseDTO loginByWeiXin(@RequestParam String code) {
		return loginService.loginByWeiXin(code);
	}

	/**
	 * 获取图片验证码接口 以BASE64转码的字符串传到前端
	 */
	@RequestMapping("/getConfirmCode")
	@ResponseBody
	public ResponseDTO getConfirmCode() {
		JSONObject data = new JSONObject();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] code = null;
			DefaultKaptcha produce = loginService.getConfirmCode();
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
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		}
	}

	/**
	 * 登录接口 登陆成功之后 需要修改use表中的count(登录次数)，last_login_time(上一次登录时间)字段，update_time字段
	 *
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public ResponseDTO loginByToken(@RequestBody EcmUserVo ecmUserVo) {
		JSONArray data = new JSONArray();
		EcmUser ecmUser = new EcmUser();
		EcmUser user = new EcmUser();
		EcmUserVo userVo = new EcmUserVo();
		try {
			ecmUser.setMobile(EncryptUtil.aesEncrypt(ecmUserVo.getMobile(), SecretKeyConstants.SECRET_KEY));
			ecmUser = ecmUserService.getUserInfos(ecmUser);
			// 判断账号是否存在
			if (ecmUser == null || StringUtils.isNullOrBlank(ecmUser.getPassword())) {
				data.add(ErrorEnum.ERR_505.getValue());
			}
			String confirmCode = ecmUserVo.getConfirmCode();
			String regionCode = String.valueOf(redisUtil.lPop(ecmUserVo.getImageCodeKey()));
			if (StringUtils.isNullOrEmpty(regionCode)) {
				data.add(ErrorEnum.ERR_501.getValue());
			} else {
				if (!regionCode.equals(confirmCode)) {
					data.add(ErrorEnum.ERR_501.getValue());
				}
			}
			// 账号不存在需要直接返回
			if (!data.isEmpty()) {
				return ResponseDTO.fail(ErrorEnum.ERR_508.getText(), data, null, ErrorEnum.ERR_508.getValue());
			}
			if ( "N".equals(ecmUser.getIsValid())) {
				data.add(ErrorEnum.ERR_509.getValue());
			}
			String inputPwd = ecmUserVo.getPassword();
			String encrypt = MD5Utils.encrypt(inputPwd);
			String password = ecmUser.getPassword();
			if (!encrypt.equals(password)) {
				data.add(ErrorEnum.ERR_506.getValue());
			}
			if (!data.isEmpty()) {
				return ResponseDTO.fail(ErrorEnum.ERR_508.getText(), data, null, ErrorEnum.ERR_508.getValue());
			}
			// 设置token
			String token = JWTUtil.sign(String.valueOf(ecmUser.getPkUserId()), ecmUser.getUsername(),
					SecretKeyConstants.JWT_SECRET_KEY);
			// 根据userId 修改用户信息
			userVo.setPkUserId(ecmUser.getPkUserId());
			user.setLastLoginTime(new Date());
			user.setUpdateTime(new Date());
			ecmUserService.updateEcmUserById(user, userVo);
			return ResponseDTO.ok("登陆成功", token);
		} catch (MyBatisSystemException e) {
			log.error("账号在数据库中有多条记录");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		}
	}

	/**
	 * 注册接口 注册成功还需要修改create_time字段和 update_time改字段
	 *
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/register")
	@ResponseBody
	public ResponseDTO registerByUserInfos(@RequestBody EcmUserVo ecmUserVo) {
		EcmUser user = new EcmUser();
		EcmUser userInfo = new EcmUser();
		String username = ecmUserVo.getUsername();
		String inputPhoneConfirmCode = ecmUserVo.getPhoneConfirmCode();
		//邀请码所需条件 过期跳过邀请码检测
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime dateTime = LocalDateTime.of(2021, 12, 31, 0, 0);
		EcmInviteCode ecmInviteCode = new EcmInviteCode();
		try {
			//邀请码检测
			if(now.isBefore(dateTime)) {
				String inviteCode = ecmUserVo.getInviteCode();
				ecmInviteCode = ecmInviteCodeService.getEcmInvitedCode(inviteCode);
				if (ecmInviteCode == null || ecmInviteCode.getFkUserId() != null) {
					return ResponseDTO.fail(ErrorEnum.ERR_515.getText(), null, null, ErrorEnum.ERR_515.getValue());
				}else {

				}
			}
			//昵称检测
			if (StringUtils.isNullOrBlank(username)) {
				return ResponseDTO.fail(ErrorEnum.ERR_504.getText(), null, null, ErrorEnum.ERR_504.getValue());
			}
			String result = AIVerifyUtil.convertContent(username);
			if (!StringUtils.isNullOrBlank(result)) {
				return ResponseDTO.fail(ErrorEnum.ERR_510.getText(), result, null, ErrorEnum.ERR_510.getValue());
			}
			userInfo.setUsername(username);
			user = ecmUserService.getUserInfos(userInfo);
			// 用户昵称是否存在
			if (user != null && !StringUtils.isNullOrBlank(String.valueOf(user.getUsername()))) {
				return ResponseDTO.fail(ErrorEnum.ERR_504.getText(), null, null, ErrorEnum.ERR_504.getValue());
			}
			//手机号检测
			String mobile = EncryptUtil.aesEncrypt(ecmUserVo.getMobile(), SecretKeyConstants.SECRET_KEY);
			userInfo.setMobile(mobile);
			user = ecmUserService.getUserInfos(userInfo);
			if (user != null && !StringUtils.isNullOrBlank(String.valueOf(user.getMobile()))) {
				return ResponseDTO.fail(ErrorEnum.ERR_505.getText(), null, null, ErrorEnum.ERR_505.getValue());
			}
			// 短信验证码验证
			String phoneConfirmCode = String.valueOf(redisUtil.get(mobile));
			if (StringUtils.isNullOrEmpty(phoneConfirmCode)) {
				return ResponseDTO.fail(ErrorEnum.ERR_507.getText(), null, null, ErrorEnum.ERR_507.getValue());
			} else {
				if (!inputPhoneConfirmCode.equals(phoneConfirmCode)) {
					return ResponseDTO.fail(ErrorEnum.ERR_502.getText(), null, null, ErrorEnum.ERR_502.getValue());
				}
			}
			// 入库
			userInfo.setUsername(ecmUserVo.getUsername());
			// count 初始时没有count 在注册时设置为0
			userInfo.setCount(0);
			userInfo.setIsValid(JudgeConstant.Y);
			userInfo.setRoles("1");
			userInfo.setCreateTime(new Date());
			userInfo.setUpdateTime(new Date());
			// 用户敏感信息需要加密 可反解
			userInfo.setMobile(mobile);
			//TODO
			userInfo.setPassword(MD5Utils.encrypt(ecmUserVo.getPassword()));
			boolean savaUserFlag = ecmUserService.savaUser(userInfo);
			if(now.isBefore(dateTime)) {
				if (savaUserFlag) {
					Integer pkUserId = userInfo.getPkUserId();
					ecmInviteCode.setBindDate(new Date());
					ecmInviteCode.setFkUserId(pkUserId);
					ecmInviteCode.setInviteCodeStatus((short)1);
					ecmInviteCodeService.savaEcmInvitedCode(ecmInviteCode);
				}
			}
			return ResponseDTO.ok("注册成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		}
	}

	/*
	 * 短信验证
	 *
	 * @param ecmUserVo
	 *
	 * @return
	 */
	@RequestMapping("/sendSMS")
	@ResponseBody
	public ResponseDTO sendShortMessage(@RequestBody EcmUserVo ecmUserVo) {
		try {
			String mobile = "86" + ecmUserVo.getMobile();
			String[] phoneNumbers = { mobile };
			String phoneConfirmCode = RandomUtil.getCode();
			String phoneCodeKey = EncryptUtil.aesEncrypt(ecmUserVo.getMobile(), SecretKeyConstants.SECRET_KEY);
			// 往redis存的时候不做判断 取得时候做错误校验
			redisUtil.set(phoneCodeKey, phoneConfirmCode, 300L);
			SendStatus status = loginService.sendSMS(phoneConfirmCode, phoneNumbers);
			if (!"Ok".equals(status.getCode())) {
				log.error("手机验证码发送失败：" + status.getMessage());
				return ResponseDTO.fail(ErrorEnum.ERR_502.getText() + status.getMessage(), null, null, ErrorEnum.ERR_502.getValue());
			}
			return ResponseDTO.ok("手机验证码发送成功");
		} catch (TencentCloudSDKException e) {
			log.error("手机验证码发送失败");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		} catch (Exception e) {
			log.error("手机号加密失败");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		}
	}

	/**
	 * 验证手机号
	 *
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/validateMobile")
	@ResponseBody
	public ResponseDTO validateMobile(@RequestBody EcmUserVo ecmUserVo) {
		JSONArray data = new JSONArray();
		EcmUser ecmUser = new EcmUser();
		try {
			String confirmCode = ecmUserVo.getConfirmCode();
			String regionCode = String.valueOf(redisUtil.lPop(ecmUserVo.getImageCodeKey()));
			String mobile = ecmUserVo.getMobile();
			ecmUser.setMobile(EncryptUtil.aesEncrypt(mobile, SecretKeyConstants.SECRET_KEY));
			ecmUser = ecmUserService.getUserInfos(ecmUser);
			if (StringUtils.isNullOrEmpty(regionCode)) {
				data.add(ErrorEnum.ERR_501.getValue());
			} else {
				if (!regionCode.equals(confirmCode)) {
					data.add(ErrorEnum.ERR_501.getValue());
				}
			}
			if (ecmUser == null || StringUtils.isNullOrBlank(ecmUser.getMobile())) {
				data.add(ErrorEnum.ERR_505.getValue());
			}
			if (!data.isEmpty()) {
				return ResponseDTO.fail(ErrorEnum.ERR_508.getText(), data, null, ErrorEnum.ERR_508.getValue());
			}
			return ResponseDTO.ok();
		} catch (MyBatisSystemException e) {
			log.error("账号在数据库中有多条记录");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		}
	}

	/**
	 * 忘记密码
	 *
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/forgetPwd")
	@ResponseBody
	public ResponseDTO forgetPassword(@RequestBody EcmUserVo ecmUserVo) {
		JSONArray data = new JSONArray();
		EcmUserVo userVo = new EcmUserVo();
		EcmUser user = new EcmUser();
		try {
			// 确认密码验证
			String inputPhoneConfirmCode = ecmUserVo.getPhoneConfirmCode();
			String mobile = ecmUserVo.getMobile();
			String mobileKey = EncryptUtil.aesEncrypt(mobile, SecretKeyConstants.SECRET_KEY);
			userVo.setMobile(mobileKey);
			EcmUser userInfos = ecmUserService.getUserInfos(userVo);
			String phoneConfirmCode = String.valueOf(redisUtil.get(mobileKey));
			String inputPwd = ecmUserVo.getPassword();
			if (userInfos == null || StringUtils.isNullOrBlank(userInfos.getMobile())) {
				data.add(ErrorEnum.ERR_505.getValue());
			}
			if (!inputPwd.equals(ecmUserVo.getConfirmPwd())) {
				data.add(ErrorEnum.ERR_503.getValue());
			}
			if (StringUtils.isNullOrEmpty(phoneConfirmCode)) {
				data.add(ErrorEnum.ERR_507.getValue());
			} else {
				// 手机验证码校验
				if (!phoneConfirmCode.equals(inputPhoneConfirmCode)) {
					data.add(ErrorEnum.ERR_502.getValue());
				}
			}
			if (!data.isEmpty()) {
				return ResponseDTO.fail(ErrorEnum.ERR_508.getText(), data, null, ErrorEnum.ERR_508.getValue());
			}
			user.setPassword(MD5Utils.encrypt(inputPwd));// 修改后的密码以MD5加密入库
			user.setUpdateTime(new Date());// 修改updateTime字段
			boolean flag = ecmUserService.updatePwdByToken(user, userVo);
			if (!flag) {
				return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
			}
			return ResponseDTO.ok("密码修改成功");
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		}

	}

	/**
	 * 校验昵称
	 *
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/validateUserName")
	@ResponseBody
	public ResponseDTO validateUsername(@RequestBody EcmUserVo ecmUserVo) {
		EcmUser user = new EcmUser();
		try {
			String username = ecmUserVo.getUsername();
			if (StringUtils.isNullOrBlank(username)) {
				return ResponseDTO.fail(ErrorEnum.ERR_504.getText(), null, null, ErrorEnum.ERR_504.getValue());
			}
			String result = AIVerifyUtil.convertContent(username);
			if (!StringUtils.isNullOrBlank(result)) {
				return ResponseDTO.fail(ErrorEnum.ERR_510.getText(), result, null, ErrorEnum.ERR_510.getValue());
			}
			user.setUsername(username);
			user = ecmUserService.getUserInfos(user);
			// 用户昵称是否存在
			if (user != null && !StringUtils.isNullOrBlank(String.valueOf(user.getUsername()))) {
				return ResponseDTO.fail(ErrorEnum.ERR_504.getText(), null, null, ErrorEnum.ERR_504.getValue());
			}
			return ResponseDTO.ok();
		} catch (MyBatisSystemException e) {
			log.error("账号在数据库中有多条记录");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		}
	}

	/**
	 * 校验手机号
	 *
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/isExistMobile")
	@ResponseBody
	public ResponseDTO isExistMobile(@RequestBody EcmUserVo ecmUserVo) {
		EcmUser user = new EcmUser();
		try {
			String mobile = EncryptUtil.aesEncrypt(ecmUserVo.getMobile(), SecretKeyConstants.SECRET_KEY);
			user.setMobile(mobile);
			user = ecmUserService.getUserInfos(user);
			if (user != null && !StringUtils.isNullOrBlank(String.valueOf(user.getMobile()))) {
				return ResponseDTO.fail(ErrorEnum.ERR_505.getText(), null, null, ErrorEnum.ERR_505.getValue());
			}
			return ResponseDTO.ok();
		} catch (MyBatisSystemException e) {
			log.error("账号在数据库中有多条记录");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		} catch (Exception e) {
			log.error("用户信息加密失败");
			e.printStackTrace();
			return ResponseDTO.fail(ErrorEnum.ERR_000039.getText(), null, null, ErrorEnum.ERR_000039.getValue());
		}

	}

	/**
	 * 校验图形验证码
	 *
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/validateImageCode")
	@ResponseBody
	public ResponseDTO validateImageCode(@RequestBody EcmUserVo ecmUserVo) {
		// 验证码验证
		String regionCode = String.valueOf(redisUtil.lPop(ecmUserVo.getImageCodeKey()));
		if (StringUtils.isNullOrEmpty(regionCode)) {
			return ResponseDTO.fail(ErrorEnum.ERR_501.getText(), null, null, ErrorEnum.ERR_501.getValue());
		} else {
			String confirmCode = ecmUserVo.getConfirmCode();
			if (!regionCode.equals(confirmCode)) {
				return ResponseDTO.fail(ErrorEnum.ERR_501.getText(), null, null, ErrorEnum.ERR_501.getValue());
			}
		}
		return ResponseDTO.ok();
	}

	/**
	 * 邀请码校验
	 *
	 * @param ecmUserVo
	 * @return
	 */
	@RequestMapping("/validateInviteCode")
	@ResponseBody
	public ResponseDTO validateInviteCode(@RequestBody EcmInviteCodeVo ecmInviteCodeVo) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime dateTime = LocalDateTime.of(2021, 12, 31, 0, 0);
		if(now.isBefore(dateTime)) {
			// 验证码验证
			String regionCode = String.valueOf(redisUtil.lPop(ecmInviteCodeVo.getImageCodeKey()));
			if (StringUtils.isNullOrEmpty(regionCode)) {
				return ResponseDTO.fail(ErrorEnum.ERR_501.getText(), null, null, ErrorEnum.ERR_501.getValue());
			} else {
				String confirmCode = ecmInviteCodeVo.getConfirmCode();
				if (!regionCode.equals(confirmCode)) {
					return ResponseDTO.fail(ErrorEnum.ERR_501.getText(), null, null, ErrorEnum.ERR_501.getValue());
				}
			}
			boolean flag = ecmInviteCodeService.isInvited(ecmInviteCodeVo);
			if (flag) {
				return ResponseDTO.ok();
			}else {
				return ResponseDTO.fail(ErrorEnum.ERR_515.getText(), null, null, ErrorEnum.ERR_515.getValue());
			}
		}
		return ResponseDTO.ok();
	}

	/**
	 * 通过正则保证图形验证码都是字母加数字
	 *
	 * @param ecmUserVo
	 * @return
	 */
	private String validateCreateText(DefaultKaptcha produce) {
		String createText = produce.createText();
		Matcher matcher = PATTERN.matcher(createText);
		boolean matches = matcher.matches();
		if (matches) {
			return createText;
		} else {
			return this.validateCreateText(produce);
		}
	}

}
