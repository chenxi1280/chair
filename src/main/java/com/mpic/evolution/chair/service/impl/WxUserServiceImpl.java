package com.mpic.evolution.chair.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.dao.EcmUserDao;
import com.mpic.evolution.chair.dao.WxUserDao;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.entity.WxUser;
import com.mpic.evolution.chair.service.WxUserService;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-28 14:23:56 
*/
@Service
public class WxUserServiceImpl implements WxUserService {
	
	@Resource
	WxUserDao wxUserDao;
	
	@Resource
	EcmUserDao ecmUseDao;

	@Override
	public Integer savaWxUer(WxUser wxUser) {
		//先判断openid这条记录在数据库中存不存在
		WxUser user = wxUserDao.selectByWxUser(wxUser);
		if (user != null && !StringUtils.isEmpty(String.valueOf(user.getFkUserId()))) {
			//openid已存在就不需要保存 直接返回 返回值任意
			return user.getFkUserId();
		}
		EcmUser ecmUser = new EcmUser();
		ecmUser.setPassword("123456");
		ecmUser.setIsValid("N");
		ecmUser.setRoles("0");
		int ecmRow = ecmUseDao.insertSelective(ecmUser);
		Integer userId = ecmUser.getPkUserId();
		wxUser.setFkUserId(userId);
		wxUser.setCtime(new Date());
		wxUser.setAuthorized(false);
		int row = wxUserDao.insertSelective(wxUser);
		if (ecmRow < 1 ? false : row < 1 ? false : true) {
			return wxUser.getFkUserId();
		}else {
			return null;
		}
	}

}
