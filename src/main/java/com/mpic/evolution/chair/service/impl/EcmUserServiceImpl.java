package com.mpic.evolution.chair.service.impl;

import javax.annotation.Resource;

import com.mpic.evolution.chair.dao.EcmUserDao;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.service.EcmUserService;

public class EcmUserServiceImpl implements EcmUserService {
	
	@Resource
	EcmUserDao ecmUserDao;

	@Override
	public EcmUser getUserInfos(EcmUser record) {
		EcmUser ecmUser = ecmUserDao.selectByRecord(record);
		return ecmUser;
	}

}
