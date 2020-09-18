package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.entity.EcmInviteCode;
import com.mpic.evolution.chair.pojo.vo.EcmInviteCodeVo;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-17 19:58:21 
*/
public interface EcmInviteCodeService {

	boolean isInvited( EcmInviteCodeVo ecmInviteCodeVo);
	
	EcmInviteCode getEcmInvitedCode(String inviteCode);
	
	boolean savaEcmInvitedCode(EcmInviteCode ecmInviteCode);
}
