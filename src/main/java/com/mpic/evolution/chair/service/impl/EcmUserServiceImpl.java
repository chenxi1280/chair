package com.mpic.evolution.chair.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.dao.EcmUserDao;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.service.EcmUserService;

@Service
public class EcmUserServiceImpl implements EcmUserService {
	
	@Resource
	EcmUserDao ecmUserDao;

	@Override
	public EcmUser getUserInfos(EcmUser record) {
		return ecmUserDao.selectByRecord(record);
	}

	@Override
	public boolean savaUser(EcmUser record) {
		return ecmUserDao.insert(record) < 1 ? false : true;
	}

	@Override
	public void saveToken(EcmUser user, String email) {
		ecmUserDao.updateTokenByEmail(user,email);
	}

	@Override
	public boolean updatePwdByToken(EcmUser user, String token) {
		int flag = ecmUserDao.updatePwdByToken(user, token);
		if (flag<0) return false;
		return true;
	}

	@Override
	public void updateEcmUserByMobile(EcmUser ecmUser, String mobile) {
		 ecmUserDao.updateByMobile(ecmUser, mobile);
	}

	@Override
	public void updateIsvalidByToken(EcmUser user, String token) {
		ecmUserDao.updateIsvalidByToken(user,token);
		
	}

}
