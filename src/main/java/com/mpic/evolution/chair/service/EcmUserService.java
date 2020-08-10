package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.entity.EcmUser;

/**
 * 
 * @author SJ
 *
 */

public interface EcmUserService {
	
	EcmUser getUserInfos(EcmUser record);
	
	boolean savaUser(EcmUser record);

	void saveToken(EcmUser user, String email);

	boolean updatePwdByToken(EcmUser user, String token);

	void updateEcmUserByMobile(EcmUser ecmUser, String mobile);

	void updateIsvalidByToken(EcmUser user, String token);
}
