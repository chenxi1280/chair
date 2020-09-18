package com.mpic.evolution.chair.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.dao.EcmInviteCodeDao;
import com.mpic.evolution.chair.pojo.entity.EcmInviteCode;
import com.mpic.evolution.chair.service.EcmInviteCodeService;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-17 19:59:01 
*/
@Service
public class EcmInviteCodeServiceImpl implements EcmInviteCodeService {
	
	@Resource
    EcmInviteCodeDao ecmInviteCodeDao;
	
	@Override
	public boolean isInvited(EcmInviteCode ecmInviteCode) {
		EcmInviteCode inviteCode = ecmInviteCodeDao.selectByEcmInviteCode(ecmInviteCode);
		Integer pkInviteId = inviteCode.getPkInviteId();
		if(pkInviteId != null) {
			return true;
		}else {
			return false;
		}
	}

}
