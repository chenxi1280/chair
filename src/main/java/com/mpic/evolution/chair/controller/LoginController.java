package com.mpic.evolution.chair.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.EcmUserService;
import com.mpic.evolution.chair.service.LoginService;
import com.mpic.evolution.chair.util.MailUtil;

@Controller
public class LoginController extends BaseController {
	
	@Resource
	LoginService loginService;
	
	@Resource
	EcmUserService ecmUserService;

    /**
     * 
     * @author SJ
     */
    @RequestMapping(value = "/loginByWeiXin", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO loginByWeiXin(@RequestParam String code) {
       return loginService.loginByWeiXin(code);
    }
    
    @RequestMapping("/getConfirmCode")
	@ResponseStatus(HttpStatus.OK)
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
    
    @RequestMapping("/sendEmail")
	@ResponseStatus(HttpStatus.OK)
    public void sendEmail() {
    	if(true){
            new Thread(new MailUtil("chenxi1280@outlook.com", UUID.randomUUID().toString())).start();
        }
    }
    
    @RequestMapping("login")
	@ResponseStatus(HttpStatus.OK)
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
    	if (!regionCode.equals(confirmCode)) return ResponseDTO.fail("验证码错误");
    	if (!password.equals(inputPwd)) return ResponseDTO.fail("密码错误");
		return ResponseDTO.ok("成功登录");
    }
    
}
