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

	void clearUUID(String token, String uuid);

	void updateIsValid(String token, String isValid);

	void updateUUID(String email, String uuid);

	boolean updatePwdByToken(String token, String password);
}
