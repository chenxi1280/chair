package com.mpic.evolution.chair.service.impl;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.mpic.evolution.chair.dao.EcmUserDao;
import com.mpic.evolution.chair.dao.WxUserDao;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.entity.WxUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.WxLoginService;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-27 9:49:24 
*/

@Service
public class WxLoginServiceImpl implements WxLoginService {
	
	@Resource
	EcmUserDao ecmUserDao;
	
	@Resource
	WxUserDao wxUserDao;
	
	@Override
	public DefaultKaptcha getConfirmCode() {
		return this.produce();
       
	}
	
	private DefaultKaptcha produce() {
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "no");// 图片边框
		properties.setProperty("kaptcha.border.color", "black");// 边框颜色
		properties.setProperty("kaptcha.textproducer.font.color", "black");// 字体颜色
		properties.setProperty("kaptcha.image.width", "125");// 图片宽
		properties.setProperty("kaptcha.image.height", "45");// 图片高
		properties.setProperty("kaptcha.session.key", "code");// session key
		properties.setProperty("kaptcha.textproducer.font.size", "38");// 字体大小
		properties.setProperty("kaptcha.noise.color", "245,255,250");
		properties.setProperty("kaptcha.background.clear.from", "240,128,128");
		properties.setProperty("kaptcha.background.clear.to", "240,128,128");
		properties.setProperty("kaptcha.textproducer.char.length", "4");// 验证码长度
		properties.setProperty("kaptcha.textproducer.font.names", "Arial");// 字体
		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);
		return defaultKaptcha;
	}

	@Override
	public boolean savaUser(EcmUser ecmUser, EcmUserVo ecmUserVo) {
		int ecmRow = ecmUserDao.insertSelective(ecmUser);
		Integer userId = ecmUser.getPkUserId();
		String openid = ecmUserVo.getOpenid();
		WxUser wxUser1 = new WxUser();
		wxUser1.setFkUserId(userId);
		WxUser wxUser2 = new WxUser();
		wxUser2.setOpenid(openid);
		int wxRow = wxUserDao.updateByWxUser(wxUser1, wxUser2);
		return ecmRow < 1 ? false : wxRow < 1 ? false : true;	
	}

}
