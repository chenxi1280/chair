package com.mpic.evolution.chair.util;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.mpic.evolution.chair.common.constant.EmailConstants;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 *
 * @author SJ
 *
 */

public class MailUtil{

	private String email;//收件人邮箱
	private String emailType;//邮件内容
	private String subject;
	private String uuid;
	private String host = "smtp.163.com";// 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
	public MailUtil(String email, String emailType, String uuid, String subject) {
		super();
		this.email = email;
		this.emailType = emailType;
		this.subject = subject;
		this.uuid = uuid;
	}

	public void sendEmail() throws AddressException, MessagingException, GeneralSecurityException {
		// 1.创建链接对象 javax.mail.Session
		// 2.创建右键对象 javax.mail.Message
		// 3.发送一封激活邮件
		Properties properties = System.getProperties();// 获取系统属性
		properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
		properties.setProperty("mail.smtp.auth", "true");// 打开认证
		// QQ邮箱需要这段代码，163不需要
		MailSSLSocketFactory mFactory = new MailSSLSocketFactory();
		mFactory.setTrustAllHosts(true);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.ssl.socketFactory", mFactory);
		// 获取默认session对象
		Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EmailConstants.USER_NAME, EmailConstants.PASSWORD);// 发件人邮箱账号和授权码
			}
		});
		MimeMessage message = new MimeMessage(session);
		// 设置发件人
		message.setFrom(new InternetAddress(EmailConstants.USER_NAME));
		// 设置接收人
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		// 设置邮件主题
		message.setSubject(subject);
		// 设置邮件内容
		message.setContent(this.getEmailContent(emailType, uuid), "text/html;charset=UTF-8");
		// 发送邮件
		Transport.send(message);
		System.out.println("邮件发送成功");
	}

	private String getEmailContent(String emailType, String uuid) {
		String content = "";
		String verification="verification";
		if (verification.equals(emailType)) {
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
					"		<h3><a href='http://192.168.1.9:8080/#/login/setpsd?token=%s" +
					"			 		'>http://192.168.1.9:8080/#/login/setpsd?token=%s" +
					"			 		</href>" +
					"		</h3>" +
					"	</body>" +
					"</html>"
					,uuid,uuid);
		}
		return content;
	}

}
