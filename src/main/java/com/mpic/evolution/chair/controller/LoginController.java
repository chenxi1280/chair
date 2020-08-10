package com.mpic.evolution.chair.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.hazelcast.util.StringUtil;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.service.LoginService;
import com.mpic.evolution.chair.util.MD5Utils;
import com.mpic.evolution.chair.util.MailUtil;
import com.mpic.evolution.chair.util.RandomUtil;
import com.mpic.evolution.chair.util.UUIDUtil;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;

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
     * @throws IOException
     */
    @RequestMapping("/getConfirmCode")
    @ResponseBody
	public void getConfirmCode() {
    	ServletOutputStream sout = null;
    	try {
    		byte[] code = null;
        	ByteArrayOutputStream out = new ByteArrayOutputStream();
        	DefaultKaptcha produce = loginService.getConfirmCode();
        	String createText = produce.createText();
        	getRequstSession().setAttribute("regionCode", createText);
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
    	HttpSession session = getRequstSession();
     	String regionCode = (String) session.getAttribute("regionCode");
    	String confirmCode = ecmUserVo.getConfirmCode();
    	if (!regionCode.equals(confirmCode)) {
			return ResponseDTO.fail("验证码错误");
		}
    	String inputPwd = ecmUserVo.getPassword();
    	String encrypt = MD5Utils.encrypt(inputPwd); 
    	EcmUser ecmUser = new EcmUser();
    	//TODO  根据手机号获取用户信息 后续改成token
    	ecmUser.setMobile(ecmUserVo.getMobile());
    	EcmUser userInfos = ecmUserService.getUserInfos(ecmUser);
    	String password = userInfos.getPassword();
    	if (!password.equals(encrypt)) {
			return ResponseDTO.fail("密码错误");
		}
    	//TODO 所有功能完善以后，会将mobile改成用户token
    	EcmUser user = new EcmUser();
    	Integer count = userInfos.getCount();
    	++count;
    	user.setCount(count);
    	user.setLastLoginTime(new Date());
    	user.setUpdateTime(new Date());
    	ecmUserService.updateEcmUserByMobile(user, ecmUserVo.getMobile());
		return ResponseDTO.ok("成功登录");
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
		HttpSession session = getRequstSession();
		String regionCode = (String) session.getAttribute("regionCode");
		String confirmCode = ecmUserVo.getConfirmCode();
		if (!regionCode.equals(confirmCode)) {
			return ResponseDTO.fail("验证码错误");
		}
		//短信验证码验证
		String inputPCC = ecmUserVo.getPhoneConfirmCode();
		String phoneConfirmCode = (String) session.getAttribute("phoneConfirmCode");
    	if (inputPCC.equals(phoneConfirmCode)) {
			return ResponseDTO.fail("手机短信验证码错误");
		}
		//入库 TODO 用户敏感信息需要加密
		EcmUser ecmUser = new EcmUser();
		ecmUser.setUsername(ecmUserVo.getUsername());
		ecmUser.setIsValid("N");
		ecmUser.setRoles("1");
		ecmUser.setCreateTime(new Date());
		ecmUser.setUpdateTime(new Date());
		ecmUser.setMobile(ecmUserVo.getMobile());
		ecmUser.setEmail(ecmUserVo.getEmail());
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
		String mobile = "86"+ecmUserVo.getMobile();
		String[] phoneNumbers = {mobile};
		String phoneConfirmCode = RandomUtil.getCode();
		getRequstSession().setAttribute("phoneConfirmCode", phoneConfirmCode);
    	try {
			SendStatus status = loginService.sendSMS(phoneConfirmCode,phoneNumbers);
			if (!status.getCode().equals("Ok")) {
				return ResponseDTO.fail("手机验证码发送失败："+status.getMessage());
			}
		} catch (TencentCloudSDKException e) {
			e.printStackTrace();
			return ResponseDTO.fail("手机验证码发送失败");
		}
		return ResponseDTO.ok("手机验证码发送成功");
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
			ecmUserService.saveToken(user,ecmUserVo.getEmail());//发送邮邮件 并更新emailUuid值
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
		ecmUserService.updateIsvalidByToken(user,ecmUserVo.getToken());
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
		boolean flag = ecmUserService.updatePwdByToken(user,ecmUserVo.getToken());
		if (!flag) {
			return ResponseDTO.ok("密码修改失败");
		}
		return ResponseDTO.fail("密码修改成功");
    }
	
}
