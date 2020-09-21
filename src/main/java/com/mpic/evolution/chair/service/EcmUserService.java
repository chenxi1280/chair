package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmUser;
import com.mpic.evolution.chair.pojo.query.EcmUserFlowQuery;
import com.mpic.evolution.chair.pojo.vo.EcmUserHistoryFlowVO;
import com.mpic.evolution.chair.pojo.vo.EcmUserVo;

/**
 * 
 * @author SJ
 *
 */

public interface EcmUserService {
	
	EcmUser getUserInfos(EcmUser record);
	
	boolean savaUser(EcmUser record);

	void saveToken(EcmUser user, EcmUserVo userVo);

	boolean updatePwdByToken(EcmUser user, EcmUserVo userVo);

	void updateEcmUserById(EcmUser ecmUser, EcmUserVo userVo);

	void updateIsvalidByToken(EcmUser user, EcmUserVo userVo);

	ResponseDTO webGetUserInfo(EcmUser ecmUser);

    ResponseDTO inspectFlow(EcmUserFlowQuery ecmUserFlowQuery);

	ResponseDTO reduceFlow(EcmUserHistoryFlowVO ecmUserHistoryFlowVO);

	EcmUser getUserInfosByUserId(Integer userIdByHandToken);

	boolean updatePwdByUserId(EcmUser user);

}
