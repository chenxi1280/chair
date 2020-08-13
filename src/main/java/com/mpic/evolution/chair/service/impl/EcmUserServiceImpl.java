package com.mpic.evolution.chair.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.dao.EcmUserDao;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
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
	public void saveToken(EcmUser user, EcmUserVo userVo) {
		ecmUserDao.updateEcmUser(user,userVo);
	}

	@Override
	public boolean updatePwdByToken(EcmUser user, EcmUserVo userVo) {
		int flag = ecmUserDao.updateEcmUser(user, userVo);
		if (flag<0) return false;
		return true;
	}

	@Override
	public void updateEcmUserById(EcmUser ecmUser, EcmUserVo userVo) {
		 ecmUserDao.updateEcmUser(ecmUser, userVo);
	}

	@Override
	public void updateIsvalidByToken(EcmUser user, EcmUserVo userVo) {
		ecmUserDao.updateEcmUser(user,userVo);
		
	}

}
