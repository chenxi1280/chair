package com.mpic.evolution.chair.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
	public void getConfirmCode() throws IOException {
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
		ServletOutputStream sout = response.getOutputStream();
		sout.write(code);
		sout.flush();
		sout.close();
	}
    
    /**
     * 	登录接口
     * @param ecmUserVo
     * @return
     */
    @RequestMapping("login")
    @ResponseBody
    public ResponseDTO loginByToken(EcmUserVo ecmUserVo) {
    	HttpSession session = getRequstSession();
     	String regionCode = (String) session.getAttribute("regionCode");
    	String inputPwd = ecmUserVo.getPassword();
    	//TODO 以后在注册和登陆时都需要对password加密 String encrypt = MD5Utils.encrypt(inputPwd); 
    	String confirmCode = ecmUserVo.getConfirmCode();
    	EcmUser ecmUser = new EcmUser();
    	ecmUser.setMobile(ecmUserVo.getMobile());
    	EcmUser userInfos = ecmUserService.getUserInfos(ecmUser);
    	String password = userInfos.getPassword();
    	if (!regionCode.equals(confirmCode)) {
			return ResponseDTO.fail("验证码错误");
		}
    	if (!password.equals(inputPwd)) {
			return ResponseDTO.fail("密码错误");
		}

		return ResponseDTO.ok("成功登录");
    }
    
    /**
     * 	注册接口
     * @param ecmUserVo
     * @return
     */
	@RequestMapping("register")
	@ResponseBody
	public ResponseDTO registerByUserInfos(EcmUserVo ecmUserVo) {
		//判断该用户是否已注册 根据isvalid=Y 和mobile
		EcmUser user = new EcmUser();
		user.setIsValid("Y");
		user.setMobile(ecmUserVo.getMobile());
		EcmUser userInfos = ecmUserService.getUserInfos(user);
		if (!StringUtil.isNullOrEmpty(userInfos.getPkUserId().toString())) {
			return ResponseDTO.fail("该账号已注册");
		}
		//确认密码验证
		if(!ecmUserVo.getPassword().equals(ecmUserVo.getConfirmPwd())) {
			return ResponseDTO.fail("密码与确认密码不一致");
		}
		//验证码验证
		HttpSession session = getRequstSession();
		String regionCode = (String) session.getAttribute("regionCode");
		String confirmCode = ecmUserVo.getConfirmCode();
		if (!regionCode.equals(confirmCode)) {
			return ResponseDTO.fail("验证码错误");
		}
		//短信验证
		String mobile = ecmUserVo.getMobile();
		String inputPCC = ecmUserVo.getPhoneConfirmCode();
		String[] phoneNumbers = {mobile};
		String phoneConfirmCode = "";
		Random random = new Random();
        for (int i = 0; i < 6; i++) {
        	phoneConfirmCode += random.nextInt(10);
        }
    	try {
			SendStatus status = loginService.sendSMS(phoneConfirmCode,phoneNumbers);
			if (!status.getCode().equals("Ok")) {
				return ResponseDTO.fail("手机发送验证码失败："+status.getMessage());
			}
		} catch (TencentCloudSDKException e) {
			e.printStackTrace();
			return ResponseDTO.fail("手机验证码发送失败");
		}
    	if (inputPCC.equals(phoneConfirmCode)) {
			return ResponseDTO.fail("手机短信验证码错误");
		}
		//入库
		EcmUser ecmUser = new EcmUser();
		ecmUser.setUsername(ecmUserVo.getUsername());
		ecmUser.setIsValid("N");
		ecmUser.setRoles("1");
		ecmUser.setMobile(ecmUserVo.getMobile());
		ecmUser.setEmail(ecmUserVo.getEmail());
		ecmUser.setPassword(MD5Utils.encrypt(ecmUserVo.getPassword()));
		ecmUserService.savaUser(ecmUser);
		return ResponseDTO.ok("注册成功");
	}
	
	@RequestMapping("/sendEmail")
	@ResponseBody
	public void sendEmail(EcmUserVo ecmUserVo) {	
		//发送邮邮件 并更行数据库中的uuid值
		String uuid = UUID.randomUUID().toString();//激活码
		String emailType = ecmUserVo.getEmailType();
		String emailContent = this.getEmailContent(emailType, uuid);
		MailUtil mailUtil = new MailUtil(ecmUserVo.getEmail(),emailContent);
		mailUtil.sendEmail();
		ecmUserService.updateUUID(ecmUserVo.getEmail(),uuid);
    }
	
	@RequestMapping("/activateUser")
	@ResponseBody
	public void activateUser(EcmUserVo ecmUserVo) {	
		//用户点击链接后获取uuid  根据uuid来修改数据库中isvalid状态
		ecmUserService.updateIsValid(ecmUserVo.getToken(),"Y");
		//修改掉uuid在数据库中的值
		String uuid = UUID.randomUUID().toString();
		ecmUserService.clearUUID(ecmUserVo.getToken(),uuid);
    }
	
	@RequestMapping("/forgetPwd")
	@ResponseBody
	public ResponseDTO forgetPassword(EcmUserVo ecmUserVo) {	
		//确认密码验证
		String password = ecmUserVo.getPassword();
		if(!password.equals(ecmUserVo.getConfirmPwd())) {
			return ResponseDTO.fail("密码与确认密码不一致");
		}
		//修改掉数据库中的密码
		boolean flag = ecmUserService.updatePwdByToken(ecmUserVo.getToken(),password);
		if (!flag) {
			return ResponseDTO.ok("密码修改失败");
		}
		//修改掉uuid在数据库中的值
		String uuid = UUID.randomUUID().toString();
		ecmUserService.clearUUID(ecmUserVo.getToken(),uuid);
		return ResponseDTO.fail("密码修改成功");
    }
	
	private String getEmailContent(String emailType, String uuid) {
		String content = "";
		if (emailType.equals("verification")) {
			content = String.format("<html>" + 
					"	<head></head>" + 
					"	<body>" + 
					"		<h1>这是一封激活邮件,激活请点击以下链接</h1>" + 
					"		<h3><a href='http://192.168.1.10:8080/activateUser?token=%s" + 
					"			 		'>http://192.168.1.10:8080/activateUser?token=%s" + 
					"			 		</href>" + 
					"		</h3>" + 
					"	</body>" + 
					"</html>"
					,uuid,uuid);
		}else {
			content = String.format("<html>" + 
					"	<head></head>" + 
					"	<body>" + 
					"		<h1>修改密码链接,请点击以下链接</h1>" + 
					"		<h3><a href='http://192.168.1.10:8080/activateUser?token=%s" + 
					"			 		'>http://192.168.1.10:8080/activateUser?token=%s" + 
					"			 		</href>" + 
					"		</h3>" + 
					"	</body>" + 
					"</html>"
					,uuid,uuid);
		}
		return content;
	}
}
