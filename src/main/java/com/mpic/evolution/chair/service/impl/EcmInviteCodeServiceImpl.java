package com.mpic.evolution.chair.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mpic.evolution.chair.dao.EcmInviteCodeDao;
import com.mpic.evolution.chair.pojo.entity.EcmInviteCode;
import com.mpic.evolution.chair.pojo.vo.EcmInviteCodeVo;
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
	public boolean isInvited(EcmInviteCodeVo ecmInviteCodeVo) {
		String inviteCode = ecmInviteCodeVo.getInviteCode();
		EcmInviteCode ecmInviteCode = new EcmInviteCode();
		ecmInviteCode.setInviteCode(inviteCode);
		ecmInviteCode.setInviteCodeStatus((short)0);
		EcmInviteCode o = ecmInviteCodeDao.selectByEcmInviteCode(ecmInviteCode);
		if(o == null || o.getPkInviteId() == null) {
			return false;
		}else {
			return true;
		}
	}

	@Override
	public EcmInviteCode getEcmInvitedCode(String inviteCode) {
		EcmInviteCode ecmInviteCode = new EcmInviteCode();
		ecmInviteCode.setInviteCode(inviteCode);
		EcmInviteCode o = ecmInviteCodeDao.selectByEcmInviteCode(ecmInviteCode);
		return o;
	}

	@Override
	public boolean savaEcmInvitedCode(EcmInviteCode ecmInviteCode) {
		return ecmInviteCodeDao.updateByPrimaryKey(ecmInviteCode) < 1 ? false : true;
	}

}
