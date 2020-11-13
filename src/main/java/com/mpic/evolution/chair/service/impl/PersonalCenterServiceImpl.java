package com.mpic.evolution.chair.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import com.mpic.evolution.chair.common.constant.SecretKeyConstants;
import com.mpic.evolution.chair.util.EncryptUtil;
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
		ecmUser.setUserLogoStatus((short)0);
		ecmUser.setUsername(ecmUserVo.getUsername());
		ecmUser.setUpdateTime(new Date());

		try {
			ecmUserVo.setMobile(EncryptUtil.aesEncrypt(ecmUserVo.getMobile(), SecretKeyConstants.secretKey));
		} catch (Exception e) {
			e.printStackTrace();
		}
		ecmUserVo.setUserLogoStatus((short)0);
		ecmUserVo.setUpdateTime(new Date());
//		int row = ecmUserDao.updateEcmUser(ecmUser, ecmUserVo);
		ecmUserVo.setMobile(null);
		int row = ecmUserDao.updateByPrimaryKeySelective(ecmUserVo);
		if (row<1) {
			return ResponseDTO.fail("保存信息失败", null, null, "000039");
		}else {
			return ResponseDTO.ok("保存信息成功");
		}
	}

}
