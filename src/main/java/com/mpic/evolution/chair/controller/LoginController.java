package com.mpic.evolution.chair.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.LoginService;
import com.mpic.evolution.chair.util.MailUtil;

@Controller
public class LoginController {
	
	@Resource
	LoginService loginService;

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
	public void getConfirmCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	byte[] code = null;
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	DefaultKaptcha produce = loginService.getConfirmCode();
    	String createText = produce.createText();
    	request.getSession().setAttribute("regionCode", createText);
    	BufferedImage bi = produce.createImage(createText);
    	ImageIO.write(bi, "jpg", out);
    	code = out.toByteArray();
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
    	//�����û�������ӷ��ص�code �����ж��Ƿ�ͨ����֤
    	if(true){
            new Thread(new MailUtil("chenxi1280@outlook.com", UUID.randomUUID().toString())).start();
        }
    }
    
    //TODO �û���¼�ӿ� ����
    @RequestMapping("login")
	@ResponseStatus(HttpStatus.OK)
    public void loginByToken(EcmUserVo ecmUserVo) {
    	
    	//�����û�������ӷ��ص�code �����ж��Ƿ�ͨ����֤
    	if(true){
            new Thread(new MailUtil("chenxi1280@outlook.com", UUID.randomUUID().toString())).start();
        }
    }
    
    //TODO �û�ע��ӿ�md5����
    
    //TODO �һ����� ������֤����û����û������룬ȷ�������ͨ������url�����޸�����ҳ��
}
