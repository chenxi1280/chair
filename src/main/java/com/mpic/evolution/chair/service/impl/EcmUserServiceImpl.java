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
	public void updateUUID(String email, String uuid) {
		ecmUserDao.updateTokenByEmail(email,uuid);
	}

	@Override
	public void updateIsValid(String token, String isValid) {
		ecmUserDao.updateIsValidByUUID(token,isValid);
	}

	@Override
	public void clearUUID(String token, String uuid) {
		ecmUserDao.clearUUID(token, uuid);
		
	}

	@Override
	public boolean updatePwdByToken(String token, String password) {
		int flag = ecmUserDao.updatePwdByToken(token, password);
		if (flag<0) return false;
		return true;
	}

}
