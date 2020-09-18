package com.mpic.evolution.chair.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.dao.EcmUserDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;
import com.mpic.evolution.chair.service.PersonalCenterService;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-18 16:42:29 
*/
@Service
public class PersonalCenterServiceImpl implements PersonalCenterService {
	
	@Resource
	EcmUserDao ecmUserDao;
	
	@Override
	public ResponseDTO savaUserInfo(EcmUserVo ecmUserVo) {
		EcmUser ecmUser = new EcmUser();
		ecmUser.setGender(ecmUserVo.getGender());
		ecmUser.setBirthday(ecmUserVo.getBirthday());
		ecmUser.setCity(ecmUserVo.getCity());
		ecmUser.setUserLogoUrl(ecmUserVo.getUserLogoUrl());
		ecmUserDao.updateEcmUser(ecmUser, ecmUserVo);
		return null;
	}

}
