package com.mpic.evolution.chair.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.mpic.evolution.chair.common.constant.EmailConstants;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 
 * @author SJ
 *
 */

public class MailUtil implements Runnable {
	
	private String email;//收件人邮箱
	private String code;//激活码
	private String host = "smtp.163.com";// 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
	public MailUtil(String email, String code) {
		super();
		this.email = email;
		this.code = code;
	}

	@Override
	public void run() {
		//1.创建链接对象 javax.mail.Session
		//2.创建右键对象 javax.mail.Message
		//3.发送一封激活邮件
		Properties properties = System.getProperties();//获取系统属性
		properties.setProperty("mail.smtp.host", host);//设置邮件服务器
		properties.setProperty("mail.smtp.auth", "true");//打开认证
		
		//QQ邮箱需要这段代码，163不需要
		try {
			MailSSLSocketFactory mFactory = new MailSSLSocketFactory();
			mFactory.setTrustAllHosts(true);
			properties.put("mail.smtp.ssl.enable", "true");
			properties.put("mail.smtp.ssl.socketFactory", mFactory);
			//获取默认session对象
			Session session = Session.getDefaultInstance(properties, new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(EmailConstants.userName, EmailConstants.password);//发件人邮箱账号和授权码
				}
			});
			
			MimeMessage message = new MimeMessage(session);
			//设置发件人
			message.setFrom(new InternetAddress(EmailConstants.userName));
			//设置接收人
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			//设置邮件主题
			message.setSubject("账号激活");
			//设置邮件内容
			 String content = String.format("<html><head></head><body><h1>这是一封激活邮件,激活请点击以下链接</h1><h3><a href='http://localhost:8080/RegisterDemo/ActiveServlet?code=%s" + 
			 		"'>http://192.168.1.10:8080/RegisterDemo/ActiveServlet?code=%s" + 
			 		"</href></h3></body></html>",code,code);
			 message.setContent(content,"text/html;charset=UTF-8");
			 //发送邮件
			 Transport.send(message);
			 System.out.println("邮件发送成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
